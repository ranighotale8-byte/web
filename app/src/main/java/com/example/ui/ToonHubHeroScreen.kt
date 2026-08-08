package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.R
import com.example.data.FIGURINE_ITEMS
import com.example.data.FigurineItem
import com.example.ui.components.FigurineDetailSheet
import com.example.ui.components.GrainOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class CarouselRole {
    CENTER, LEFT, RIGHT, BACK
}

@Composable
fun ToonHubHeroScreen() {
    var activeIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    var showDetailSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val antonFont = remember { FontFamily(Font(R.font.anton, FontWeight.Normal)) }

    // Navigation logic with 650ms animation lock
    fun navigate(direction: String) {
        if (isAnimating) return
        isAnimating = true

        activeIndex = if (direction == "next") {
            (activeIndex + 1) % 4
        } else {
            (activeIndex + 3) % 4
        }

        coroutineScope.launch {
            delay(650)
            isAnimating = false
        }
    }

    val activeItem = FIGURINE_ITEMS[activeIndex]

    // Animated background color transition (650ms CubicBezier / FastOutSlowIn)
    val animatedBgColor by animateColorAsState(
        targetValue = activeItem.backgroundColor,
        animationSpec = tween(
            durationMillis = 650,
            easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
        ),
        label = "bgColorAnimation"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBgColor)
            .pointerInput(Unit) {
                var totalDrag = 0f
                detectHorizontalDragGestures(
                    onDragStart = { totalDrag = 0f },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount
                    },
                    onDragEnd = {
                        if (totalDrag < -50f) {
                            navigate("next")
                        } else if (totalDrag > 50f) {
                            navigate("prev")
                        }
                    }
                )
            }
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val isMobile = screenWidth < 600.dp || screenHeight < 700.dp

        // 1. Grain Noise Overlay (zIndex 50)
        GrainOverlay(
            modifier = Modifier.zIndex(50f),
            opacity = 0.08f
        )

        // 2. Top-Left Brand Label "TOONHUB" (zIndex 60)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 20.dp, start = 20.dp)
                .zIndex(60f)
        ) {
            Text(
                text = "TOONHUB",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.4.sp,
                modifier = Modifier.testTag("brand_label")
            )
        }

        // 3. Giant Ghost Text "3D SHAPE" (zIndex 2)
        val ghostFontSize = if (isMobile) {
            (screenWidth.value * 0.22f).coerceIn(80f, 180f).sp
        } else {
            (screenWidth.value * 0.26f).coerceIn(120f, 360f).sp
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.TopCenter)
                .padding(top = if (isMobile) screenHeight * 0.14f else screenHeight * 0.16f)
                .zIndex(2f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "3D SHAPE",
                fontFamily = antonFont,
                fontSize = ghostFontSize,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-2).sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }

        // 4. Character Carousel Items (zIndex 3)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(3f)
        ) {
            FIGURINE_ITEMS.forEachIndexed { index, item ->
                val role = when (index) {
                    activeIndex -> CarouselRole.CENTER
                    (activeIndex + 3) % 4 -> CarouselRole.LEFT
                    (activeIndex + 1) % 4 -> CarouselRole.RIGHT
                    else -> CarouselRole.BACK
                }

                AnimatedCarouselItem(
                    item = item,
                    role = role,
                    isMobile = isMobile,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    onItemClick = {
                        if (role == CarouselRole.CENTER) {
                            showDetailSheet = true
                        } else if (role == CarouselRole.LEFT) {
                            navigate("prev")
                        } else if (role == CarouselRole.RIGHT) {
                            navigate("next")
                        }
                    }
                )
            }
        }

        // 5. Bottom-Left Text + Navigation Buttons (zIndex 60)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = if (isMobile) 20.dp else 40.dp, start = if (isMobile) 20.dp else 48.dp)
                .zIndex(60f),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                modifier = Modifier.widthIn(max = 340.dp)
            ) {
                Text(
                    text = "TOONHUB FIGURINES",
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isMobile) 16.sp else 22.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(if (isMobile) 4.dp else 8.dp))

                Text(
                    text = activeItem.description,
                    fontSize = if (isMobile) 11.sp else 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = if (isMobile) 16.sp else 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(if (isMobile) 12.dp else 18.dp))

                // Circular Arrow Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularNavButton(
                        onClick = { navigate("prev") },
                        isMobile = isMobile,
                        testTag = "prev_button"
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Figurine",
                            tint = Color.White,
                            modifier = Modifier.size(if (isMobile) 22.dp else 26.dp)
                        )
                    }

                    CircularNavButton(
                        onClick = { navigate("next") },
                        isMobile = isMobile,
                        testTag = "next_button"
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Figurine",
                            tint = Color.White,
                            modifier = Modifier.size(if (isMobile) 22.dp else 26.dp)
                        )
                    }
                }
            }
        }

        // 6. Bottom-Right Link "DISCOVER IT" (zIndex 60)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = if (isMobile) 24.dp else 44.dp, end = if (isMobile) 20.dp else 40.dp)
                .zIndex(60f),
            contentAlignment = Alignment.BottomEnd
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showDetailSheet = true
                    }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .testTag("discover_button"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "DISCOVER IT",
                    fontFamily = antonFont,
                    fontSize = if (isMobile) 22.sp else 36.sp,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(if (isMobile) 22.dp else 30.dp)
                )
            }
        }

        // Detail Sheet Dialog
        if (showDetailSheet) {
            FigurineDetailSheet(
                item = activeItem,
                onDismiss = { showDetailSheet = false }
            )
        }
    }
}

@Composable
private fun CircularNavButton(
    onClick: () -> Unit,
    isMobile: Boolean,
    testTag: String,
    content: @Composable () -> Unit
) {
    val buttonSize = if (isMobile) 48.dp else 58.dp

    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, Color.White), CircleShape)
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun AnimatedCarouselItem(
    item: FigurineItem,
    role: CarouselRole,
    isMobile: Boolean,
    screenWidth: Dp,
    screenHeight: Dp,
    onItemClick: () -> Unit
) {
    // Role-based target styling calculations:
    // center: translateX(-50%) scale(isMobile ? 1.25 : 1.68), blur 0dp, opacity 1, zIndex 20, left 50%, height (isMobile ? 60% : 92%), bottom (isMobile ? 22% : 0)
    // left: translateX(-50%) scale(1), blur 2dp, opacity 0.85, zIndex 10, left (isMobile ? 20% : 30%), height (isMobile ? 16% : 28%), bottom (isMobile ? 32% : 12%)
    // right: left (isMobile ? 80% : 70%)
    // back: left 50%, height (isMobile ? 13% : 22%), bottom (isMobile ? 32% : 12%), blur 4dp, opacity 1.0, zIndex 5

    val targetScale = when (role) {
        CarouselRole.CENTER -> if (isMobile) 1.25f else 1.68f
        else -> 1.0f
    }

    val targetBlur = when (role) {
        CarouselRole.CENTER -> 0.dp
        CarouselRole.LEFT, CarouselRole.RIGHT -> 2.dp
        CarouselRole.BACK -> 4.dp
    }

    val targetOpacity = when (role) {
        CarouselRole.CENTER -> 1.0f
        CarouselRole.LEFT, CarouselRole.RIGHT -> 0.85f
        CarouselRole.BACK -> 0.95f
    }

    val targetZIndex = when (role) {
        CarouselRole.CENTER -> 20f
        CarouselRole.LEFT, CarouselRole.RIGHT -> 10f
        CarouselRole.BACK -> 5f
    }

    val targetLeftFraction = when (role) {
        CarouselRole.CENTER, CarouselRole.BACK -> 0.50f
        CarouselRole.LEFT -> if (isMobile) 0.20f else 0.30f
        CarouselRole.RIGHT -> if (isMobile) 0.80f else 0.70f
    }

    val targetHeightFraction = when (role) {
        CarouselRole.CENTER -> if (isMobile) 0.60f else 0.90f
        CarouselRole.LEFT, CarouselRole.RIGHT -> if (isMobile) 0.16f else 0.28f
        CarouselRole.BACK -> if (isMobile) 0.13f else 0.22f
    }

    val targetBottomOffsetFraction = when (role) {
        CarouselRole.CENTER -> if (isMobile) 0.20f else 0.0f
        else -> if (isMobile) 0.32f else 0.12f
    }

    val animationSpec = tween<Float>(
        durationMillis = 650,
        easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
    )

    val animatedScale by animateFloatAsState(targetValue = targetScale, animationSpec = animationSpec, label = "scale")
    val animatedOpacity by animateFloatAsState(targetValue = targetOpacity, animationSpec = animationSpec, label = "opacity")
    val animatedLeftFraction by animateFloatAsState(targetValue = targetLeftFraction, animationSpec = animationSpec, label = "left")
    val animatedHeightFraction by animateFloatAsState(targetValue = targetHeightFraction, animationSpec = animationSpec, label = "height")
    val animatedBottomFraction by animateFloatAsState(targetValue = targetBottomOffsetFraction, animationSpec = animationSpec, label = "bottom")

    val animatedBlurDp by animateDpAsState(
        targetValue = targetBlur,
        animationSpec = tween(durationMillis = 650, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)),
        label = "blur"
    )

    val itemHeight = screenHeight * animatedHeightFraction
    val itemBottomOffset = screenHeight * animatedBottomFraction
    val itemCenterHorizontal = screenWidth * animatedLeftFraction

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(targetZIndex)
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = itemCenterHorizontal - (itemHeight * 0.3f), // Center aligned horizontally based on 0.6 aspect ratio width
                    y = screenHeight - itemHeight - itemBottomOffset
                )
                .height(itemHeight)
                .width(itemHeight * 0.6f) // aspect ratio 0.6 / 1
                .scale(animatedScale)
                .alpha(animatedOpacity)
                .then(if (animatedBlurDp > 0.dp) Modifier.blur(animatedBlurDp) else Modifier)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onItemClick()
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.BottomCenter
            )
        }
    }
}
