package com.example.testappforevamobi.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testappforevamobi.R
import com.example.testappforevamobi.databinding.ActivityMainBinding
import com.example.testappforevamobi.model.constant.MAIN

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //установка полноэкранного режима
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        MAIN = this
        navController = Navigation.findNavController(this,R.id.id_nav_host)


    }

    // функция закрытия приложения
    fun closeApplication(){
        this.finishAffinity()
    }

}