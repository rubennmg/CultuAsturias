package com.example.cultuasturias.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cultuasturias.data.ApiResult
import com.example.cultuasturias.data.Repository
import com.example.cultuasturias.model.CulturalVenueItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos de la aplicación. Almacena datos de tipo [CulturalVenueItem] en la
 * tabla cultural_venues_table.
 *
 */
@Database(entities = [CulturalVenueItem::class], version = 1)
abstract class CultuAstDatabase : RoomDatabase() {
    abstract fun culturalVenueDAO(): CulturalVenueDAO

    companion object {
        private var INSTANCE: CultuAstDatabase? = null

        fun getInstance(context: Context): CultuAstDatabase? {
            if (INSTANCE == null) {
                synchronized(CultuAstDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CultuAstDatabase::class.java, "cultu_ast.db"
                    )
                        .addCallback(CALLBACK)
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // Insertar datos obtenidos en el repositorio
                CoroutineScope(Dispatchers.IO).launch {
                    Repository.updateCulturalVenues().collect { apiResult ->
                        if (apiResult is ApiResult.Success) {
                            INSTANCE!!.culturalVenueDAO().insertAll(apiResult._data)
                        }
                    }
                }
            }
        }
    }
}