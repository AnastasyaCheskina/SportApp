package com.example.sportapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportapp.Model.Workout
import com.example.sportapp.R
import com.example.sportapp.databinding.WorkoutItemBinding

class AdapterWorkout(private val listener: AdapterWorkout.Listener): RecyclerView.Adapter<AdapterWorkout.ItemsHolder>() {
    private var itemsList = ArrayList<Workout>()
    class ItemsHolder (item: View): RecyclerView.ViewHolder(item) {
        private val binding = WorkoutItemBinding.bind(item)
        fun bind(result: Workout, listener: Listener) = with(binding)
        {
            nameWorkout.text = result.name
            if (result.durationInSec >= 60) {
                min.text = (result.durationInSec / 60).toString()
                sec.text = (result.durationInSec % 60).toString()
            }
            else {
                sec.text = result.durationInSec.toString()
                min.text = "00"
            }
            item.setOnClickListener()
            {
                listener.OnClick(result)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item,parent,false)
        return ItemsHolder(view)
    }
    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        holder.bind(itemsList[position],listener)
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
    fun addResult(item: Workout)
    {
        itemsList.add(item)
    }
    fun deleteItem(item: Workout) {
        if(itemsList.contains(item)) itemsList.remove(item)
    }
    interface Listener
    {
        fun OnClick(result: Workout)
    }
}