package com.example.szymon_auguscik_czw_8_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyPredictorActivity : AppCompatActivity() {

    private lateinit var modeSpinner: Spinner
    private lateinit var modeFirstOption: String
    private lateinit var modeSecondOption: String
    private lateinit var currencySpinner: Spinner
    private lateinit var editText: EditText
    private lateinit var exchangeButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var currencyData: Array<CurrencyDetails>
    private lateinit var codesArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_predictor)
        DataHolder.prepare(applicationContext)

        modeSpinner = findViewById(R.id.modeSpinner)
        modeFirstOption = "From other to PLN"
        modeSecondOption = "From PLN to other"
        currencySpinner = findViewById(R.id.currencySpinner)
        editText = findViewById(R.id.editText)
        exchangeButton = findViewById(R.id.exchangeButton)
        resultTextView = findViewById(R.id.resultTextView)
        currencyData = emptyArray()

        getData()

        exchangeButton.setOnClickListener{calculateExchange()}
    }

    private fun getData() {
        val queue = DataHolder.queue
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"
        val currenciesRatesRequestA = JsonArrayRequest(
            Request.Method.GET, urlA, null,
            Response.Listener {
                    response -> println("Success!")
                val tmpData = currencyData + DataHolder.loadData(response)
                tmpData.sort()
                currencyData = tmpData
                showData()
            },
            Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })

        queue.add(currenciesRatesRequestA)

        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B/last/2/?format=json"
        val currenciesRatesRequestB = JsonArrayRequest(
            Request.Method.GET, urlB, null,
            Response.Listener {
                    response -> println("Success!")
                val tmpData = currencyData + DataHolder.loadData(response)
                tmpData.sort()
                currencyData = tmpData
                showData()
            },
            Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })
        queue.add(currenciesRatesRequestB)
    }

    private fun showData() {
        val modeArray = arrayOf<String?>(modeFirstOption, modeSecondOption)
        val modeAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, modeArray)
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeSpinner.adapter = modeAdapter

        codesArray = currencyData.map{ it.currencyCode }.toTypedArray()
        val codesAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, codesArray)
        codesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = codesAdapter
    }

    fun roundDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_EVEN
        return df.format(number).toDouble()
    }

    private fun calculateExchange() {
        val modeText = modeSpinner.selectedItem.toString()
        val moneyAmount = if (editText.text.toString() == "") {
            0.0
        } else {
            editText.text.toString().toDouble()
        }
        val chosenCurrency = currencySpinner.selectedItem.toString()
        val chosenRateRecord = currencyData.find{ it.currencyCode == chosenCurrency }
        val chosenRate = chosenRateRecord?.todayRate ?: 0.0

        val result  = if (modeText == modeFirstOption) {
            moneyAmount * chosenRate
        } else {
            moneyAmount / chosenRate
        }

        resultTextView.text = roundDecimal(result).toString()
    }
}