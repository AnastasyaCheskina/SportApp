package com.example.sportapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportapp.Model.Training
import com.example.sportapp.R
import com.example.sportapp.databinding.TrainingItemBinding

class AdapterTrainings(private val listener: AdapterTrainings.Listener): RecyclerView.Adapter<AdapterTrainings.ItemsHolder>() {
    private var itemsList = ArrayList<Training>()
    class ItemsHolder (item: View): RecyclerView.ViewHolder(item) {
        private val binding = TrainingItemBinding.bind(item)
        fun bind(result: Training, listener: Listener) = with(binding)
        {
            nameTraining.text = result.name
            duration.text = result.durationInMin.toString()
            btnNext.setOnClickListener()
            {
                listener.OnClick(result)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_item,parent,false)
        return ItemsHolder(view)
    }
    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        holder.bind(itemsList[position],listener)
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
    fun addResult(item: Training)
    {
        itemsList.add(item)
    }
    fun deleteItem(item: Training) {
        if(itemsList.contains(item)) itemsList.remove(item)
    }
    interface Listener
    {
        fun OnClick(result: Training)
    }
}