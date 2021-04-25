package com.example.szymon_auguscik_czw_8_00

/*
Szymon Auguścik czwartek 8:00

Wykonane podpunkty:
1. layout - w całości
2. logika obliczeń - w całości, implementacja własna
3. zabezpieczenia:
    * zapewnione: obsługa kropki, rozpoczynanie od liczb ujemnych
    * zabronione: wielokrotne użycie operatorów, wielokrotne użycie zera przed kropką,
                  kropka i operator koło siebie
    * obsłużone jako błąd: dzielenie przez zero
4. zmienna calculatorDisplay to textView, który jest aktualizowany za pomocą zmiennej
  accumulatedTextFromPressedButtons
5. możliwość zmiany znaku reprezentującego wybrane działanie matematyczne
6. zbyt długie napisy zastępowane są offset_characters ze strings.xml
7. wykluczone przez pkt 9.
8. -
9. obsługa kolejności wykonywania działań poprzez odwróconą notację polską

Braki:
3. wszystkie nieodkryte błędy (o ile występują)
8. brak zapewnienia pierwiastków oraz procentów
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var calculator : Calculator
    override fun onCreate(savedInstanceState: Bundle?) {
        calculator = Calculator(this.applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculator.clearButton = findViewById(R.id.ClearButton)
        calculator.equationButton = findViewById(R.id.EquationButton)
        calculator.dotButton = findViewById(R.id.DotButton)
        calculator.calculatorDisplay = findViewById(R.id.textView)

        val digitsIDs = arrayOf(R.id.Button0, R.id.Button1, R.id.Button2, R.id.Button3,
                R.id.Button4, R.id.Button5, R.id.Button6, R.id.Button7, R.id.Button8, R.id.Button9)

        val operationsIDs = arrayOf(R.id.AddButton, R.id.SubtractButton, R.id.MultiplyButton,
                R.id.DivideButton)

        calculator.digits = (digitsIDs.map{ value -> findViewById<Button>(value) }).toTypedArray()
        calculator.operations = (operationsIDs.map{ value -> findViewById<Button>(value) }).toTypedArray()

        calculator.clearButton.setOnClickListener{ calculator.clear() }
        calculator.equationButton.setOnClickListener{ calculator.evaluateFormula() }
        calculator.dotButton.setOnClickListener{ calculator.handleDecimal() }

        calculator.digits.forEach{ button -> button.setOnClickListener{i -> calculator.handleDigitButton(i as Button)}}
        calculator.operations.forEach { button -> button.setOnClickListener{i -> calculator.handleOperationButton(i as Button)} }
    }
}