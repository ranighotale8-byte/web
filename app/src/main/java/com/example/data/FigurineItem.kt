package com.example.data

import androidx.compose.ui.graphics.Color

data class FigurineItem(
    val id: Int,
    val name: String,
    val subtitle: String,
    val imageUrl: String,
    val backgroundColor: Color,
    val panelColor: Color,
    val description: String,
    val edition: String,
    val scale: String,
    val height: String,
    val weight: String
)

val FIGURINE_ITEMS = listOf(
    FigurineItem(
        id = 0,
        name = "BLAZE SQUAD",
        subtitle = "CHROME VIXEN #01",
        imageUrl = "https://fifth-gentle-45902158.figma.site/_components/v2/4de492f6d9cf8244ad5293233e5c6f52407d42fc/1.02464a56.png",
        backgroundColor = Color(0xFFF4845F),
        panelColor = Color(0xFFF79B7F),
        description = "The artwork is stunning, shipped fully prepared. The finish is a vision, the 3D craft is flawless. Many thanks! Wishing you the win. Order now.",
        edition = "1 of 250 Limited Edition",
        scale = "1:6 Scale Premium Vinyl",
        height = "28.5 cm",
        weight = "820 g"
    ),
    FigurineItem(
        id = 1,
        name = "MINTY FRESH",
        subtitle = "CYBER RABBIT #02",
        imageUrl = "https://fifth-gentle-45902158.figma.site/_components/v2/4de492f6d9cf8244ad5293233e5c6f52407d42fc/2.b977faab.png",
        backgroundColor = Color(0xFF6BBF7A),
        panelColor = Color(0xFF85CC92),
        description = "Precision molded vinyl figurine featuring ultra-gloss mint finish and hand-painted metallic highlights.",
        edition = "1 of 180 Collector Series",
        scale = "1:6 Scale Matte & Gloss",
        height = "30.0 cm",
        weight = "910 g"
    ),
    FigurineItem(
        id = 2,
        name = "BUBBLE GUM",
        subtitle = "NEON DREAMER #03",
        imageUrl = "https://fifth-gentle-45902158.figma.site/_components/v2/4de492f6d9cf8244ad5293233e5c6f52407d42fc/3.4df853b4.png",
        backgroundColor = Color(0xFFE882B4),
        panelColor = Color(0xFFED9DC4),
        description = "Vibrant pastel pop art figurine with custom metallic accents and high-density resin core.",
        edition = "1 of 300 Art Toy Edition",
        scale = "1:6 Scale Custom Sculpt",
        height = "27.0 cm",
        weight = "780 g"
    ),
    FigurineItem(
        id = 3,
        name = "SKY SURFER",
        subtitle = "AZURE BLAZER #04",
        imageUrl = "https://fifth-gentle-45902158.figma.site/_components/v2/4de492f6d9cf8244ad5293233e5c6f52407d42fc/4.4457fbce.png",
        backgroundColor = Color(0xFF6EB5FF),
        panelColor = Color(0xFF8DC4FF),
        description = "Dynamic aerial posture with iridescent sky blue gradient and translucent display pedestal.",
        edition = "1 of 150 Master Sculpture",
        scale = "1:6 Scale Display Model",
        height = "32.0 cm",
        weight = "950 g"
    )
)
