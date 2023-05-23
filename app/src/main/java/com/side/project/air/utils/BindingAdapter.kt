package com.side.project.air.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load
import com.side.project.air.R
import com.side.project.air.data.WeatherStateIcon
import com.side.project.air.utils.Contracts.MAX_HUMIDITY
import com.side.project.air.utils.Contracts.MAX_TEMPERATURE
import com.side.project.air.utils.Contracts.MIN_HUMIDITY
import com.side.project.air.utils.Contracts.MIN_TEMPERATURE
import com.side.project.air.utils.widget.CircularSeekBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BindingAdapter {
    @BindingAdapter("android:loadAnyImage")
    @JvmStatic
    fun setLoadImage(imageView: ImageView, any: Any) {
        try {
            imageView.load(any, imageLoader = imageView.context.imageLoader)
        } catch (ignored: Exception) {
        }
    }

    @BindingAdapter("android:loadWeatherIcon")
    @JvmStatic
    fun setLoadWeatherIcon(imageView: ImageView, state: WeatherStateIcon?) {
        if (state == null) return
        val type = Method.getWeatherType(state.number)
        val now = LocalDateTime.parse(Method.getCurrentDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val sunriseTime = LocalDateTime.parse(state.sunRiseTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val sunsetTime = LocalDateTime.parse(state.sunSetTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val isDay = now.isAfter(sunriseTime) && now.isBefore(sunsetTime)
        try {
            when (type) {
                "isThunderstorm" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_thunderstorm, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_thunderstorm, imageLoader = imageView.context.imageLoader)
                }
                "isClear" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_clear, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_clear, imageLoader = imageView.context.imageLoader)
                }
                "isCloudyFog" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_cloudy_fog, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_cloudy_fog, imageLoader = imageView.context.imageLoader)
                }
                "isCloudy" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_cloudy, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_cloudy, imageLoader = imageView.context.imageLoader)
                }
                "isFog" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_fog, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_fog, imageLoader = imageView.context.imageLoader)
                }
                "isPartiallyClearWithRain" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_partially_clear_with_rain, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_partially_clear_with_rain, imageLoader = imageView.context.imageLoader)
                }
                "isSnowing" -> {
                    if (isDay)
                        imageView.load(R.drawable.day_snowing, imageLoader = imageView.context.imageLoader)
                    else
                        imageView.load(R.drawable.night_snowing, imageLoader = imageView.context.imageLoader)
                }

                else ->
                    imageView.load(R.drawable.baseline_image_search_24, imageLoader = imageView.context.imageLoader)
            }
        } catch (ignored: Exception) {
        }
    }

    @BindingAdapter("android:convertTemperature")
    @JvmStatic
    fun setConvertTemperature(circularSeekBar: CircularSeekBar, progress: String?) {
        if (progress == null) return
        val currentProgress = progress.toFloat()
        try {
            when {
                currentProgress > MAX_TEMPERATURE -> circularSeekBar.progress = 100f
                currentProgress < MIN_TEMPERATURE -> circularSeekBar.progress = 0.0f
                else -> circularSeekBar.progress = ((currentProgress - MIN_TEMPERATURE) / (MAX_TEMPERATURE - MIN_TEMPERATURE)) * 100
            }
        } catch (ignored: Exception) {
        }
    }

    @BindingAdapter("android:convertHumidity")
    @JvmStatic
    fun setConvertHumidity(circularSeekBar: CircularSeekBar, progress: String?) {
        if (progress == null) return
        val currentProgress = progress.toFloat()
        try {
            when {
                currentProgress > MAX_HUMIDITY -> circularSeekBar.progress = 100f
                currentProgress < MIN_HUMIDITY -> circularSeekBar.progress = 0.0f
                else -> circularSeekBar.progress = ((currentProgress - MIN_HUMIDITY) / (MAX_HUMIDITY - MIN_HUMIDITY)) * 100
            }
        } catch (ignored: Exception) {
        }
    }
}