package com.example.testappforevamobi.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testappforevamobi.R
import com.example.testappforevamobi.databinding.FragmentGameBinding
import com.example.testappforevamobi.model.adapter.GameAdapter
import com.example.testappforevamobi.model.adapter.InterfaceForAdapter
import com.example.testappforevamobi.model.constant.CURRENT_RESULT
import com.example.testappforevamobi.model.constant.LOSS
import com.example.testappforevamobi.model.constant.MAIN
import com.example.testappforevamobi.model.constant.WIN
import com.example.testappforevamobi.model.constant.listItemsForGame
import com.example.testappforevamobi.model.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment(),InterfaceForAdapter {

    private var binding: FragmentGameBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameAdapter: GameAdapter
    private lateinit var repository: Repository

    private var jobTimer:Job? = null

    private var startSec = 10
    private var level = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())

        recyclerView = binding!!.idGameRv
        gameAdapter = GameAdapter(this,requireContext())
        recyclerView.layoutManager = GridLayoutManager(requireContext(),6)
        recyclerView.adapter = gameAdapter

        binding!!.idGameGoOrFinish.setOnClickListener {
            if(binding!!.idGameGoOrFinish.text != "-finish-"){
                binding!!.idGameGoOrFinish.text = "-finish-"
                startTimer()
                setListInAdapter()
            }else{
                goToMenu()
            }
        }

        // обработка выхода из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            goToMenu()
        }

    }


    // очистка биндинга при очистке view
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // функция установки новых чисел в списке
    private fun setListInAdapter(){
        val list = listItemsForGame.shuffled().slice(0..34).toMutableList()
        list.add(list.shuffled()[0])
        gameAdapter.setListItems(list)
    }

    // функция показа обратного отсчета
    @SuppressLint("SetTextI18n")
    private fun startTimer(){
        jobTimer = CoroutineScope(Dispatchers.Main).launch {
            for (i in startSec downTo 0 ){
                delay(1000)
                binding!!.idGameTvTimer.text = "sec: $i"
            }
            jobTimer!!.cancel()
            repository.showToast("time is up")

            goToGameOverFragment()

        }
    }

    // функция проверки ответа
    @SuppressLint("SetTextI18n")
    override fun setOutcome(outcome: String) {
        when(outcome){
            WIN -> {
                if(jobTimer!!.isActive){
                    level+=1
                    binding!!.idGameTvLevel.text = "level: $level"
                    jobTimer!!.cancel()
                    repository.showToast("RIGHT!")
                    setListInAdapter()
                    startTimer()
                }
            }
            LOSS -> {
                if(jobTimer!!.isActive){

                    // проигрыш
                    jobTimer!!.cancel()
                    repository.showToast("WRONG...")
                    goToGameOverFragment()

                }
            }
        }
    }

    // функция окончания игры и перехода на другой экран
    private fun goToGameOverFragment(){
        val bundle = Bundle()
        bundle.putInt(CURRENT_RESULT,level)
        MAIN.navController.navigate(R.id.action_gameFragment_to_gameOverFragment,bundle)
    }

    // функция перехода в меню
    private fun goToMenu(){
        if(jobTimer!=null){
            if(jobTimer!!.isActive){
                jobTimer!!.cancel()
            }
        }
        MAIN.navController.navigate(R.id.action_gameFragment_to_menuFragment)
    }

}