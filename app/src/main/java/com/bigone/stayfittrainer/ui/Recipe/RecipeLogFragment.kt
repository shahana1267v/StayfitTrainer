package com.bigOne.stayfittrainer.ui.Recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.Recipe


class RecipeLogFragment : Fragment() {
    private val recipeViewModel: RecipeViewModel by activityViewModels()





lateinit var binding: com.bigOne.stayfittrainer.databinding.FragmentRecipeLogBinding
    private lateinit var mAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_log, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
        observe()

    }


    private fun initAdapter() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            mAdapter= RecipeAdapter(mutableListOf(),requireContext())
            recyclerView.adapter = mAdapter
        }
        mAdapter.onitemClickListner = object : RecipeAdapter.OnItemClickListener {
            override fun onClick(item: Recipe) {
                mAdapter.setItems(mutableListOf())
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()

                val action = RecipeLogFragmentDirections.actionRecipeLogFragmentToRecipeDetailsFragment(item.recipe_id)
                findNavController().navigate(action)
            }

        }
    }

    private fun observe() {
        recipeViewModel.searchRecipe(null).observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                mAdapter.setItems(it as List<Recipe>?)
            }
        }

    }



    private fun init(){
    binding.recipeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null && query.length > 2) {
                mAdapter.setItems(mutableListOf())
                recipeViewModel.searchRecipe(query).observe(viewLifecycleOwner) {
                }
            } else {
                mAdapter.setItems(mutableListOf())
            }
            return true
        }


        override fun onQueryTextChange(newText: String?): Boolean {
            return  false
        }

    })
        binding.recipeToolbar.title = "Recipes"


    }

}


