package com.mironov.currency_converter.data

import android.content.Context
import android.content.SharedPreferences


class DataShared {

    val pref: SharedPreferences
    val editor: SharedPreferences.Editor

    constructor(context: Context) {
        pref = context.getSharedPreferences("Cache", Context.MODE_PRIVATE)
        editor = pref.edit();
    }


    fun saveCurrencyRate(curToCur: Float, name: String) {
        editor.putFloat(name, curToCur).apply()
    }

    fun getCurrencyRate(name: String): Float {
        return pref.getFloat(name, 0f)
    }

    fun clearPrefs() {
        editor.clear().commit()
    }

}