package com.side.project.air.data.weatherApi.weather

data class LocationX(
    val geocode: String,
    val lat: String,
    val locationName: String,
    val lon: String,
    val weatherElement: List<WeatherElement>
)