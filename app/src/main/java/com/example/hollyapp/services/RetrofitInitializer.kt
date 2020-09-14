package com.example.hollyapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit = Retrofit.Builder()
                            .baseUrl("https://api.nytimes.com/svc/movies/v2/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()


    fun reviewService () : ReviewService {
        return retrofit.create(ReviewService::class.java)
    }
}