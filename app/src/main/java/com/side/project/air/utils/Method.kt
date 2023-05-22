package com.side.project.air.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.side.project.air.utils.Contracts.PERMISSION_CODE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Method {
    /**
     * Logcat
     */
    fun logE(tag: String, message: String) =
        Log.e(tag, message)

    /**
     * Permissions
     */
    fun requestPermission(activity: Activity, vararg permissions: String): Boolean {
        return if (!hasPermissions(activity, *permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CODE)
            false
        } else
            true
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions)
            if (ActivityCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED)
                return false
        return true
    }

    /**
     * Get Area Code
     */
    fun getCodeByAreaName(areaName: String): String {
        return when (areaName) {
            "宜蘭縣" -> "F-D0047-001"
            "桃園市" -> "F-D0047-005"
            "新竹縣" -> "F-D0047-009"
            "苗栗縣" -> "F-D0047-013"
            "彰化縣" -> "F-D0047-017"
            "南投縣" -> "F-D0047-021"
            "雲林縣" -> "F-D0047-025"
            "嘉義縣" -> "F-D0047-029"
            "屏東縣" -> "F-D0047-033"
            "臺東縣" -> "F-D0047-037"
            "花蓮縣" -> "F-D0047-041"
            "澎湖縣" -> "F-D0047-045"
            "基隆市" -> "F-D0047-049"
            "新竹市" -> "F-D0047-053"
            "嘉義市" -> "F-D0047-057"
            "臺北市" -> "F-D0047-061"
            "高雄市" -> "F-D0047-065"
            "新北市" -> "F-D0047-069"
            "臺中市" -> "F-D0047-073"
            "臺南市" -> "F-D0047-077"
            "連江縣" -> "F-D0047-081"
            "金門縣" -> "F-D0047-085"
            else -> ""
        }
    }

    /**
     * Get Current Date Time
     */
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    /**
     * Get Current Date
     */
    fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDateTime.format(formatter)
    }

    /**
     * Get Weather Type
     */
    fun getWeatherType(weatherCode: Int): String {
        val weatherTypes = mapOf(
            "isThunderstorm" to listOf(15, 16, 17, 18, 21, 22, 33, 34, 35, 36, 41),
            "isClear" to listOf(1),
            "isCloudyFog" to listOf(25, 26, 27, 28),
            "isCloudy" to listOf(2, 3, 4, 5, 6, 7),
            "isFog" to listOf(24),
            "isPartiallyClearWithRain" to listOf(
                8, 9, 10, 11, 12,
                13, 14, 19, 20, 29, 30,
                31, 32, 38, 39
            ),
            "isSnowing" to listOf(23, 37, 42)
        )

        return weatherTypes.entries.firstOrNull { entry ->
            entry.value.contains(weatherCode)
        }?.key ?: "Unknown"
    }
}