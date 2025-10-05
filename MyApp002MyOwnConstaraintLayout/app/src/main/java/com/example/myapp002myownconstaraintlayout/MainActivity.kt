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

    // hlavní funkce
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)
        etEvent = findViewById(R.id.etEvent)
        btnAdd = findViewById(R.id.btnAdd)
        tvEvents = findViewById(R.id.tvEvents)

        //formatování datumu a uložení aktuálního datumu
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // When a date is selected
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = dateFormat.format(calendar.time)
            showEventsForDate(selectedDate)
        }

        // Add event button
        btnAdd.setOnClickListener {
            val eventText = etEvent.text.toString().trim()
            if (eventText.isNotEmpty() && selectedDate.isNotEmpty()) {
                val eventList = eventsMap.getOrPut(selectedDate) { mutableListOf() }
                eventList.add(eventText)
                etEvent.text.clear()
                showEventsForDate(selectedDate)
            } else {
                Toast.makeText(this, "Vyber datum a zadej událost", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEventsForDate(date: String) {
        val events = eventsMap[date]
        tvEvents.text = if (events.isNullOrEmpty()) {
            "No events for $date"
        } else {
            "Events on $date:\n" + events.joinToString("\n")
        }
    }
}
