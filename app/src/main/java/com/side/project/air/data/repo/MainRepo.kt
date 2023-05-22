package com.side.project.air.data.repo

import com.side.project.air.data.weatherApi.dayNight.DayNight
import com.side.project.air.data.weatherApi.weather.Weather
import com.side.project.air.network.ApiClient
import com.side.project.air.utils.Method
import com.side.project.air.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

interface MainRepo {
    fun getWeather(city: String, locationName: String): Flow<Resource<Weather>>
    fun getDayNight(city: String): Flow<Resource<DayNight>>
}

class MainRepoImpl : KoinComponent, MainRepo {
    companion object {
        private const val TAG = "MainRepoImpl"
    }

    override fun getWeather(city: String, locationName: String): Flow<Resource<Weather>> = flow {
        // Loading
        emit(Resource.Loading())
        // 判斷縣市是否存在
        val cityCode: String = Method.getCodeByAreaName(city.replace("台", "臺"))
        if (cityCode.isEmpty()) {
            Method.logE(TAG, "cityCode is empty")
            emit(Resource.Error("Unknown Error"))
            return@flow
        }
        // 取得天氣資料
        try {
            val response = ApiClient.weatherServer.getWeather(city = cityCode, locationName = locationName)
            if (response.isSuccessful) {
                Method.logE(TAG, "response is successful")
                val weather = response.body()
                if (weather != null)
                    emit(Resource.Success(weather))
                else
                    emit(Resource.Error("Unknown Error"))
            } else {
                Method.logE(TAG, "response is not successful")
                emit(Resource.Error("Unknown Error"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Method.logE(TAG, "Exception: ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    override fun getDayNight(city: String): Flow<Resource<DayNight>> = flow {
        // Loading
        emit(Resource.Loading())
        // 調整縣市是否名稱
        val countyName = city.replace("台", "臺")

        // 取得日出日落時間
        try {
            val response = ApiClient.weatherServer.getDayNight(countyName = countyName)
            if (response.isSuccessful) {
                Method.logE(TAG, "response is successful")
                val dayNight = response.body()
                if (dayNight != null)
                    emit(Resource.Success(dayNight))
                else
                    emit(Resource.Error("Unknown Error"))
            } else {
                Method.logE(TAG, "response is not successful")
                emit(Resource.Error("Unknown Error"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Method.logE(TAG, "Exception: ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }
}