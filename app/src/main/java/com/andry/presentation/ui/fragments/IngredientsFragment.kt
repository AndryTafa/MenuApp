package com.andry.presentation.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentChooseIngredientsBinding
import com.andry.adapter.IngredientsAdapter
import com.andry.presentation.viewmodels.SharedViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class IngredientsFragment(
    private val openPaymentTypeDialog: () -> Unit,

    ) : BottomSheetDialogFragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentChooseIngredientsBinding
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.4f)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChooseIngredientsBinding.bind(
            inflater.inflate(
                R.layout.fragment_choose_ingredients,
                container
            )
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientsAdapter = IngredientsAdapter {
            viewModel.updateIngredients(it)
            observe()
        }

        binding.rvIngredients.adapter = ingredientsAdapter
        observe()
        listen()


    }


    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.filteredIngredientsList.collect {
                    ingredientsAdapter.setIngredientsList(it)
                    ingredientsAdapter.notifyDataSetChanged()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.selectedFood.collect {
                    it?.let {
                        binding.tvFoodName.text = it.name
                        binding.tvFoodPrice.text = "$" + it.price.toString()
                        binding.tvQuantity.text = it.quantity.toString()
                        Glide.with(requireContext()).load(it.image).error(R.drawable.food)
                            .into(binding.ivFoodImage)
                    }
                }
            }
        }
    }

    private fun listen() {

        binding.tvNext.setOnClickListener {
            viewModel.addToBasket()
            openPaymentTypeDialog.invoke()
        }
        binding.ivAddButton.setOnClickListener {
            viewModel.increaseSelectedFoodQuantity()
        }
        binding.ivRemoveButton.setOnClickListener {
            viewModel.decreaseSelectedFoodQuantity {
                openPaymentTypeDialog.invoke()
            }
        }
    }

}