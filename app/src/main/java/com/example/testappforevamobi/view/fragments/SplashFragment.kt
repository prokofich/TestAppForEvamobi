package com.example.testappforevamobi.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.testappforevamobi.R
import com.example.testappforevamobi.databinding.FragmentGameBinding
import com.example.testappforevamobi.databinding.FragmentSplashBinding
import com.example.testappforevamobi.model.constant.MAIN
import com.example.testappforevamobi.model.repository.Repository
import com.example.testappforevamobi.viewmodel.SplashViewModel

class SplashFragment : Fragment() {

    private var binding: FragmentSplashBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // привязка к ViewModel
        val splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        // вызов функции получения геопозиции пользователя
        splashViewModel.getGeoInfo(Repository(requireContext()))

        // слушатель событий для ответа от сервера
        splashViewModel.geoInfo.observe(viewLifecycleOwner){
            if(it.body()!!.countryCode == "RU"){

                MAIN.navController.navigate(R.id.action_splashFragment_to_webFragment) // переход к WebView

            }else{

                MAIN.navController.navigate(R.id.action_splashFragment_to_menuFragment) // переход на заглушку

            }
        }

    }

    // очистка биндинга при очистке view
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}