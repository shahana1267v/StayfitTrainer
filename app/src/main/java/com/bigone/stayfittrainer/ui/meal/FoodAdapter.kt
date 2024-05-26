package com.bigOne.stayfittrainer.ui.meal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.ListEachItemBinding
import com.bigOne.stayfittrainer.datas.model.FoodFactResponse

class FoodAdapter(var mList: MutableList<FoodFactResponse>, private  val context: Context) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    var onitemClickListner: OnItemClickListener? = null
    inner class FoodViewHolder(private val binding: ListEachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: FoodFactResponse) {
            binding.apply {
                titleTv.text = item.name
                calorieText.text = item.description
                foodcard.setOnClickListener {
                    onitemClickListner?.onClick(item)
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = DataBindingUtil.inflate<ListEachItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_each_item,
            parent,
            false
        )
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setItems(newItems: List<FoodFactResponse>) {
        mList.clear()
        mList.addAll(newItems)
        Log.e("query Adapter", mList.toString())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(item: FoodFactResponse)
    }
}