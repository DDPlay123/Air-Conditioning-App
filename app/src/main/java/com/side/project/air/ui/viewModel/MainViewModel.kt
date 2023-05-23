package com.side.project.air.ui.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.side.project.air.data.WeatherStateIcon
import com.side.project.air.data.repo.MainRepo
import com.side.project.air.data.weatherApi.dayNight.DayNight
import com.side.project.air.data.weatherApi.weather.Weather
import com.side.project.air.utils.Contracts
import com.side.project.air.network.MQTTClient
import com.side.project.air.utils.Method
import com.side.project.air.utils.Resource
import com.side.project.air.utils.floatFormat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val fusedLocationClient: FusedLocationProviderClient by inject()
    private val mainRepo: MainRepo by inject()
    private val mqttClient: MQTTClient by inject()
    private val context: Context
        get() = getApplication<Application>().applicationContext

    /**
     * 是否為設定模式
     */
    private val _isSettingMode = MutableLiveData<Boolean>()
    val isSettingMode: LiveData<Boolean>
        get() = _isSettingMode

    /**
     * 是否正在載入資料
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * 縣市名稱
     */
    private val _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city

    /**
     * 地區名稱
     */
    private val _locationName = MutableLiveData<String>()
    val locationName: LiveData<String>
        get() = _locationName

    /**
     * 天氣資料
     */
    private val _weatherData = MutableLiveData<Resource<Weather>>()
    val weatherData: LiveData<Resource<Weather>>
        get() = _weatherData

    /**
     * 天氣溫度
     */
    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String>
        get() = _temperature

    /**
     * 天氣濕度
     */
    private val _humidity = MutableLiveData<String>()
    val humidity: LiveData<String>
        get() = _humidity

    /**
     * 降雨機率
     */
    private val _rain = MutableLiveData<String>()
    val rain: LiveData<String>
        get() = _rain

    /**
     * 天氣狀態(number, description)
     */
    private val _weatherState = MutableLiveData<Pair<String, String>>()
    val weatherState: LiveData<Pair<String, String>>
        get() = _weatherState

    /**
     * 日出日落時間
     */
    private val _dayNightData = MutableLiveData<Resource<DayNight>>()
    val dayNightData: LiveData<Resource<DayNight>>
        get() = _dayNightData

    /**
     * 天氣狀態給圖片用
     */
    private val _weatherStateIcon = MutableLiveData<WeatherStateIcon>()
    val weatherStateIcon: LiveData<WeatherStateIcon>
        get() = _weatherStateIcon

    /**
     * 感測溫度
     */
    private val _deviceTemperature = MutableLiveData<String>()
    val deviceTemperature: LiveData<String>
        get() = _deviceTemperature

    /**
     * 感測濕度
     */
    private val _deviceHumidity = MutableLiveData<String>()
    val deviceHumidity: LiveData<String>
        get() = _deviceHumidity

    /**
     * 是否開啟自動模式
     */
    private val _isOpenAutoMode = MutableLiveData<Boolean>()
    val isOpenAutoMode: LiveData<Boolean>
        get() = _isOpenAutoMode

    /**
     * 溫度閥值
     */
    private val _autoModeTemperature = MutableLiveData<String>()
    val autoModeTemperature: LiveData<String>
        get() = _autoModeTemperature

    /**
     * 切換設定模式
     */
    fun setSettingMode() {
        viewModelScope.launch {
            _isSettingMode.value = !(_isSettingMode.value ?: false)
            if (isSettingMode.value == true) {
                subscribeMQTT(Contracts.AUTO_MODE_TOPIC)
                subscribeMQTT(Contracts.SET_TEMPERATURE_TOPIC)
            }
        }
    }

    /**
     * 取得當前位置
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val geocoder = Geocoder(context, Locale.TAIWAN)
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    Method.logE(TAG, "Current Location：$addresses")
                    addresses?.let {
                        _city.value = addresses[0].adminArea ?: addresses[0].subAdminArea
                        _locationName.value = addresses[0].locality ?: addresses[0].subLocality
                        _isLoading.value = false
                        callWeatherAPI()
                    }
                }
            }.addOnFailureListener {
                _city.value = "NaN"
                _locationName.value = ""
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * 取得天氣資料
     */
    private fun callWeatherAPI() {
        viewModelScope.launch {
            mainRepo.getWeather(city.value.toString(), locationName.value.toString())
                .onStart { _weatherData.value = Resource.Loading() }
                .catch { exception -> _weatherData.value = Resource.Error(exception.message ?: "Unknown Error") }
                .collect { weatherResource ->
                    _weatherData.value = weatherResource
                    weatherData.value?.data?.let { weather ->
                        weather.records.locations[0].location[0].weatherElement.forEach { data ->
                            when (data.elementName) {
                                // 天氣現象
                                "Wx" -> _weatherState.value = Pair(data.time.last().elementValue[1].value, data.time.last().elementValue[0].value)
                                // 溫度
                                "T" -> _temperature.value = data.time.last().elementValue[0].value
                                // 相對濕度
                                "RH" -> _humidity.value = data.time.last().elementValue[0].value
                                // 降雨機率 12h
                                "PoP12h" ->
                                    _rain.value = if (data.time.isNotEmpty()) data.time.last().elementValue[0].value else "0"
                                // 降雨機率 6h
                                "PoP6h" ->
                                    _rain.value = if (data.time.isNotEmpty()) data.time.last().elementValue[0].value else "0"
                                // 其他資料
                                else -> Unit
                            }
                        }.run { callDayNightAPI() }
                    }
                }
        }
    }

    /**
     * 取得日出日落時間
     */
    private fun callDayNightAPI() {
        viewModelScope.launch {
            mainRepo.getDayNight(city.value.toString())
                .onStart { _dayNightData.value = Resource.Loading() }
                .catch { exception -> _dayNightData.value = Resource.Error(exception.message ?: "Unknown Error") }
                .collect { dayNightResource ->
                    _dayNightData.value = dayNightResource
                    dayNightData.value?.data?.let { dayNight ->
                        dayNight.records.locations.location[0].time.last().apply {
                            _weatherStateIcon.value = WeatherStateIcon(
                                number = weatherState.value?.first?.toInt() ?: return@apply,
                                sunRiseTime = "${Date}T${SunRiseTime}",
                                sunSetTime = "${Date}T${SunSetTime}"
                            )
                        }
                    }
                }
        }
    }

    /**
     * 連線 MQTT Server
     */
    fun connectMQTT() {
        mqttClient.connect() {
            subscribeMQTT(Contracts.TEMPERATURE_TOPIC)
            subscribeMQTT(Contracts.HUMIDITY_TOPIC)
            mqttClient.setListener(object : MQTTClient.OnMQTTMessageArrivedListener {
                override fun onMessageArrived(topic: String, message: String) {
                    when (topic) {
                        Contracts.TEMPERATURE_TOPIC ->
                            _deviceTemperature.value = message.floatFormat()

                        Contracts.HUMIDITY_TOPIC ->
                            _deviceHumidity.value = message.floatFormat()

                        Contracts.AUTO_MODE_TOPIC ->
                            _isOpenAutoMode.value = message == "1"

                        Contracts.SET_TEMPERATURE_TOPIC ->
                            _autoModeTemperature.value = message

                        else -> Unit
                    }
                }
            })
        }
    }

    /**
     * 斷線 MQTT Server
     */
    fun disconnectMQTT() =
        mqttClient.disconnect()

    /**
     * 訂閱 MQTT Topic
     */
    private fun subscribeMQTT(topic: String) =
        mqttClient.subscribe(topic)

    /**
     * 取消訂閱 MQTT Topic
     */
    private fun unsubscribeMQTT(topic: String) =
        mqttClient.unsubscribe(topic)

    /**
     * 發布到 MQTT Topic
     */
    fun publishMQTT(topic: String, message: String, retained: Boolean = false) =
        mqttClient.publish(topic, message, retained = retained)

    fun openAirConditioner() =
        publishMQTT(Contracts.ACTIVATE_TOPIC, "1")

    fun closeAirConditioner() =
        publishMQTT(Contracts.ACTIVATE_TOPIC, "0")

    fun toggleAutoMode() =
        publishMQTT(Contracts.AUTO_MODE_TOPIC, if (isOpenAutoMode.value == true) "0" else "1", true)
}