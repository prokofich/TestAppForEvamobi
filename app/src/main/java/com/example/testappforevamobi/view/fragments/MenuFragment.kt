package com.example.testappforevamobi.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.testappforevamobi.R
import com.example.testappforevamobi.databinding.FragmentGameBinding
import com.example.testappforevamobi.databinding.FragmentMenuBinding
import com.example.testappforevamobi.model.constant.MAIN

class MenuFragment : Fragment() {

    private var binding: FragmentMenuBinding? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // переход к игре
        binding!!.idMenuButtonPlay.setOnClickListener {
            MAIN.navController.navigate(R.id.action_menuFragment_to_gameFragment)
        }

        // выход из игры
        binding!!.idMenuButtonExit.setOnClickListener {
            showDialog()
        }

        // выход из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            showDialog()
        }


    }

    // очистка биндинга при очистке view
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // функция показа диалогового сообщения
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("выход из приложения")
        builder.setMessage("вы точно хотите выйти?")

        builder.setPositiveButton("выйти") { _, _ ->
            MAIN.closeApplication()
        }

        builder.setNegativeButton("отмена") { _, _ ->
            alertDialog?.dismiss()
        }

        alertDialog = builder.create()
        alertDialog?.show()
    }

}