package com.mironov.currency_converter.main

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mironov.currency_converter.R
import com.mironov.currency_converter.data.RemoteStatus
import android.text.Editable

import android.text.TextWatcher
import com.mironov.currency_converter.CustomAdapter
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

import android.widget.Toast





class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    lateinit var convertButton: Button
    lateinit var currencyText: TextView
    lateinit var currencyFrom: EditText
    lateinit var spinnerFrom: Spinner
    lateinit var spinnerTo: Spinner


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
        /*
        ArrayAdapter.createFromResource(
            this,
            R.array.currency_variants,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
        }*/
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
        ArrayAdapter.createFromResource(
            this,
            R.array.currency_variants,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTo.adapter = adapter
        }
        spinnerTo.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if (parent.id == R.id.spinnerFrom) {
           curFrom = parent.getItemAtPosition(pos).toString()
        } else {
           curTo = parent.getItemAtPosition(pos).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        curTo = "USD"
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
                    var curFromNumb = (currencyFrom.text.toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.text = viewModel.formatFloatToString(viewModel.convert(curFromNumb))
                }
                RemoteStatus.FROM_CACHE -> {
                    var curFromNumb = (currencyFrom.text.toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.text = viewModel.formatFloatToString(viewModel.convert(curFromNumb))
                    Toast.makeText(applicationContext, R.string.from_cache, Toast.LENGTH_SHORT).show()
                }
                RemoteStatus.ERROR -> {
                    Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}
