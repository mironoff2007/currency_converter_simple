package com.mironov.currency_converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mironov.currency_converter.NetworkService.getJSONApi
import org.junit.Assert
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class ApiTest {

    @Test
    fun getRequestTest() {

        val  call:Call<Map<String, CurrencyValue>?>? = NetworkService.getJSONApi().getCurrencyMap("USD_PHP","y","ae286aaf3f808d07c695")
        val responce: Response<Map<String, CurrencyValue>?> =call!!.execute()
        var map:Map<String, CurrencyValue>?=responce.body()
        var currVal=responce.body()?.get("USD_PHP")?.getValue()

        System.out.println("Currency="+currVal)

        assertEquals(true, 1< currVal!!)
    }

}