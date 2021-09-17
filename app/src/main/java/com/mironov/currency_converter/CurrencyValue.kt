package com.mironov.currency_converter

import com.google.gson.annotations.SerializedName

class CurrencyValue {
    @SerializedName("val")
    private var value: Float = 0F

    fun getValue(): Float {
        return value
    }

    fun setValue(value: Float) {
        this.value = value
    }

    override fun toString(): String {
        return "CurrencyValue(value=$value)"
    }
}