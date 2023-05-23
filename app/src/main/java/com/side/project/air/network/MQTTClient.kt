package com.side.project.air.network

import com.side.project.air.BuildConfig
import com.side.project.air.utils.Method
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MQTTClient : KoinComponent {
    companion object {
        private const val TAG = "MQTTClient"
    }

    private val mqttClient: MqttAndroidClient by inject()
    private var onCallback: OnMQTTMessageArrivedListener? = null

    /**
     * 連線 MQTT Server
     */
    fun connect(work: () -> Unit = {}) {
        val options = MqttConnectOptions()
        options.userName = BuildConfig.MQTT_USERNAME
        options.password = BuildConfig.MQTT_PASSWORD.toCharArray()

        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Method.logE(TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Method.logE(TAG, "Receive message: ${message.toString()} from topic: $topic")
                onCallback?.onMessageArrived(topic.toString(), message.toString())
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Method.logE(TAG, "Connection success")
                    work()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Method.logE(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Method.logE(TAG, "Error: ${e.message}")
        }
    }

    /**
     * 訂閱 MQTT Topic
     */
    fun subscribe(topic: String, qos: Int = 2) {
        try {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Method.logE(TAG, "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Method.logE(TAG, "Failed to subscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    /**
     * 取消訂閱 MQTT Topic
     */
    fun unsubscribe(topic: String) {
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Method.logE(TAG, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Method.logE(TAG, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Method.logE(TAG, "Error: ${e.message}")
        }
    }

    /**
     * 發布到 MQTT Topic
     */
    fun publish(topic: String, msg: String, qos: Int = 2, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Method.logE(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Method.logE(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Method.logE(TAG, "Error: ${e.message}")
        }
    }

    /**
     * 斷線 MQTT Server
     */
    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Method.logE(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Method.logE(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Method.logE(TAG, "Error: ${e.message}")
        }
    }

    /**
     * 設定 MQTT 訊息接收監聽
     */
    fun setListener(listener: OnMQTTMessageArrivedListener) {
        onCallback = listener
    }

    /**
     * MQTT 訊息接收監聽
     */
    interface OnMQTTMessageArrivedListener {
        fun onMessageArrived(topic: String, message: String)
    }
}