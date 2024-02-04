package com.example.testappforevamobi.model.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testappforevamobi.R
import com.example.testappforevamobi.model.constant.LOSS
import com.example.testappforevamobi.model.constant.WIN

class GameAdapter(private val interfaceAdapter:InterfaceForAdapter,private val context:Context):RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View):RecyclerView.ViewHolder(view)

    private var listItems = mutableListOf<Int>()
    private var countTouch = 0

    private var item_1 = 0
    private var item_2 = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv,parent,false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {

        val textView = holder.itemView.findViewById<TextView>(R.id.id_item_rv_tv)
        val constraintLayout = holder.itemView.findViewById<ConstraintLayout>(R.id.id_item_rv_cl)

        textView.text = listItems[position].toString()
        constraintLayout.setBackgroundResource(R.drawable.background_item_rv)

    }

    override fun onViewAttachedToWindow(holder: GameViewHolder) {
        super.onViewAttachedToWindow(holder)

        val textView = holder.itemView.findViewById<TextView>(R.id.id_item_rv_tv)
        val constraintLayout = holder.itemView.findViewById<ConstraintLayout>(R.id.id_item_rv_cl)

        holder.itemView.setOnClickListener {
            countTouch+=1
            if(countTouch == 1){
                item_1 = textView.text.toString().toInt()
                constraintLayout.setBackgroundResource(R.drawable.background_item_rv_pressed)
            }else{
                if(countTouch == 2){
                    item_2 = textView.text.toString().toInt()
                    constraintLayout.setBackgroundResource(R.drawable.background_item_rv_pressed)

                    if(item_1 == item_2){
                        interfaceAdapter.setOutcome(WIN)
                    }else{
                        interfaceAdapter.setOutcome(LOSS)
                    }

                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItems(list:MutableList<Int>){
        listItems = list
        countTouch = 0
        item_1 = 0
        item_2 = 0
        notifyDataSetChanged()
    }

}