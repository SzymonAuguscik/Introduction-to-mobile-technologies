package com.example.szymon_auguscik_czw_8_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray

class GoldActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var goldChart: LineChart
    private var textViewString: String = ""
    private var goldArray: Array<Pair<String, Double>> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)
        DataHolder.prepare(applicationContext)

        textView = findViewById(R.id.goldTextView)
        goldChart = findViewById(R.id.goldChart)

        makeRequest()
    }

    private fun makeRequest() {
        val queue = DataHolder.queue
        val urlGold = "http://api.nbp.pl/api/cenyzlota/last/30/?format=json"
        val goldRequest = JsonArrayRequest(
            Request.Method.GET, urlGold, null,
            Response.Listener {
                    response -> println("Success!")
                    goldArray = loadGoldData(response)
                    showData()
            },
            Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })

        queue.add(goldRequest)
    }

    private fun loadGoldData(response: JSONArray?) : Array<Pair<String, Double>>{
        response?.let {
            val rateCount = response.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(rateCount)
            textViewString = response.getJSONObject(rateCount - 1).getString("cena")

            for(i in 0 until rateCount) {
                val date = response.getJSONObject(i).getString("data")
                val rate = response.getJSONObject(i).getDouble("cena")

                tmpData[i] = Pair(date, rate)
            }

            return tmpData as Array<Pair<String, Double>>
        }

        return emptyArray()
    }

    private fun showData() {
        textView.text = textViewString

        val goldEntries = ArrayList<Entry>()

        for((index, element) in goldArray.withIndex()) {
            goldEntries.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val goldLineDataSet = LineDataSet(goldEntries, "Rates")
        goldLineDataSet.color = R.color.black
        goldLineDataSet.setCircleColor(R.color.black)
        goldLineDataSet.lineWidth = 3f
        goldLineDataSet.setDrawCircles(false)
        goldLineDataSet.setDrawValues(false)

        val goldLineData = LineData(goldLineDataSet)

        goldChart.data = goldLineData

        val monthDescription = Description()
        monthDescription.text = "Gold rates from last 30 stock days"
        goldChart.description = monthDescription

        goldChart.xAxis.valueFormatter = IndexAxisValueFormatter(goldArray.map{ it.first }.toTypedArray())
        goldChart.legend.isEnabled = false
        goldChart.invalidate()
    }
}