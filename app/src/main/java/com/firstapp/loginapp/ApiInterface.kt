package com.firstapp.loginapp


import retrofit2.http.GET

interface ApiInterface {
    @GET("user.info?handles=DmitriyH")
    fun getData():retrofit2.Call<ResponseDataClass>
}