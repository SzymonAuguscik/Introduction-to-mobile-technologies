package com.example.szymon_auguscik_czw_8_00

data class CurrencyDetails(var currencyCode: String, var todayRate: Double, var yesterdayRate: Double, var flag: Int = 0) : Comparable<CurrencyDetails> {
    override fun compareTo(other: CurrencyDetails): Int = this.currencyCode.compareTo(other.currencyCode)
    override fun toString(): String = currencyCode
}