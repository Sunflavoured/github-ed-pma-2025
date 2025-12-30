package com.example.myapp017vanocniaplikace

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "christmas_prefs")

class ChristmasDataStore(private val context: Context) {
    private val gson = Gson()

    // Create the TypeToken once to avoid overhead on every call
    private val giftListType = object : TypeToken<List<Gift>>() {}.type

    companion object {
        val BUDGET_KEY = intPreferencesKey("budget_limit")
        val GIFTS_KEY = stringPreferencesKey("gifts_list")
    }

    val giftsFlow: Flow<List<Gift>> = context.dataStore.data.map { prefs ->
        val json = prefs[GIFTS_KEY] ?: "[]"
        try {
            gson.fromJson(json, giftListType) ?: emptyList()
        } catch (e: Exception) {
            emptyList() // Fallback if JSON is corrupted
        }
    }

    val budgetFlow: Flow<Int> = context.dataStore.data.map { it[BUDGET_KEY] ?: 5000 }

    suspend fun saveGifts(gifts: List<Gift>) {
        context.dataStore.edit { it[GIFTS_KEY] = gson.toJson(gifts) }
    }

    suspend fun saveBudget(amount: Int) {
        context.dataStore.edit { it[BUDGET_KEY] = amount }
    }

    suspend fun resetBudget() {
        context.dataStore.edit { preferences ->
            preferences[BUDGET_KEY] = 0
        }
    }

    suspend fun resetSpent() {
        context.dataStore.edit { preferences ->
            preferences[GIFTS_KEY] = "[]"
        }
    }

    suspend fun updateGift(updatedGift: Gift) {
        context.dataStore.edit { preferences ->
            // 1. Get current JSON string from the mutable preferences
            val json = preferences[GIFTS_KEY] ?: "[]"
            // 2. Deserialize
            val currentGifts: MutableList<Gift> = gson.fromJson(json, giftListType) ?: mutableListOf()

            // 3. Find and Update
            val index = currentGifts.indexOfFirst { it.id == updatedGift.id }
            if (index != -1) {
                currentGifts[index] = updatedGift
                // 4. Serialize and save back
                preferences[GIFTS_KEY] = gson.toJson(currentGifts)
            }
        }
    }

    suspend fun deleteGift(giftId: Long) {
        context.dataStore.edit { preferences ->
            val json = preferences[GIFTS_KEY] ?: "[]"
            val currentGifts: MutableList<Gift> = gson.fromJson(json, giftListType) ?: mutableListOf()

            val wasRemoved = currentGifts.removeAll { it.id == giftId }

            if (wasRemoved) {
                preferences[GIFTS_KEY] = gson.toJson(currentGifts)
            }
        }
    }
}