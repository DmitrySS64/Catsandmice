package ru.sergeevdmitry8i11.catsandmice

data class GameStats(
    val id: Int = 0,
    val totalClicks: Int,
    val mouseClicks: Int,
    val gameTime: String,
    val percentage: Float
)
