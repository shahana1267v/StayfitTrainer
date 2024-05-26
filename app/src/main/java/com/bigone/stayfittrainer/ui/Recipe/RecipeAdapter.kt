package com.bigOne.stayfittrainer.ui.Recipe

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.ListRecipeItemBinding
import com.bigOne.stayfittrainer.datas.model.Recipe
import com.bumptech.glide.Glide

class RecipeAdapter(var mList: MutableList<Recipe>, private  val context: Context) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    var onitemClickListner: OnItemClickListener? = null

    inner class RecipeViewHolder(private val binding: ListRecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Recipe) {
            binding.apply {
                recipeName.text = item.recipe_name
                recipeDescription.text = item.recipe_description
                calorie.text = item.recipe_nutrition.calories
                carb.text = item.recipe_nutrition.carbohydrate
                pro.text = item.recipe_nutrition.protein
                fat.text = item.recipe_nutrition.fat
                recipecard.setOnClickListener {
                    onitemClickListner?.onClick(item)

                }
                Glide.with(context)
                    .load(item.recipe_image)
                    .placeholder(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_food
                        )!!
                    )
                    .error(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_food
                        )!!
                    )
                    .into(binding.recipeView)

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = DataBindingUtil.inflate<ListRecipeItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_recipe_item,
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setItems(newItems: List<Recipe>?) {
        mList.clear()
        if (newItems != null)
            mList.addAll(newItems)
        Log.e("query Adapter", mList.toString())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(item: Recipe)
    }
}
