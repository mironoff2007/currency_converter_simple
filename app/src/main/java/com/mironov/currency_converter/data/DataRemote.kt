package com.mironov.currency_converter.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mironov.currency_converter.CurrencyValue
import com.mironov.currency_converter.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRemote(var dataRemoteStatus: MutableLiveData<RemoteStatus>) {

    private var currency = 0f

    fun getCurrencyFromWeb(curToCur: String, key: String) {
        NetworkService
            .getJSONApi()
            .getCurrencyMap(curToCur, "y", key)
            ?.enqueue(object : Callback<Map<String, CurrencyValue>?> {
                override fun onResponse(
                    call: Call<Map<String, CurrencyValue>?>,
                    response: Response<Map<String, CurrencyValue>?>
                ) {
                    currency = response.body()?.get(curToCur)?.getValue() ?: 0f
                    dataRemoteStatus.postValue(RemoteStatus.RESPONSE)
                }

                override fun onFailure(call: Call<Map<String, CurrencyValue>?>, t: Throwable) {
                    dataRemoteStatus.postValue(RemoteStatus.ERROR)
                }
            })
    }

    fun getCurrency():Float{
        return currency
    }
}
