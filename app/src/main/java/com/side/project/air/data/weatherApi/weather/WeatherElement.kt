package com.side.project.air.data.weatherApi.weather

data class WeatherElement(
    val description: String,
    val elementName: String,
    val time: List<Time>
)