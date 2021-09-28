package com.mironov.currency_converter.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironov.currency_converter.data.DataRemote
import com.mironov.currency_converter.data.DataShared
import com.mironov.currency_converter.data.RemoteStatus
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

    private val dataRemote: DataRemote by lazy { DataRemote(dataRemoteStatus) }
    private lateinit var dataShared: DataShared

    private val dataRemoteStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()
    val viewModelStatus: MutableLiveData<RemoteStatus> = MutableLiveData<RemoteStatus>()

    private var currencyRatio: Float = 0f
    private var curToCur: String? =null

    init {
        setupObserver()
    }

    fun convert(curFrom:Float):Float{
        return curFrom*currencyRatio
    }

    fun initDataShared(context: Context) {
        this.dataShared = DataShared(context)
    }

    fun getCurrencyRatio(): Float {
        return currencyRatio
    }
    fun getCurrenciesNames(): String? {
        return curToCur
    }

    fun requestCurrency(curToCur: String, key: String) {
        this.curToCur = curToCur
        dataRemote.getCurrencyFromWeb(curToCur, key)
    }

    private fun setupObserver() {
        dataRemote.dataRemoteStatus.observeForever { status ->
            when (status) {
                RemoteStatus.RESPONSE -> {
                    currencyRatio = dataRemote.getCurrency()
                    dataShared.saveCurrencyRate(currencyRatio, curToCur!!)
                    viewModelStatus.postValue(RemoteStatus.RESPONSE)
                }
                RemoteStatus.ERROR -> {
                    currencyRatio = dataShared.getCurrencyRate(curToCur!!)
                    if (currencyRatio == 0f) {
                        viewModelStatus.postValue(RemoteStatus.ERROR)
                    } else {
                        viewModelStatus.postValue(RemoteStatus.FROM_CACHE)
                    }

                }
            }
        }
    }

    fun formatFloatToString(numb:Float):String{
        if(numb>99.99){
           return numb.roundToInt().toString()
        }
        else{
            return "%.2f".format(numb)
        }
    }

     fun setDestroyStatus(){
        viewModelStatus.value=RemoteStatus.DESTROY
    }
}


