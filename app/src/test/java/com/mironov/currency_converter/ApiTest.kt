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

        System.out.println("Start")
       var  cl:Call<Map<String, CurrencyValue>?>?

       cl=NetworkService.getJSONApi().getCurrencyMap("USD_PHP","ultra","ae286aaf3f808d07c695")
            cl!!.enqueue(object : Callback<Map<String, CurrencyValue>?> {

                override fun onResponse(
                    call: Call<Map<String, CurrencyValue>?>?,
                    response: Response<Map<String, CurrencyValue>?>?
                ) {
                    System.out.println( call?.request()?.url().toString())
                }

                override fun onFailure(call: Call<Map<String, CurrencyValue>?>, t: Throwable) {
                    System.out.println(call?.request()?.url().toString())
                }
            })
        //gson = Gson();
        //val json: String = "{\"USD_PHP\":{\"val\":49.876016}}"
        //var cr: JsonObject = gson.fromJson(json, object : TypeToken<JsonObject>() {}.type)

        //assertEquals(49.876016f, cr.getCurrencyValue()?.getValue())
    }

}