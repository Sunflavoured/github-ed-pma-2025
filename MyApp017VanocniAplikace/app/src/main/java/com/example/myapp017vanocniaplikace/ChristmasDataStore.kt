package com.example.myapp017vanocniaplikace

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Vytvoření instance DataStore. Jméno "christmas_prefs" je název souboru, kam se to uloží.
private val Context.dataStore by preferencesDataStore(name = "christmas_prefs")

class ChristmasDataStore(private val context: Context) {

    // 2. Definice KLÍČŮ. Pod těmito názvy budeme hodnoty hledat.
    companion object {
        val BUDGET_KEY = intPreferencesKey("budget_limit") // Celkový limit (např. 5000)
        val SPENT_KEY = intPreferencesKey("total_spent")   // Kolik už jsme utratili
    }

    // --- ČTENÍ DAT (Flow) ---
    // Flow znamená, že kdykoliv se hodnota změní, aplikace se to hned dozví.

    val budgetFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            // Pokud nic uloženo není, vrátíme 0 (elvis operátor ?: )
            preferences[BUDGET_KEY] ?: 0
        }

    val spentFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[SPENT_KEY] ?: 0
        }

    // --- ZÁPIS DAT (Suspend funkce) ---
    // Musí být suspend, protože zápis na disk chvíli trvá a nesmí zaseknout aplikaci.

    // Uloží nový limit rozpočtu
    suspend fun saveBudget(amount: Int) {
        context.dataStore.edit { preferences ->
            preferences[BUDGET_KEY] = amount
        }
    }

    // Přičte cenu dárku k útratě
    suspend fun addExpense(amount: Int) {
        context.dataStore.edit { preferences ->
            val currentSpent = preferences[SPENT_KEY] ?: 0
            preferences[SPENT_KEY] = currentSpent + amount
        }
    }

    // Vymaže útratu (reset) - hodí se na testování
    suspend fun resetSpent() {
        context.dataStore.edit { preferences ->
            preferences[SPENT_KEY] = 0
        }
    }
}