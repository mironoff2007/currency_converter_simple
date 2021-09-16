package com.mironov.currency_converter.main

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mironov.currency_converter.R
import com.mironov.currency_converter.data.RemoteStatus
import android.text.Editable
import android.text.Layout

import android.text.TextWatcher
import android.util.Log
import com.mironov.currency_converter.CustomAdapter
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.get
import androidx.core.view.isVisible


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    lateinit var convertButton: Button
    lateinit var currencyText: TextView
    lateinit var currencyFrom: EditText
    lateinit var spinnerFrom: Spinner
    lateinit var spinnerTo: Spinner
    lateinit var progressBar: ProgressBar


    var currencyRate: Float = 0f
    lateinit var curTo: String
    lateinit var curFrom: String
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initDataShared(applicationContext)

        initViews()
        addEditTextListener()
        setupButtonsListeners()
        setupObserver()
        initSpinerAdapters()

    }

    private fun addEditTextListener() {
        currencyFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                convertButton.isEnabled = false
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length == 0) {
                    convertButton.isEnabled = false
                }
                else{
                    convertButton.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun initViews() {
        convertButton = findViewById(R.id.convert_button)
        currencyText = findViewById(R.id.currency_to)
        currencyFrom = findViewById(R.id.currency_from)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        progressBar = findViewById(R.id.progressBar)
        convertButton.isEnabled = false
    }

    private fun initSpinerAdapters() {
        var spinnerImages = intArrayOf(
            R.drawable.ic_usa,
            R.drawable.ic_rus,
            R.drawable.ic_eur,
            R.drawable.ic_php
        )
        val string_array: Array<String> = resources.getStringArray(R.array.currency_variants)
        val mCustomAdapter =
            CustomAdapter(this@MainActivity, string_array, spinnerImages)
        //Spinner From
        spinnerFrom.adapter = mCustomAdapter
        spinnerFrom.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                    curFrom = string_array[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curFrom="USD"
            }
        })

        //spinnerFrom.onItemSelectedListener = this

        //Spinner To
        spinnerTo.adapter = mCustomAdapter
        spinnerTo.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                curTo = string_array[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curTo = "USD"
            }
        })
    }

    private fun setupButtonsListeners() {
        convertButton.setOnClickListener { v: View? ->
            viewModel.requestCurrency(curFrom + "_" + curTo, resources.getString(R.string.api_key))
        }

    }

    private fun setupObserver() {
        viewModel.viewModelStatus.observe(this) { status ->
            when (status) {
                RemoteStatus.RESPONSE -> {
                    progressBar.visibility = View.INVISIBLE
                    var curFromNumb = (currencyFrom.text.toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.text =
                        viewModel.formatFloatToString(viewModel.convert(curFromNumb))
                }
                RemoteStatus.FROM_CACHE -> {
                    progressBar.visibility = View.INVISIBLE
                    var curFromNumb = (currencyFrom.text.toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.text =
                        viewModel.formatFloatToString(viewModel.convert(curFromNumb))
                    Toast.makeText(applicationContext, R.string.from_cache, Toast.LENGTH_SHORT)
                        .show()
                }
                RemoteStatus.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
                }
                RemoteStatus.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }

    }
}
