package com.side.project.air.data.weatherApi.weather

data class Location(
    val dataid: String,
    val datasetDescription: String,
    val location: List<LocationX>,
    val locationsName: String
)