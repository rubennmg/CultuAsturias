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

@Database(entities = [CulturalVenueItem::class], version = 1)
abstract class CulturalVenueDatabase : RoomDatabase() {

    abstract fun culturalVenueDAO(): CulturalVenueDAO

    companion object {
        private var INSTANCE: CulturalVenueDatabase? = null

        fun getInstance(context: Context): CulturalVenueDatabase? {
            if (INSTANCE == null) {
                synchronized(CulturalVenueDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CulturalVenueDatabase::class.java, "cultural_venues.db"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        fun getInstance(): CulturalVenueDatabase? {
            return INSTANCE
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // Insertar datos obtenidos en el repositorio
                CoroutineScope(Dispatchers.IO).launch {
                    Repository.updateCulturalVenues().collect { apiResult ->
                        if (apiResult is ApiResult.Success) {
                            apiResult.data?.forEach { culturalVenueItem ->
                                INSTANCE!!.culturalVenueDAO().insertCulturalVenue(culturalVenueItem)
                            }
                        }
                    }
                }
            }
        }
    }
}