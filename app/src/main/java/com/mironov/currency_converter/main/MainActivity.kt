package com.mironov.currency_converter.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.mironov.currency_converter.R
import com.mironov.currency_converter.data.RemoteStatus

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    lateinit var convertButton: Button
    lateinit var currencyText: TextView
    lateinit var spinnerFrom: Spinner

    var currencyRate: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertButton = findViewById(R.id.convert_button)
        currencyText = findViewById(R.id.currency_text)
        spinnerFrom = findViewById(R.id.spinnerFrom)


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initDataShared(applicationContext)
        setupListeners()
        setupObserver()

        ArrayAdapter.createFromResource(
            this,
            R.array.currency_variants,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
        }
        spinnerFrom.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.d("My_tag", parent.getItemAtPosition(pos).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
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
                RemoteStatus.FROM_CACHE -> {
                    currencyRate = viewModel.getCurrency()
                    currencyText.setText(currencyRate.toString() + "From cache")
                }

            }
        }

    }

}
