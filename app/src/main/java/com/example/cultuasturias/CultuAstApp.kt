package com.example.cultuasturias

import android.app.Application
import com.example.cultuasturias.data.Repository

class CultuAstApp: Application() {
    lateinit var repository: Repository

    // Inicializar la base de datos de la aplicación
    override fun onCreate() {
        super.onCreate()
        repository = Repository.apply {
            initializeDatabase(this@CultuAstApp)
        }
    }
}