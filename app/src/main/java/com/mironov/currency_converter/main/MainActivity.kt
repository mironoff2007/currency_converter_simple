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
                convertButton.setEnabled(false)
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length == 0) {
                    convertButton.setEnabled(false)
                }
                else{
                    convertButton.setEnabled(true)
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
        convertButton.setEnabled(false)
    }

    private fun initSpinerAdapters() {
        //Spinner From
        ArrayAdapter.createFromResource(
            this,
            R.array.currency_variants,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
        }
        spinnerFrom.onItemSelectedListener = this

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
        if (parent.getId() == R.id.spinnerFrom) {
            curFrom = parent.getItemAtPosition(pos).toString()
        } else {
            curTo = parent.getItemAtPosition(pos).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        curFrom = "USD"
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
                    var curFromNumb = (currencyFrom.getText().toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.setText(viewModel.formatFloatToString(viewModel.convert(curFromNumb)))
                }
                RemoteStatus.FROM_CACHE -> {
                    var curFromNumb = (currencyFrom.getText().toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.setText(viewModel.formatFloatToString(viewModel.convert(curFromNumb)))
                    Toast.makeText(applicationContext, R.string.from_cache, Toast.LENGTH_SHORT).show()
                }
                RemoteStatus.ERROR -> {
                    Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

}
