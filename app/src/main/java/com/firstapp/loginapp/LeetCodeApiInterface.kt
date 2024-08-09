package com.firstapp.loginapp
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface LeetCodeApiInterface {
    @GET("{profileId}")
    fun getData(
        @Path("profileId") codingProfile: String
    ): Call<ResponseDataLeetcodeClass>

}