package com.example.cultuasturias.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cultuasturias.model.CulturalVenueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CulturalVenueDAO {
    @Query("SELECT * FROM cultural_venues_table")
    fun getAllCulturalVenues(): List<CulturalVenueItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCulturalVenue(culturalVenueItem: CulturalVenueItem)

    @Query("DELETE FROM cultural_venues_table WHERE Nombre = :culturalVenueItem")
    suspend fun deleteCulturalVenue(culturalVenueItem: String)

    @Query("SELECT * FROM cultural_venues_table WHERE Nombre LIKE :name")
    fun getCulturalVenueByName(name: String): Flow<CulturalVenueItem>

    @Query("SELECT Nombre FROM cultural_venues_table")
    fun getNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(culturalVenues: List<CulturalVenueItem>)

    @Query("SELECT * FROM cultural_venues_table WHERE Nombre LIKE '%' || :query || '%' OR Localidad LIKE '%' || :query || '%' OR Concejo LIKE '%' || :query || '%'")
    fun searchCulturalVenues(query: String): Flow<List<CulturalVenueItem>>
}