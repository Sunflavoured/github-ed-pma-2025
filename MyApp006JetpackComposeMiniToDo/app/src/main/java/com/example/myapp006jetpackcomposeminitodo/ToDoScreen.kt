package com.example.myapp006jetpackcomposeminitodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.style.TextDecoration


data class TodoItem(
    val title: String,
    val isDone: Boolean = false,
    val priority: Int = 0,
    val description: String = ""
)
@Composable
fun ToDoScreen(modifier: Modifier = Modifier) {
    // Stav pro textové pole
    var titleInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var priorityInput by remember { mutableStateOf("") }


    // Stav seznamu úkolů
    //val tasks = remember { mutableStateListOf<String>() }
    val tasks = remember { mutableStateListOf<TodoItem>() }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Pole pro NÁZEV
        TextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            label = { Text("Název úkolu") },
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Pole pro POPIS
        TextField(
            value = descriptionInput,
            onValueChange = { descriptionInput = it },
            label = { Text("Popis") },
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Řádek s PRIORITOU a TLAČÍTKEM
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = priorityInput,
                onValueChange = { priorityInput = it },
                label = { Text("Priorita") },
                modifier = Modifier.weight(1f) // Zabere dostupnou šířku
            )

            Button(
                onClick = {
                    if (titleInput.isNotBlank()) {
                        // LOGIKA PŘEVODU:
                        // Zkusíme převést text na Int. Pokud to nejde (je to prázdné nebo text),
                        // použije se operátor ?: (Elvis) a nastaví se 0.
                        val priorityNumber = priorityInput.toIntOrNull() ?: 0

                        tasks.add(
                            TodoItem(
                                title = titleInput,
                                description = descriptionInput,
                                priority = priorityNumber
                            )
                        )
                        // Vymazat pole po přidání
                        titleInput = ""
                        descriptionInput = ""
                        priorityInput = ""
                    }
                },
                modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically) // Zarovnání tlačítka na střed řádku
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ... Zde pokračuje LazyColumn (výpis úkolů), ten zůstává stejný ...


        Spacer(modifier = Modifier.height(16.dp))


        // ----- Seznam úkolů -----
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val index = tasks.indexOf(task)
                            tasks[index] = task.copy(isDone = !task.isDone)
                        }
                ) {
                    // Doporučuji obalit obsah do Columnu a dát padding jemu, vypadá to lépe
                    Column(modifier = Modifier.padding(16.dp)) {

                        // 1. NÁZEV (upravíme styl, aby byl tučnější)
                        Text(
                            text = task.title,
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                            // Můžeš přidat: style = MaterialTheme.typography.titleMedium
                        )

                        // 2. POPIS
                        Text(
                            text = task.description

                            // Můžeš přidat: style = MaterialTheme.typography.titleMedium
                        )


                        // 3. PRIORITA
                        Text(
                            text = "Priorita: ${task.priority}"

                            // Můžeš přidat: style = MaterialTheme.typography.titleMedium
                        )


                        // Tlačítko smazat
                        TextButton(onClick = { tasks.remove(task) }) {
                            Text("Smazat")
                        }
                    }
                }
            }
        }
    }
}
