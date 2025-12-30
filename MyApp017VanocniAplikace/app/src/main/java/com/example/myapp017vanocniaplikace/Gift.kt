package com.example.myapp017vanocniaplikace

// Datová třída pro jeden dárek
data class Gift(
    val id: Long = System.currentTimeMillis(), // Unikátní ID podle času vytvoření
    val personName: String, // Pro koho to je (Máma, Partner...)
    val giftDescription: String, // Co to je (Ponožky, Lego...)
    val price: Int,
    var isSelected: Boolean = false // Zda jsme ho vybrali do finální kombinace
)