package com.example.szymon_auguscik_czw_8_00

import android.widget.Button

interface CalculatorInterface {
    fun clear() {}
    fun evaluateFormula() { }
    fun handleDecimal() { }
    fun handleDigitButton(button: Button) { }
    fun handleOperationButton(button: Button) { }
}