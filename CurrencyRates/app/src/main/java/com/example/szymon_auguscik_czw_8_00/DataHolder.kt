package com.example.szymon_auguscik_czw_8_00

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.World
import com.mynameismidori.currencypicker.ExtendedCurrency
import org.json.JSONArray

object DataHolder {
    lateinit var queue: RequestQueue
    private lateinit var currencies: List<ExtendedCurrency>

    fun prepare(context: Context) {
        World.init(context)
        queue = newRequestQueue(context)
        currencies = ExtendedCurrency.getAllCurrencies()
    }

    private fun getFlag(countryCode: String) : Int {
        return currencies.find { it.code == countryCode }?.flag ?: World.getWorldFlag()
    }

    fun loadData(response: JSONArray?): Array<CurrencyDetails> {
        response?.let {
            val yesterdayRates = response.getJSONObject(0).getJSONArray("rates")
            val todayRates = response.getJSONObject(1).getJSONArray("rates")
            val rateCount = todayRates.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(rateCount)

            for(i in 0 until rateCount) {
                val currencyCode = todayRates.getJSONObject(i).getString("code")
                val todayRate = todayRates.getJSONObject(i).getDouble("mid")
                val yesterdayRate = yesterdayRates.getJSONObject(i).getDouble("mid")
                val flag = getFlag(currencyCode)
                val currencyObject = CurrencyDetails(currencyCode, todayRate, yesterdayRate, flag)

                tmpData[i] = currencyObject
            }

            return tmpData as Array<CurrencyDetails>
        }
        return emptyArray()
    }
}