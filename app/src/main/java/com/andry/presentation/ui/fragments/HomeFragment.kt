package com.andry.presentation.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentFirstNewBinding
import com.andry.adapter.CategoryAdapter
import com.andry.adapter.CategoryItemDecoration
import com.andry.adapter.FoodAdapter
import com.andry.adapter.FoodItemDecoration
import com.andry.data.models.FoodForCart
import com.andry.presentation.viewmodels.SharedViewModel
import com.andry.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentFirstNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BasketFragment
    private lateinit var ingredientsFragment: IngredientsFragment
    private lateinit var paymentDialog: PaymentFragment
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var foodAdapter: FoodAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        listen()
        setUi()
    }

    private fun setUi() {
        categoryAdapter = CategoryAdapter {
            viewModel.setSelectedCategory(it)
        }
        foodAdapter = FoodAdapter {
            lifecycleScope.launch {
                viewModel.setSelectedFood(
                    FoodForCart(
                        it.foodId, it.categoryId, it.name, it.price, it.image, 1
                    )
                )
                viewModel.filterIngredientsList(it.foodId)
                openChooseIngredientsFragment()


            }

        }
        binding.rvCategories.addItemDecoration(CategoryItemDecoration(requireContext()))
        binding.rvFoods.addItemDecoration(FoodItemDecoration(requireContext()))

        binding.rvCategories.adapter = categoryAdapter
        binding.rvFoods.adapter = foodAdapter

    }

    private fun listen() {
        binding.ivShoppingCartButton.setOnClickListener {
            if (viewModel.cart.value.isEmpty()) {
                showNoItemDialog()
            } else {
                openBasketFragment()
            }
        }

        binding.tvPayButton.setOnClickListener {
            if (viewModel.cart.value.isEmpty()) {
                showNoItemDialog()
            } else {
                openBasketFragment()
            }
        }
    }

    private fun goToFinalScreen() {
        paymentDialog.dismiss()
        viewModel.clearBasket()
        findNavController().navigate(R.id.action_firstFragmentNew_to_finalFragment)
    }

    private fun openBasketFragment() {
        dialog = BasketFragment({
            dialog.dismiss()
        }, {
            openPaymentDialog()
        })
        dialog.show(this.parentFragmentManager, "tag")

    }

    private fun openChooseIngredientsFragment() {
        ingredientsFragment = IngredientsFragment {
            ingredientsFragment.dismiss()
        }
        ingredientsFragment.show(this.parentFragmentManager, "tag")

    }

    private fun openPaymentDialog() {
        paymentDialog = PaymentFragment({
            paymentDialog.dismiss()
        }, {
            goToFinalScreen()
        }, {
            goToFinalScreen()
        })
        dialog.dismiss()
        paymentDialog.show(this.parentFragmentManager, "tag")

    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.categoryList.collect {
                    when (it) {
                        is Resource.Success -> {
                            categoryAdapter.setCategoryList(it.data)
                        }

                        else -> {}
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.filteredFoodList.collect {
                    when (it) {

                        is Resource.Success -> {
                            foodAdapter.setFoodList(it.data)
                        }

                        else -> {}
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.cart.collect {
                    foodAdapter.setCart(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.selectedCategory.collect {
                    binding.tvCategoryName.text = it?.name
                    categoryAdapter.setSelectedCategory(it?.categoryId ?: 1)
                    categoryAdapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.totalCost.collect {
                    binding.tvTotalPrice.text = "$${String.format("%.2f", it)}"
                }
            }
        }


    }

    private fun showNoItemDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_no_item)

        val yesBtn = dialog.findViewById(R.id.btnOkay) as AppCompatButton
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

