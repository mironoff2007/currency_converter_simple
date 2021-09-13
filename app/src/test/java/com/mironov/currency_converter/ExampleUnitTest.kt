package com.mironov.currency_converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    lateinit var gson:Gson;
    @Test
    fun  JsonInnerClass() {
        gson = Gson();
        val json:String="{\"USD_PHP\":{\"val\":49.876016}}"
       var cr:JsonObject=gson.fromJson(json, object : TypeToken<JsonObject>() {}.type)

        assertEquals(49.876016f, cr.getCurrencyValue()?.getValue())
    }

    @Test
    fun  JsonMap() {
        gson = Gson();
        val json:String="{\"USD_PHP\":{\"val\":49.876016}}"
        var map= emptyMap<String, CurrencyValue>()
        map=gson.fromJson(json, object : TypeToken<Map<String, CurrencyValue>>() {}.type)

        assertEquals(49.876016f, map.get("USD_PHP")?.getValue())
    }

}
