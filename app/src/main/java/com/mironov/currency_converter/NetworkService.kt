package com.mironov.currency_converter

import mironov.random_gif.CurrencyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    //apiKey=do-not-use-this-api-key-RYzUQylft12X1ohCpEK4c&q=USD_PHP&compact=y
    private val BASE_URL = "https://free.currconv.com/api/v7/"
    private lateinit var mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getJSONApi():CurrencyApi  {
        return mRetrofit.create<CurrencyApi>(CurrencyApi::class.java)
    }

}


