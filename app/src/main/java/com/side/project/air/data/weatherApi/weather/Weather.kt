package com.side.project.air.data.weatherApi.weather

data class Weather(
    val records: Records,
    val result: Result,
    val success: String
)