package com.side.project.air.utils

object Contracts {
    /**
     * Variable
     */
    const val PERMISSION_CODE = 0

    /**
     * Permission
     */
    const val PERMISSION_FINE_LOCATION =  android.Manifest.permission.ACCESS_FINE_LOCATION
    const val PERMISSION_COARSE_LOCATION =  android.Manifest.permission.ACCESS_COARSE_LOCATION

    val location_permission = arrayOf(PERMISSION_FINE_LOCATION, PERMISSION_COARSE_LOCATION)
}