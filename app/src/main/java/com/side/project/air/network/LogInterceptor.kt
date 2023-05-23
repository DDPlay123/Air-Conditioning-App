package com.side.project.air.network

import com.side.project.air.utils.Method
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * 用於攔截API訊息
 */
class LogInterceptor: Interceptor {
    private val tag = "Interceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        val utf8: Charset = Charset.forName("UTF-8")

        // Request
        val request: Request = chain.request()
        val requestBody: RequestBody? = request.body
        var reqBody: String? = null
        requestBody?.let {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            var charset: Charset = utf8
            val contentType: MediaType? = requestBody.contentType()
            contentType?.let {
                try {
                    charset = it.charset(utf8)!!
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }

            reqBody = buffer.readString(charset)
        }
        Method.logE(tag, "\nRequest:\nmethod:${request.method}\nURL:${request.url}\nheaders:${request.headers}\nbody:${reqBody}")

        // Response
        val response: Response = chain.proceed(request)
        val responseBody: ResponseBody = response.body
        val respBody: String?
        val source: BufferedSource = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer

        var charset: Charset = utf8
        val contentType: MediaType? = responseBody.contentType()
        contentType?.let {
            try {
                charset = it.charset(utf8)!!
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            }
        }
        respBody = buffer.clone().readString(charset)
        Method.logE(tag, "\nResponse:\ncode:${response.code}\nURL:${response.request.url}\nbody:${respBody}")

        return response
    }
}