package com.bigOne.stayfittrainer.ui.mycourse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.ListWorkoutItemBinding
import com.bigOne.stayfittrainer.datas.model.WorkoutData


class WorkOutCourseAdapter(var mList: MutableList<WorkoutData>, private  val context: Context) : RecyclerView.Adapter<WorkOutCourseAdapter.WorkoutViewHolder>() {
    var onitemClickListner: OnItemClickListener? = null
    inner class WorkoutViewHolder(private val binding: ListWorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: WorkoutData) {
            binding.apply {
                item.let { it ->
                    workoutTitle.text = it.name
                    timeTitleText.text = it.time
                    difficulty.text = it.difficulty
                    workoutCard.setOnClickListener {
                        onitemClickListner?.onClick(item)
                    }

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

    interface OnItemClickListener {
        fun onClick(item: WorkoutData)
    }

}