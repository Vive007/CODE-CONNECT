package com.firstapp.loginapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitLeetcode {
    private  val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://leetcode-api-faisalshohag.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    // connect to interface api
    val leetCodeApiInterface by lazy {
        retrofit.create(LeetCodeApiInterface::class.java)
    }
}