package com.side.project.air.network

import com.side.project.air.BuildConfig
import com.side.project.air.data.weatherApi.dayNight.DayNight
import com.side.project.air.data.weatherApi.weather.Weather
import com.side.project.air.utils.Method
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * 臺灣各縣市鄉鎮未來3天(72小時)逐3小時天氣預報
     */
    @Headers("Accept-Encoding: identity")
    @GET("{city}")
    suspend fun getWeather(
        @Path("city") city: String,
        @Query("Authorization") authorization: String = BuildConfig.API_KEY,
        @Query("format") format: String = "JSON",
        @Query("locationName") locationName: String,
        @Query("elementName") elementName: String = "Wx,T,RH,PoP6h,PoP12h",
        @Query("sort") sort: String = "time",
    ): Response<Weather>

    /**
     * 全臺各縣市每天的日出、日沒及太陽過中天等時刻資料-含有日出日沒時之方位及過中天時之仰角資料
     */
    @Headers("Accept-Encoding: identity")
    @GET("A-B0062-001")
    suspend fun getDayNight(
        @Query("Authorization") authorization: String = BuildConfig.API_KEY,
        @Query("format") format: String = "JSON",
        @Query("CountyName") countyName: String,
        @Query("Date") date: String = Method.getCurrentDate(),
        @Query("parameter") parameter: String = "SunRiseTime,SunSetTime",
    ): Response<DayNight>
}