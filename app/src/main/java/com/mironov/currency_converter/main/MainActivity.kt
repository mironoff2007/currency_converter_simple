package com.mironov.currency_converter.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.mironov.currency_converter.R
import com.mironov.currency_converter.data.RemoteStatus

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    lateinit var convertButton: Button
    lateinit var currencyText: TextView

    var currencyRate: Float = 0f




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertButton = findViewById(R.id.convert_button)
        currencyText = findViewById(R.id.currency_text)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupListeners()
        setupObserver()
    }

    private fun setupListeners() {
        convertButton.setOnClickListener { v: View? ->
            viewModel.requestCurrency("USD_RUB", resources.getString(R.string.api_key))
        }

    }

    private fun setupObserver() {
        viewModel.viewModelStatus.observe(this) { status ->
            when (status) {
                RemoteStatus.RESPONSE -> {
                    currencyRate = viewModel.getCurrency()
                    currencyText.setText(currencyRate.toString())
                }
            }
        }

    }

}
