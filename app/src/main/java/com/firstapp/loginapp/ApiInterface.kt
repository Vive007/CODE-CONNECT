package com.firstapp.loginapp


import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface ApiInterface {

    @GET("user.info")
    fun getData(
        @Query("handles") codingProfile: String
    ): Call<ResponseDataClass>
}