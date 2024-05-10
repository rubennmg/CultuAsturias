package com.example.cultuasturias.network

import com.example.cultuasturias.model.CulturalVenueItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type

private val BASE_URL = " http://orion.edv.uniovi.es"

interface RestApiService {
    @GET("/~arias/json/MuseosEspaciosCulturales.json")
    suspend fun getCulturalVenues(): List<CulturalVenueItem>
}

private val moshi = Moshi.Builder()
    .add(DefaultIfNullFactory())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

class DefaultIfNullFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>,
                        moshi: Moshi): JsonAdapter<*>? {
        val delegate = moshi.nextAdapter<Any>(this, type, annotations)
        return object : JsonAdapter<Any>() {
            override fun fromJson(reader: JsonReader): Any? {
                val blob1 = reader.readJsonValue()
                try {
                    val blob = blob1 as Map<String, Any?>
                    val noNulls = blob.filterValues { it != null }
                    return delegate.fromJsonValue(noNulls)
                } catch (e: Exception) {
                    return delegate.fromJsonValue(blob1)
                }
            }
            override fun toJson(writer: JsonWriter, value: Any?) {
                return delegate.toJson(writer, value)
            }
        }
    }
}

object RestApi {
    val retrofitService: RestApiService by lazy {
        retrofit.create(RestApiService::class.java)
    }
}