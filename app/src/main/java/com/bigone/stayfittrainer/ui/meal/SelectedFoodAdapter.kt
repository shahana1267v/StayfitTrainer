package com.bigOne.stayfittrainer.ui.meal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.stayfittrainer.R

import com.bigOne.stayfittrainer.databinding.ListSelectedItemBinding
import com.bigOne.stayfittrainer.datas.model.SavedFood


class SelectedFoodAdapter(var mList: MutableList<SavedFood>, private  val context: Context) : RecyclerView.Adapter<SelectedFoodAdapter.SelectedFoodViewHolder>() {
    var onitemClickListner: OnItemClickListener? = null
    inner class SelectedFoodViewHolder(private val binding: ListSelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SavedFood) {
            binding.apply {
                titleTv.text = item.name
                countText.text = item.selectedQuantity
                unitText.text =item.selectedUnit
                calorieValue.text = (item.selectedQuantity.toDouble() * item.calorie).toInt().toString()
                deleteBtn.setOnClickListener {
                     onitemClickListner?.ondelete(item)
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFoodViewHolder {
        val binding = DataBindingUtil.inflate<ListSelectedItemBinding>(
            LayoutInflater.from(parent.context), R.layout.list_selected_item, parent, false)
        return SelectedFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedFoodViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setItems(newItems: List<SavedFood>) {
        mList.clear()
        mList.addAll(newItems)
        Log.e("query Adapter", mList.toString())
        notifyDataSetChanged()
    }

    fun clearItems() {
        mList.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
      // fun onClick(item: SavedFood)
        fun ondelete(item: SavedFood)
    }
}