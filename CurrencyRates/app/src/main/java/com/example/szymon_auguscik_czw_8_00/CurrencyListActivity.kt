package com.example.szymon_auguscik_czw_8_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest

class CurrencyListActivity : AppCompatActivity() {

    internal lateinit var recycler: RecyclerView
    internal lateinit var adapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)
        DataHolder.prepare(applicationContext)
        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyAdapter(emptyArray(), this)
        recycler.adapter = adapter

        makeRequest()
    }

    private fun makeRequest() {
        val queue = DataHolder.queue
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"
        val currenciesRatesRequestA = JsonArrayRequest(Request.Method.GET, urlA, null,
                Response.Listener { 
                        response -> println("Success!")
                        val tmpData = adapter.dataSet + DataHolder.loadData(response)
                        tmpData.sort()
                        adapter.dataSet = tmpData
                        adapter.notifyDataSetChanged()
                },
                Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })

        queue.add(currenciesRatesRequestA)

        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B/last/2/?format=json"
        val currenciesRatesRequestB = JsonArrayRequest(Request.Method.GET, urlB, null,
            Response.Listener {
                    response -> println("Success!")
                val tmpData = adapter.dataSet + DataHolder.loadData(response)
                tmpData.sort()
                adapter.dataSet = tmpData
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { TODO("ERROR, NOT IMPLEMENTED YET!") })
        queue.add(currenciesRatesRequestB)
    }
}