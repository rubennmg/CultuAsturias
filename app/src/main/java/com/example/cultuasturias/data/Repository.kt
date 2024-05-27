package com.example.cultuasturias.data

import android.content.Context
import com.example.cultuasturias.db.CulturalVenueDAO
import com.example.cultuasturias.db.CultuAstDatabase
import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.network.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Repositorio de la aplicación. Se encarga de gestionar la comunicación entre API y la base de datos a
 * través de la clase [RestApi] y el DAO [CulturalVenueDAO], respectivamente
 *
 */
object Repository {
    private lateinit var culturalVenueDAO: CulturalVenueDAO

    fun initializeDatabase(context: Context) {
        val db = CultuAstDatabase.getInstance(context)
        if (db != null) {
            culturalVenueDAO = db.culturalVenueDAO()
        }
    }

    fun updateCulturalVenues() =
        flow <ApiResult<List<CulturalVenueItem>>> {
            try {
                val culturalVenues = RestApi.retrofitService.getCulturalVenues()
                withContext(Dispatchers.IO) {
                    culturalVenueDAO.insertAll(culturalVenues)
                }
                emit(ApiResult.Success(culturalVenues))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.toString()))
                val cachedCulturalVenues = culturalVenueDAO.getAllCulturalVenues()
                if (cachedCulturalVenues.isNotEmpty()) {
                    emit(ApiResult.Success(cachedCulturalVenues))
                }
            }
        }.flowOn(Dispatchers.IO)

    fun getAllCulturalVenues(): Flow<List<CulturalVenueItem>> {
        return flow {
            emit(culturalVenueDAO.getAllCulturalVenues() ?: emptyList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertAll(culturalVenues: List<CulturalVenueItem>) = culturalVenueDAO.insertAll(culturalVenues)

    fun searchCulturalVenues(query: String): Flow<List<CulturalVenueItem>> =
        culturalVenueDAO.searchCulturalVenues("%$query%")
            .flowOn(Dispatchers.IO)

    fun getCulturalVenueByName(name: String): Flow<CulturalVenueItem> = culturalVenueDAO.getCulturalVenueByName(name)

    fun getCulturalVenuesNames() = culturalVenueDAO.getNames()

    suspend fun insertCulturalVenue(culturalVenueItem: CulturalVenueItem) = culturalVenueDAO.insertCulturalVenue(culturalVenueItem)

    suspend fun deleteCulturalVenue(name: String) = culturalVenueDAO.deleteCulturalVenue(name)
}
