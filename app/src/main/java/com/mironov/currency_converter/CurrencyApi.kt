package com.mironov.currency_converter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {

    //https://free.currconv.com/api/v7/convert?q=USD_PHP&compact=ultra&apiKey=ae286aaf3f808d07c695
    @GET("convert")
    fun getCurrencyMap(
        @Query("q") curToCur: String,
        @Query("compact") compact: String,
        @Query("apiKey") key: String
    ): Call<Map<String, CurrencyValue>?>?
}
