package com.bigOne.stayfittrainer.ui.Recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentRecipeDetailsBinding
import com.bumptech.glide.Glide


class RecipeDetailsFragment : Fragment() {
    lateinit var binding: FragmentRecipeDetailsBinding
    private  val  RecipeViewModel: RecipeViewModel by activityViewModels()

 private  val args:RecipeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()


    }

    private fun observer() {

        RecipeViewModel.getRecipeDetails(args.recipeId).observe(viewLifecycleOwner){
            Log.e("selectedRecipe",it.toString())
            binding.apply {
                recipeTextView.text=it?.recipe_name
                recipeDescription.text=it?.recipe_description
                val foodNames = it?.ingredients?.ingredient?.joinToString("\n  \n") { "${it.food_name} (${it.ingredient_description})" }
                time.text="${it?.preparation_time_min} min"
                ingrTextView.text= foodNames.toString()
                val directionDescriptions = it?.directions?.direction?.joinToString("\n\n") { "${it.direction_number}. ${it.direction_description}" }
                directionText.text=directionDescriptions

                val servingFields = it?.serving_sizes?.serving?.javaClass?.declaredFields
                val sb = StringBuilder()
                if (servingFields != null) {
                    for (field in servingFields) {
                        field.isAccessible = true
                        val value = field.get(it?.serving_sizes.serving).toString().toDoubleOrNull()
                        if (value != null && value != 0.0) {
                            sb.append("${field.name}: ${field.get(it?.serving_sizes.serving)}\n\n")
                        }
                    }
                }
                servingText.text = sb.toString()

                Glide.with(requireContext())
                    .load(it?.recipe_images?.recipe_image?.get(0))
                    .placeholder(
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_food)!!
                    )
                    .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_food)!!)
                    .into(recipeImageView)

            }

        }
    }
}


