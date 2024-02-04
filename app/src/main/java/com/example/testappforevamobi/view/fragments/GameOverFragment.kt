package com.example.testappforevamobi.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.testappforevamobi.R
import com.example.testappforevamobi.databinding.FragmentGameOverBinding
import com.example.testappforevamobi.model.constant.CURRENT_RESULT
import com.example.testappforevamobi.model.constant.MAIN
import com.example.testappforevamobi.model.repository.Repository

class GameOverFragment : Fragment() {

    private var binding: FragmentGameOverBinding? = null
    private var alertDialog: AlertDialog? = null
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameOverBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())

        val currentResult = requireArguments().getInt(CURRENT_RESULT)
        val bestResult = repository.getBestResult()

        // показ текущего результата
        binding!!.idGameOverTvCurrentResult.text = "your current result: $currentResult levels"

        // показ рекорда
        if(currentResult > bestResult){
            repository.showToast("you have a new record!")
            repository.updateBestResult(currentResult)
            binding!!.idGameOverTvRecord.text = "your best score: $currentResult levels"
        }else{
            binding!!.idGameOverTvRecord.text = "your best score: $bestResult levels"
        }

        // обработка повтора игры
        binding!!.idGameOverButtonAgain.setOnClickListener {
            MAIN.navController.navigate(R.id.action_gameOverFragment_to_gameFragment)
        }

        // обработка перехода в меню
        binding!!.idGameOverButtonMenu.setOnClickListener {
            MAIN.navController.navigate(R.id.action_gameOverFragment_to_menuFragment)
        }

        // обработка выхода в меню или повторной игры при нажатии на кнопку НАЗАД
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

        builder.setTitle("выход")
        builder.setMessage("куда вы хотите выйти?")

        builder.setPositiveButton("в меню") { _, _ ->
            MAIN.navController.navigate(R.id.action_gameOverFragment_to_menuFragment)
        }

        builder.setNegativeButton("повтор игры") { _, _ ->
            MAIN.navController.navigate(R.id.action_gameOverFragment_to_gameFragment)
        }

        alertDialog = builder.create()
        alertDialog?.show()
    }

}