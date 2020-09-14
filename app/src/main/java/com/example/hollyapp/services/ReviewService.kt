package com.example.hollyapp.services

import com.example.hollyapp.services.beans.DeviceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ReviewService {
    @GET("reviews/picks.json")
    fun reviewDevice(@Query("api-key") apiKey: String, @Query("offset") offset: Int) : Call<DeviceResponse>

    @GET("reviews/picks.json")
    fun reviewSearch(@Query("api-key") apiKey: String, @Query("query") queryTitle: String) : Call<DeviceResponse>
}