package com.mironov.currency_converter

import com.google.gson.annotations.SerializedName

class JsonObject {
    @SerializedName("USD_PHP")
    var currencyValue:CurrencyValue?=null


    @JvmName("getCurrencyValue1")
    fun getCurrencyValue(): CurrencyValue? {
        return currencyValue
    }


    fun set(value: CurrencyValue) {
        this.currencyValue = value
    }

}