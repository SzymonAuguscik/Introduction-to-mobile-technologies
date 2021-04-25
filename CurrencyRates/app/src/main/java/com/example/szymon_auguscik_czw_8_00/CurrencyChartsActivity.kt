package com.example.szymon_auguscik_czw_8_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class CurrencyChartsActivity : AppCompatActivity() {
    private lateinit var currencyName: TextView
    private lateinit var todayRate: TextView
    private lateinit var yesterdayRate: TextView
    private lateinit var weekChart: LineChart
    private lateinit var monthChart: LineChart
    private lateinit var currencyCode: String
    private var dataArray: Array<Pair<String, Double>> = emptyArray()
    private var currencyNameString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_charts)
        DataHolder.prepare(applicationContext)

        currencyName = findViewById(R.id.currencyName)
        todayRate = findViewById(R.id.todayRate)
        yesterdayRate = findViewById(R.id.yesterdayRate)
        weekChart = findViewById(R.id.weekChart)
        monthChart = findViewById(R.id.goldChart)

        currencyCode = intent.getStringExtra("currencyCode") ?: "USD"

        getData()
    }

    fun getData() {
        val queue = DataHolder.queue

        val urlA = "http://api.nbp.pl/api/exchangerates/rates/A/%s/last/30/?format=json".format(currencyCode)
        val currenciesRatesRequestA = JsonObjectRequest(Request.Method.GET, urlA, null,
                Response.Listener { response ->
                    println("Success in chart activity - table A!")
                    dataArray = loadHistoricData(response)
                    showData()
                },
                Response.ErrorListener {
                    val urlB = "http://api.nbp.pl/api/exchangerates/rates/B/%s/last/30/?format=json".format(currencyCode)
                    val currenciesRatesRequestB = JsonObjectRequest(Request.Method.GET, urlB, null,
                        Response.Listener { response ->
                            println("Success in chart activity - table B!")
                            dataArray = loadHistoricData(response)
                            showData()
                        },
                        Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })

                    queue.add(currenciesRatesRequestB)
                })

        queue.add(currenciesRatesRequestA)
    }

    fun loadHistoricData(response: JSONObject?) : Array<Pair<String, Double>> {
        response?.let {
            val rates = response.getJSONArray("rates")
            val rateCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(rateCount)
            currencyNameString = response.getString("currency")

            for(i in 0 until rateCount) {
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")

                tmpData[i] = Pair(date, rate)
            }

            return tmpData as Array<Pair<String, Double>>
        }

        return emptyArray()
    }

    private fun showData() {
        currencyName.text = currencyNameString
        todayRate.text = getString(R.string.today_rate, dataArray.last().second)
        yesterdayRate.text = getString(R.string.yesterday_rate, dataArray[dataArray.size - 2].second)

        val monthEntries = ArrayList<Entry>()

        for((index, element) in dataArray.withIndex()) {
            monthEntries.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val weekEntries = monthEntries.subList(monthEntries.size - 7, monthEntries.size)

        val monthLineDataSet = LineDataSet(monthEntries, "Rates")
        monthLineDataSet.color = R.color.black
        monthLineDataSet.setCircleColor(R.color.black)
        monthLineDataSet.lineWidth = 3f
        monthLineDataSet.setDrawCircles(false)
        monthLineDataSet.setDrawValues(false)
        val weekLineDataSet = LineDataSet(weekEntries, "Rates")
        weekLineDataSet.color = R.color.black
        weekLineDataSet.setCircleColor(R.color.black)
        weekLineDataSet.lineWidth = 3f
        weekLineDataSet.setDrawCircles(false)
        weekLineDataSet.setDrawValues(false)

        val monthLineData = LineData(monthLineDataSet)
        val weekLineData = LineData(weekLineDataSet)

        monthChart.data = monthLineData
        weekChart.data = weekLineData

        val weekDescription = Description()
        weekDescription.text = "%s rates from last 7 stock days".format(currencyCode)
        weekChart.description = weekDescription

        val monthDescription = Description()
        monthDescription.text = "%s rates from last 30 stock days".format(currencyCode)
        monthChart.description = monthDescription

        weekChart.xAxis.valueFormatter = IndexAxisValueFormatter(dataArray.map{ it.first }.toTypedArray())
        monthChart.xAxis.valueFormatter = IndexAxisValueFormatter(dataArray.map{ it.first }.toTypedArray())

        weekChart.legend.isEnabled = false
        monthChart.legend.isEnabled = false

        monthChart.invalidate()
        weekChart.invalidate()
    }
}