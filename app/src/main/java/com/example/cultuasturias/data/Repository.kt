package com.example.cultuasturias.data

import com.example.cultuasturias.db.CulturalVenueDatabase
import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.network.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Repository {

    private  val culturalVenueDAO = CulturalVenueDatabase.getInstance()!!.culturalVenueDAO()

    fun updateCulturalVenues() =
        flow <ApiResult<List<CulturalVenueItem>>> {
            try {
                val culturalVenues = RestApi.retrofitService.getCulturalVenues()
                emit(ApiResult.Success(culturalVenues))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getCulturalVenuesNames() = culturalVenueDAO.getNames()
    fun getCulturalVenueByName(name: String) = culturalVenueDAO.getCulturalVenueByName(name)
    suspend fun insertCulturalVenue(culturalVenueItem: CulturalVenueItem) = culturalVenueDAO.insertCulturalVenue(culturalVenueItem)
    suspend fun deleteCulturalVenue(name: String) = culturalVenueDAO.deleteCulturalVenue(name)
}