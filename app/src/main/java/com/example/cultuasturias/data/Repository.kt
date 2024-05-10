package com.example.cultuasturias.data

import com.example.cultuasturias.model.CulturalVenuesItem
import com.example.cultuasturias.network.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Repository {

    fun updateCulturalVenues() =
        flow <ApiResult<List<CulturalVenuesItem>>> {
            try {
                val culturalVenues = RestApi.retrofitService.getCulturalVenues()
                emit(ApiResult.Success(culturalVenues))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
}