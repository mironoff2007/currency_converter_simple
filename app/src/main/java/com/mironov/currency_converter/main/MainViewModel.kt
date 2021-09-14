package com.mironov.currency_converter.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironov.currency_converter.data.DataRemote
import com.mironov.currency_converter.data.DataShared
import com.mironov.currency_converter.data.RemoteStatus

class MainViewModel : ViewModel() {

    private val dataRemote: DataRemote by lazy { DataRemote(dataRemoteStatus) }
    private lateinit var dataShared: DataShared

    private val dataRemoteStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()
    val viewModelStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()

    private var currency: Float = 0f
    private lateinit var curToCur: String

    init {
        setupObserver()
    }

    fun initDataShared(context: Context) {
        this.dataShared = DataShared(context)
    }

    fun getCurrency(): Float {
        return currency
    }

    fun requestCurrency(curToCur: String, key: String) {
        this.curToCur = curToCur
        currency = dataShared.getCurrencyRate(curToCur)
        if (currency == 0f) {
            dataRemote.getCurrencyFromWeb(curToCur, key)
        } else {
            viewModelStatus.postValue(RemoteStatus.FROM_CACHE)
        }
    }

    private fun setupObserver() {
        dataRemote.dataRemoteStatus.observeForever { status ->
            when (status) {
                RemoteStatus.RESPONSE -> {
                    currency = dataRemote.getCurrency()
                    dataShared.saveCurrencyRate(currency, curToCur)
                    viewModelStatus.postValue(RemoteStatus.RESPONSE)
                    Log.d("My_tag", "cur=" + currency)
                }
            }
        }

    }
}


