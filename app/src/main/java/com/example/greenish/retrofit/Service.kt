package com.example.greenish.retrofit

import com.example.greenish.retrofit.PlantResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("service/garden/gardenList")
    fun plantResult(
        @Query("apiKey" ,encoded = true) apiKey: String,
        @Query("numOfRows", encoded = true) numOfRows: String

    ): Call<PlantResult>
}