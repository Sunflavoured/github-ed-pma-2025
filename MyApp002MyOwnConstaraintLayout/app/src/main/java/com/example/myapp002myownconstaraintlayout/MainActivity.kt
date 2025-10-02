package com.example.myapp002myownconstaraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
//nejdříve si připravím proměnné dostupné pouze ze třídy MainActivity(private)
    // lateinit mi dovolí je inicializovat pozdějí ve funkci onCreate
    private lateinit var calendarView: CalendarView
    private lateinit var etEvent: EditText
    private lateinit var btnAdd: Button
    private lateinit var tvEvents: TextView

    // vytvářím proměnnou mapu klíč hodnota pro ukládání eventů k datumu, lze ji průběžné upravovat
    private val eventsMap = mutableMapOf<String, MutableList<String>>()
   //proměnná která bude ukládat datum podle zrovna vybraného datumu v kalendáři
    private var selectedDate: String = ""

    // onCreate je funkce volaná při vytvoření aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        val etSurname = findViewById<EditText>(R.id.etSurname)
        val etPlace = findViewById<EditText>(R.id.etPlace)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val tvInformation = findViewById<TextView>(R.id.tvInformation)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        // tlačítko Uložit
        btnSend.setOnClickListener {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val place = etPlace.text.toString()
            val age = etAge.text.toString()
            val phone = etPhone.text.toString()

            val formattedText = "Jmenuji se $name $surname. Je mi $age let, bydlím v $place a můj telefon je $phone."
            tvInformation.text = formattedText
        }

        // tlačítko Vymazat
        btnDelete.setOnClickListener {
            etName.text.clear()
            etSurname.text.clear()
            etPlace.text.clear()
            etAge.text.clear()
            etPhone.text.clear()
            tvInformation.text = ""
        }
    }
}
