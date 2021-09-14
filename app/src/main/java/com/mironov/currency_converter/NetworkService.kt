package com.mironov.currency_converter

import mironov.random_gif.CurrencyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {


    private const val BASE_URL = "https://free.currconv.com/api/v7/"
    private var mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getJSONApi():CurrencyApi  {
        return mRetrofit.create<CurrencyApi>(CurrencyApi::class.java)
    }

}


