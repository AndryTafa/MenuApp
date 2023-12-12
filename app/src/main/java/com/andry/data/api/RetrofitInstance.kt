package com.andry.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    const val BASE_URL = "http://192.248.167.188/"

    private val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjAsIm5hbWUiOiJUZXN0QW5kcnkiLCJlbWFpbCI6ImFuZHJ5QGdtYWlsLmNvbSIsImlzTGljZW5zZUFjdGl2ZSI6dHJ1ZSwicm9sZSI6IkFkbWluIiwicmVzdGF1cmFudElkIjpudWxsLCJpYXQiOjE3MDE0NTM4NjV9.UtpmteGrZvSbTlKgW5pxZHM74AkuhpelkNO6thAT4ZU"


    private val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("x-auth-token", accessToken)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val customLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()

        println("--> ${request.method} ${request.url}")

        val response = chain.proceed(request)

        val responseBody = response.body
        val responseBodyString = responseBody?.string()

        println("<-- ${response.code} ${response.message} for call ${request.url.encodedPath} body: $responseBodyString")

        response.newBuilder()
            .body(responseBodyString?.toResponseBody(responseBody.contentType()))
            .build()
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(customLoggingInterceptor)
        .addInterceptor(headerInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}