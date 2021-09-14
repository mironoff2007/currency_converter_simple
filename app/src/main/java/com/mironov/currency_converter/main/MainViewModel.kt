package com.mironov.currency_converter.main

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironov.currency_converter.data.DataRemote
import com.mironov.currency_converter.data.RemoteStatus

class MainViewModel : ViewModel() {

    private val dataRemote: DataRemote by lazy { DataRemote(dataRemoteStatus) }
    private val dataRemoteStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()
    val viewModelStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()
    private var currency: Float = 0f

    init {
        setupObserver()
    }

    fun getCurrency(): Float {
        return currency
    }
    fun requestCurrency(curToCur: String, key: String) {
        dataRemote.getCurrencyFromWeb(curToCur, key)
    }

    private fun setupObserver() {
        dataRemote.dataRemoteStatus.observeForever { status ->
            when (status) {
                RemoteStatus.RESPONSE -> {
                    currency = dataRemote.getCurrency()
                    viewModelStatus.postValue(RemoteStatus.RESPONSE)
                    Log.d("My_tag", "cur=" + currency)
                }
            }
        }

    }
}


