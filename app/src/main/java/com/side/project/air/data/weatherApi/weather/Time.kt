package com.side.project.air.data.weatherApi.weather

data class Time(
    val dataTime: String,
    val elementValue: List<ElementValue>,
    val endTime: String,
    val startTime: String
)