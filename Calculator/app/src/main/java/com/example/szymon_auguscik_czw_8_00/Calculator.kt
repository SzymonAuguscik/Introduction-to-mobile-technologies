package com.example.szymon_auguscik_czw_8_00

import android.content.Context
import android.widget.Button
import android.widget.TextView
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs

class Calculator(context: Context) : CalculatorInterface {
    internal lateinit var clearButton: Button
    internal lateinit var equationButton: Button
    internal lateinit var dotButton: Button

    internal lateinit var calculatorDisplay: TextView

    internal lateinit var digits: Array<Button>
    internal lateinit var operations: Array<Button>

    private var applicationContext : Context = context

    private var accumulatedTextFromPressedButtons : String = ""
    private val charLimit : Int = 10

    override fun clear() {
        accumulatedTextFromPressedButtons = ""
        calculatorDisplay.text = ""
    }

    override fun evaluateFormula() {
        if (accumulatedTextFromPressedButtons.isNotEmpty()) {
            val parsedText = parseTextToRNP()
            val queue = mutableListOf<Double>()

            for (token in parsedText) {
                val d = token.toDoubleOrNull()
                if (d != null) {
                    queue.add(d)
                }
                else {
                    val d1 = queue.removeAt(queue.lastIndex)
                    val d2 = queue.removeAt(queue.lastIndex)

                    queue.add(when (token) {
                        applicationContext.getString(R.string.add_character)  -> d2 + d1
                        applicationContext.getString(R.string.subtract_character)  -> d2 - d1
                        applicationContext.getString(R.string.multiply_character)  -> d2 * d1
                        applicationContext.getString(R.string.divide_character)  -> d2 / d1
                        else -> -1.0
                    })
                }
            }

            if (abs(queue[0]).isInfinite() || abs(queue[0]).isNaN()) {
                accumulatedTextFromPressedButtons = applicationContext.getString(R.string.error)
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            }
            else {
                val scoreString = round(queue[0])

                accumulatedTextFromPressedButtons = scoreString
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            }
        }
    }

    override fun handleDecimal() {
        if (accumulatedTextFromPressedButtons.isNotEmpty() && canAddDotsInCurrentText()
                && accumulatedTextFromPressedButtons != applicationContext.getString(R.string.error)) {
            accumulatedTextFromPressedButtons += applicationContext.getString(R.string.dot_character)
            addNewStringToDisplay(accumulatedTextFromPressedButtons)
        }
    }

    override fun handleDigitButton(button: Button) {
        if (accumulatedTextFromPressedButtons != applicationContext.getString(R.string.error)) {
            if (accumulatedTextFromPressedButtons.isEmpty()) {
                accumulatedTextFromPressedButtons = button.text.toString()
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            } else if (accumulatedTextFromPressedButtons.last() == applicationContext.getString(R.string.digit_0)[0]) {
                val len = accumulatedTextFromPressedButtons.length
                //only '0' on display
                if (len == 1) {
                    accumulatedTextFromPressedButtons = button.text.toString()
                    addNewStringToDisplay(accumulatedTextFromPressedButtons)
                }
                // '0' not preceded by an operation character
                else if (accumulatedTextFromPressedButtons[len - 2] !in getOperations()) {
                    accumulatedTextFromPressedButtons += button.text.toString()
                    addNewStringToDisplay(accumulatedTextFromPressedButtons)
                }
            } else {
                accumulatedTextFromPressedButtons += button.text.toString()
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            }
        }
    }

    override fun handleOperationButton(button: Button) {
        if (accumulatedTextFromPressedButtons != applicationContext.getString(R.string.error)) {
            // leading '-' (in order to start expressions with negative numbers)
            if (accumulatedTextFromPressedButtons.isEmpty() && button.text == applicationContext.getString(R.string.subtract_character)) {
                accumulatedTextFromPressedButtons += button.text.toString()
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            }
            // multiple operators are forbidden (including '.')
            else if (accumulatedTextFromPressedButtons.isNotEmpty() && (accumulatedTextFromPressedButtons.last() !in getOperations())
                    && accumulatedTextFromPressedButtons.last() != applicationContext.getString(R.string.dot_character)[0]) {
                accumulatedTextFromPressedButtons += button.text.toString()
                addNewStringToDisplay(accumulatedTextFromPressedButtons)
            }
        }
    }

    private fun canAddDotsInCurrentText() : Boolean {
        val arrayOfDelimiters = getOperations().toCharArray()
        val textTokens: List<String> = tokenizeString(arrayOfDelimiters)

        return textTokens.isNotEmpty() &&
                textTokens.last().isNotEmpty() &&
                !textTokens.last().contains(applicationContext.getString(R.string.dot_character)) &&
                textTokens.last().last() !in getOperations()
    }

    private fun isLongerThanLimitAfterAddingString(string: String) : Boolean {
        return string.length > charLimit
    }

    private fun addNewStringToDisplay(string: String) {
        if (isLongerThanLimitAfterAddingString(string)) {
            val newText = applicationContext.getString(R.string.offset_characters) +
                    string.substring(string.length-charLimit)
            calculatorDisplay.text = newText
        }
        else {
            calculatorDisplay.text = string
        }
    }

    private fun tokenizeString(arrayOfDelimiters: CharArray) : List<String> {
        return accumulatedTextFromPressedButtons.split(*arrayOfDelimiters)
    }

    private fun getPriorityOfOperation(op: Char) : Int {
        return when (op) {
            applicationContext.getString(R.string.multiply_character)[0] -> 7
            applicationContext.getString(R.string.divide_character)[0] -> 7
            applicationContext.getString(R.string.add_character)[0] -> 4
            applicationContext.getString(R.string.subtract_character)[0] -> 4
            else -> 1
        }
    }

    private fun parseTextToRNP(): MutableList<String> {
        val stack = Stack<String>()
        val queue = mutableListOf<String>()

        if (accumulatedTextFromPressedButtons.last() in getOperations() ||
                accumulatedTextFromPressedButtons.last() == applicationContext.getString(R.string.dot_character)[0]) {
            accumulatedTextFromPressedButtons = accumulatedTextFromPressedButtons.substring(0, accumulatedTextFromPressedButtons.length - 1)
        }

        for (op in getOperations()) {
            if (op != accumulatedTextFromPressedButtons[0] || op != applicationContext.getString(R.string.subtract_character)[0]) {
                accumulatedTextFromPressedButtons = accumulatedTextFromPressedButtons.replace(op.toString(), " $op ")
            }
        }

        val delimiter = charArrayOf(' ')
        val tokens = tokenizeString(delimiter)

        for (token in tokens) {
            if (token.last() in getOperations()) {
                if (stack.isNotEmpty() && getPriorityOfOperation(stack.firstElement()[0]) > getPriorityOfOperation(token.last())) {
                    while(stack.isNotEmpty()) {
                        queue.add(stack.pop())
                    }
                }
                stack.push(token)
            }
            //just a number
            else {
                queue.add(token)
            }
        }

        while (stack.isNotEmpty()) {
            queue.add(stack.pop())
        }

        accumulatedTextFromPressedButtons = ""

        return queue
    }

    private fun round(num: Double) : String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_EVEN
        return df.format(num)
    }

    private fun getOperations() : String {
        return applicationContext.getString(R.string.add_character) +
                applicationContext.getString(R.string.subtract_character) +
                applicationContext.getString(R.string.divide_character) +
                applicationContext.getString(R.string.multiply_character)
    }
}