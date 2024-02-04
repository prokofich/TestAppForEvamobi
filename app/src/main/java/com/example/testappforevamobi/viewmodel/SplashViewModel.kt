package com.example.testappforevamobi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappforevamobi.model.modelgeoinfo.GeoInfo
import com.example.testappforevamobi.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SplashViewModel():ViewModel() {

    val geoInfo : MutableLiveData<Response<GeoInfo>> = MutableLiveData()

    // функция асинхронного получения геопозиции
    fun getGeoInfo(repository: Repository){
        viewModelScope.launch(Dispatchers.IO) {
            val answer = repository.getGeoInfo()
            withContext(Dispatchers.Main){
                geoInfo.value = answer
            }
        }
    }

}