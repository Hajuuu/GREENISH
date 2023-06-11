package com.example.greenish.retrofit2

import com.example.greenish.retrofit.PlantResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InfoService {
    @GET("service/garden/gardenDtl")
    fun plantInfo(
        @Query("apiKey" ,encoded = true) apiKey: String,
        @Query("cntntsNo", encoded = true) cntntsNo: String
    ): Call<PlantInfo>
}