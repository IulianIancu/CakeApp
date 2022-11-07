package com.iulian.iancu.cakeapp.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//TODO this class should live in its own data module, for clean architecture
interface CakeService {
    @GET("waracle_cake-android-client")
    suspend fun getCakeList(): Response<List<Cake>>

    companion object {
        var retrofitService: CakeService? = null
        fun getInstance(): CakeService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://gist.githubusercontent.com/t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(CakeService::class.java)
            }
            return retrofitService!!
        }
    }
}