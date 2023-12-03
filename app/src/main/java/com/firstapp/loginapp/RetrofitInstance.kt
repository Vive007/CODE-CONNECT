package com.firstapp.loginapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private  val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://codeforces.com/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    // connect to interface api
    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }

}