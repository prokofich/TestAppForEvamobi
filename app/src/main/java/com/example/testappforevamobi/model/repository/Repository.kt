package com.example.testappforevamobi.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.testappforevamobi.model.api.RetrofitInstance
import com.example.testappforevamobi.model.constant.BEST_RESULT
import com.example.testappforevamobi.model.modelgeoinfo.GeoInfo
import retrofit2.Response

class Repository(private val context:Context) {

    @Suppress("DEPRECATION")
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    // функция асинхронного получения геопозиции пользователя
    suspend fun getGeoInfo(): Response<GeoInfo> {
        return RetrofitInstance.api.getGeoInfo()
    }

    // функция получения рекорда в игре
    fun getBestResult():Int{
        return sharedPreferences.getInt(BEST_RESULT,0)
    }

    // функция обновления рекорда в игре
    fun updateBestResult(record:Int){
        sharedPreferences.edit()
            .putInt(BEST_RESULT,record)
            .apply()
    }

    // функция показа всплывающего сообщения
    fun showToast(message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

}