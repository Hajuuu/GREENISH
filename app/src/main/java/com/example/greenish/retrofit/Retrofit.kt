package com.example.greenish.retrofit

import com.example.greenish.retrofit2.InfoService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object Retrofit {

    private const val BASE_URL = "http://api.nongsaro.go.kr/"

    fun getRetrofit() : Service {

        val parser = TikXml.Builder()
            .exceptionOnUnreadXml(false).build()

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .build().create(Service::class.java)


    }

    fun getRetrofit2() : InfoService {

        val parser = TikXml.Builder()
            .exceptionOnUnreadXml(false).build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .build().create(InfoService::class.java)


    }
}