package com.example.myapp013aeducationgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapp013aeducationgame.database.AppDatabase
import com.example.myapp013aeducationgame.database.User
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    // Přístup k databázi
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.gameDao()

    // Data, která sleduje UI (když se změní, UI se překreslí)
    val currentUser = MutableLiveData<User?>()

    // Funkce pro přihlášení nebo registraci
    fun loginOrRegister(username: String) {
        viewModelScope.launch {
            // 1. Zkusíme najít uživatele v DB
            var user = dao.getUserByName(username)

            // 2. Pokud neexistuje, vytvoříme ho
            if (user == null) {
                val newUser = User(username = username, highestScore = 0)
                dao.insertUser(newUser)
                // Znovu ho načteme, abychom měli jeho vygenerované ID
                user = dao.getUserByName(username)
            }

            // 3. Nastavíme ho jako aktuálního uživatele
            currentUser.value = user
        }
    }
}