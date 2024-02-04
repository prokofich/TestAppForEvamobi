package com.example.testappforevamobi.model.api

import com.example.testappforevamobi.model.modelgeoinfo.GeoInfo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    // асинхронная функция получения геопозиции пользователя
    @GET("/json")
    suspend fun getGeoInfo():Response<GeoInfo>

}