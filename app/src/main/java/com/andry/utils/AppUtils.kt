package com.andry.utils

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.math.RoundingMode
import java.net.SocketTimeoutException
import java.text.DecimalFormat

object AppUtils {
    suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Response<T> {
        return try {
            call()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Response.error(408, "Request Timeout".toResponseBody())
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, "Internal Server Error".toResponseBody())
        }
    }


    fun Double.formatTo2DecimalPlaces(): String {
        val df = DecimalFormat("#.00")
        df.roundingMode = RoundingMode.HALF_EVEN
        if (this == 0.0) {
            return "0.00"
        }
        return df.format(this)
    }
}
