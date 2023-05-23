package com.side.project.air.utils

object Contracts {
    /**
     * Variable
     */
    const val PERMISSION_CODE = 0
    const val MAX_AUTO_MODE_TEMPERATURE = 32
    const val MIN_AUTO_MODE_TEMPERATURE = 23
    const val MAX_TEMPERATURE = 30
    const val MIN_TEMPERATURE = 20
    const val MAX_HUMIDITY = 100
    const val MIN_HUMIDITY = 0

    /**
     * Permission
     */
    const val PERMISSION_FINE_LOCATION =  android.Manifest.permission.ACCESS_FINE_LOCATION
    const val PERMISSION_COARSE_LOCATION =  android.Manifest.permission.ACCESS_COARSE_LOCATION

    val location_permission = arrayOf(PERMISSION_FINE_LOCATION, PERMISSION_COARSE_LOCATION)

    /**
     * MQTT Topic
     */
    // 溫度 Topic (僅訂閱)
    const val TEMPERATURE_TOPIC = "/ntut/iot/air/temperature"
    // 濕度 Topic (僅訂閱)
    const val HUMIDITY_TOPIC = "/ntut/iot/air/humidity"
    // 開關冷氣 Topic (僅發布)
    const val ACTIVATE_TOPIC = "/ntut/iot/air/activate" // 0 or 1
    // 設定溫度閥值 Topic (訂閱/發布)
    const val SET_TEMPERATURE_TOPIC = "/ntut/iot/air/autoMode/setValue" // 23 ~ 32
    // 開關自動模式 Topic (訂閱/發布)
    const val AUTO_MODE_TOPIC = "/ntut/iot/air/autoMode" // 0 or 1
}