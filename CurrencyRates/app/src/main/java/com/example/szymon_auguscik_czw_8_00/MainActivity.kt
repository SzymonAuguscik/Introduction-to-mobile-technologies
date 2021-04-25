package com.example.szymon_auguscik_czw_8_00

/*
Szymon Auguścik

Zrealizowane punkty:
3. Obsługa obu tabel NBP
    Aplikacja wykorzystuje waluty z obu tabel. Ponadto, waluty są wyświetlane
    w porządku alfabetycznym
4. Kursy walut:
    a. lista lub siatka - waluty ustawione są na liście za pomocą RecyclerView
    b. flagi państw - w ramach tego podpunktu skorzystano z biblioteki currency_picker dla
    dostępnych państw, a w przypadku braku flagi posłużono się flagą światową z biblioteki
    country data
    c. obsługa błędów - z uwagi na rozwiązanie z punktu b., takowe błędy nie zachodzą
    d. wykresy po wyborze z listy - po kliknięciu następuje przejście do nowego Activity,
    w którym dostępne są takie informacje na temat waluty, jak jej nazwa, ostatnie dwa kursy oraz
    dwa wykresy (tygodniowy i miesięczny), które zostały zmodyfikowane w celu dostosowania
    wyglądu (pogrubienie linii, usunięcie etykiet punktów, pozbycie się kół z wykresu etc)
    e. spadek i wzrost - podpunkt został zrealizowany wraz z inwencją twórczą, tj. w przypadku
    spadku kursu wyświetlony został obraz z Leonadro DiCaprio gryzącym się w pięść, a w przypadku
    wzrostu - obrazek z Leonardo DiCaprio rzucającym pieniędzmi
5. Złoto
    a. aktualny kurs - zrealizowano
    b. wykres - zrealizowano, wygląd analogiczny jak w punkcie 4d.
6. Przelicznik walut
    a. i b. - konwersja w obie strony została zapewniona poprzez wykorzystanie Spinnerów.
    Pierwszy z nich służy do wyboru konwersji ("złoty -> inna waluta" lub "inna waluta -> złoty"),
    drugi natomiast do wyboru pożądanej waluty. Przez EditText użytkownik powinien podać liczbę
    stanowiącą liczbę przeliczanych pieniędzy (implementacja pozwala na podanie nieujemnej liczby
    całkowitej, w przypadku niepodania liczby domyślnie przyjmuje się wartość 0.0). Ostatecznie
    po wciśnięciu przycisku wynik zostaje wyświetlony w formie TextView u dołu ekranu.
*/

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // only 'goto' instructions one should use
    fun goToCurrencies(view: View) {
        val intent = Intent(this, CurrencyListActivity::class.java)
        startActivity(intent)
    }

    fun goToGold(view: View) {
        val intent = Intent(this, GoldActivity::class.java)
        startActivity(intent)
    }

    fun goToPredictor(view: View) {
        val intent = Intent(this, CurrencyPredictorActivity::class.java)
        startActivity(intent)
    }
}