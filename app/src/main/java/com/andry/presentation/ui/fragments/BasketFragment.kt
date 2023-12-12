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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentBasketBinding
import com.andry.adapter.BasketAdapter
import com.andry.presentation.viewmodels.SharedViewModel
import com.andry.utils.CustomizeCartViewTypeEnum
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BasketFragment(
    private val onCancelClick: () -> Unit,
    private val openPaymentTypeDialog: () -> Unit,

    ) : BottomSheetDialogFragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentBasketBinding
    private lateinit var basketAdapter: BasketAdapter
    private var customizeCartViewTypeEnum: CustomizeCartViewTypeEnum =
        CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.4f)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBasketBinding.bind(inflater.inflate(R.layout.fragment_basket, container))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basketAdapter = BasketAdapter({
            viewModel.increaseCartItemQuantity(it)
            observe()

        }, {
            viewModel.decreaseCartItemQuantity(it)
            observe()

        }, {
            viewModel.deleteCartItem(it)
            observe()
        })

        binding.rvFoods.adapter = basketAdapter
        observe()
        listen()


    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.cart.collect {
                    basketAdapter.setFoodList(it)
                    basketAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateUi() {
        if (customizeCartViewTypeEnum == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD) {
            binding.tvProceedToPayment.text = "Proceed to Payment"
            binding.tvPreparationTime.visibility = View.GONE
            binding.tvTotal.visibility = View.GONE
            binding.tvSummaryText.visibility = View.GONE
            binding.viewTotal.visibility = View.GONE
            binding.tvPreparationTimeInfo.visibility = View.GONE
            binding.tvTotalPrice.visibility = View.GONE

        } else if (customizeCartViewTypeEnum == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_PRICE) {
            binding.tvProceedToPayment.text = "Confirm"
            binding.tvPreparationTime.visibility = View.VISIBLE
            binding.tvTotal.visibility = View.VISIBLE
            binding.tvSummaryText.visibility = View.VISIBLE
            binding.viewTotal.visibility = View.VISIBLE
            binding.tvPreparationTimeInfo.visibility = View.VISIBLE
            binding.tvTotalPrice.visibility = View.VISIBLE
            binding.tvTotalPrice.text = "$${String.format("%.2f", viewModel.calculateTotalPrice())}"
        } else {

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


    private fun listen() {
        binding.tvProceedToPayment.setOnClickListener {
            if (viewModel.cart.value.isEmpty()){
                showNoItemDialog()
            }
            else{
                if (basketAdapter.getViewType() == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_PRICE) {
                    openPaymentTypeDialog.invoke()
                } else {
                    basketAdapter.setViewType(CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_PRICE)
                    customizeCartViewTypeEnum = CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_PRICE
                    updateUi()
                }
            }
        }
        binding.tvBackToHome.setOnClickListener {
            if (customizeCartViewTypeEnum == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_PRICE) {
                basketAdapter.setViewType(CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD)
                customizeCartViewTypeEnum = CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD
                updateUi()
            } else {
                onCancelClick.invoke()
            }
        }
    }

}