package com.mironov.currency_converter.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mironov.currency_converter.CustomAdapter
import com.mironov.currency_converter.R
import com.mironov.currency_converter.data.RemoteStatus
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    lateinit var convertButton: Button
    private lateinit var currencyText: TextView
    private lateinit var currencyFrom: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var progressBar: ProgressBar


    private var currencyRate: Float = 0f
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
        initSpinnerAdapters()

    }

    private fun addEditTextListener() {
        currencyFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                convertButton.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                convertButton.isEnabled = s.toString().isNotEmpty()
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

    private fun initSpinnerAdapters() {
        val spinnerImages = intArrayOf(
            R.drawable.ic_usa,
            R.drawable.ic_rus,
            R.drawable.ic_eur,
            R.drawable.ic_php
        )


        val stringArray = resources.getStringArray(R.array.currency_variants)
        val mCustomAdapter =
            CustomAdapter(this@MainActivity, stringArray, spinnerImages)
        //Spinner From
        spinnerFrom.adapter = mCustomAdapter
        spinnerFrom.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                curFrom = stringArray[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curFrom = "USD"
            }
        }

        //Spinner To
        spinnerTo.adapter = mCustomAdapter
        spinnerTo.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                curTo = stringArray[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curTo = "USD"
            }
        }
    }

    private fun setupButtonsListeners() {
        convertButton.setOnClickListener { v: View? ->
            progressBar.visibility = View.VISIBLE
            viewModel.requestCurrency(curFrom + "_" + curTo, resources.getString(R.string.api_key))
        }

    }

    private fun setupObserver() {
        viewModel.viewModelStatus.observe(this) {
            when (it) {
                RemoteStatus.RESPONSE -> {
                    progressBar.visibility = View.INVISIBLE
                    val curFromNumb = (currencyFrom.text.toString()).toFloat()
                    currencyRate = viewModel.getCurrencyRatio()
                    currencyText.text =
                        viewModel.formatFloatToString(viewModel.convert(curFromNumb))
                }
                RemoteStatus.FROM_CACHE -> {
                    progressBar.visibility = View.INVISIBLE
                    val curFromNumb = (currencyFrom.text.toString()).toFloat()
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
                RemoteStatus.DESTROY -> {

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setDestroyStatus()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        currencyText.text = savedInstanceState?.getString("CALCULATED_CUR")
        spinnerFrom.setSelection(savedInstanceState?.getInt("SPINNER_CUR_FROM"))
        spinnerTo.setSelection(savedInstanceState?.getInt("SPINNER_CUR_TO"))
        currencyFrom.setText(savedInstanceState?.getString("VAL_CUR_FROM"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("CALCULATED_CUR", currencyText.text.toString())
            putInt("SPINNER_CUR_FROM", spinnerFrom.selectedItemPosition)
            putInt("SPINNER_CUR_TO", spinnerTo.selectedItemPosition)
            putString("VAL_CUR_FROM", currencyFrom.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
