package com.bigOne.stayfittrainer.ui.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.ListEachcourseItemBinding
import com.bigOne.stayfittrainer.datas.model.CourseData
import com.bumptech.glide.Glide

class CourseAdapter(var mList: MutableList<CourseData>, private  val context: Context) : RecyclerView.Adapter<CourseAdapter.WorkoutViewHolder>() {
    var onitemClickListner: OnItemClickListener? = null
    inner class WorkoutViewHolder(private val binding: ListEachcourseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: CourseData) {
            binding.apply {
                item.let { it ->
                    courseTitle.text = it.couresName
                    timeTitleText.text = it.totalTime
                    difficulty.text = it.difficulty
                    createdBy.text =it.trainerName
                    courseDescrp.text =it.description
                    val randomParameter = (1..1000).random() // Generate a random number between 1 and 1000
                    val imageUrl = "https://source.unsplash.com/random/100x100/?gym&$randomParameter"
                    Glide.with(context)
                        .load(imageUrl)
                        .error(R.drawable.ic_placeholder)
                        .placeholder(R.drawable.ic_placeholder)
                        .centerCrop()
                        .into(binding.appCompatImageView);

                }
                binding.courseCard.setOnClickListener {
                    onitemClickListner?.onClick(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = DataBindingUtil.inflate<ListEachcourseItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_eachcourse_item,
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


    fun setItems(newItems: List<CourseData>) {
        mList.clear()
        mList.addAll(newItems)
        Log.e("query Adapter", mList.toString())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(item: CourseData)
    }

}