package mironov.random_gif

import com.mironov.currency_converter.CurrencyValue
import retrofit2.http.GET
import retrofit2.Call;


interface CurrencyApi{
    @GET("random?json=true")//-TODO-
    fun getCurrencyMap(): Call<Map<String,CurrencyValue>?>?
}
