package com.bigOne.trainerstayfit.ui.trainer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.trainerstayfit.R
import com.bigOne.trainerstayfit.databinding.ListWorkoutItemBinding
import com.bigOne.trainerstayfit.datas.model.WorkoutData

class WorkOutAdapter(var mList: MutableList<WorkoutData>, private  val context: Context) : RecyclerView.Adapter<WorkOutAdapter.WorkoutViewHolder>() {
    inner class WorkoutViewHolder(private val binding: ListWorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: WorkoutData) {
            binding.apply {
                item.let { it ->
                    workoutTitle.text = it.name
                    timeTitleText.text = it.time
                    calorieText.text = it.calorie
                    difficulty.text =it.difficulty
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = DataBindingUtil.inflate<ListWorkoutItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_workout_item,
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setItems(newItems: List<WorkoutData>) {
        mList.clear()
        mList.addAll(newItems)
        Log.e("query Adapter", mList.toString())
        notifyDataSetChanged()
    }

}