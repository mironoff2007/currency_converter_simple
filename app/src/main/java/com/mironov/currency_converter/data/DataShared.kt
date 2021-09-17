package com.mironov.currency_converter.data

import android.content.Context
import android.content.SharedPreferences

class DataShared(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

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