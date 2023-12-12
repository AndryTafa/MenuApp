package com.andry.presentation.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentPaymentTypeBinding
import com.andry.adapter.BasketAdapter
import com.andry.data.models.FoodForCart
import com.andry.presentation.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class PaymentFragment(
    private val onCancelClick: () -> Unit,
    private val onCardClicked: () -> Unit,
    private val onCashClicked: () -> Unit,
) : BottomSheetDialogFragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentPaymentTypeBinding
    private var mBottomSheetListener: CardDialogListener? = null
    private lateinit var basketAdapter: BasketAdapter


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.4f)
            /** Set dim amount here (the dimming factor of the parent fragment) */

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPaymentTypeBinding.bind(
            inflater.inflate(
                R.layout.fragment_payment_type,
                container
            )
        )
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

        observe()
        listen()


    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                println("sadasadsasd")
                viewModel.cart.collect {
                    println("cart collected")
                    basketAdapter.setFoodList(it)
                    basketAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateUi() {
    }

    private fun listen() {
        binding.llCard.setOnClickListener {
            onCardClicked.invoke()
        }
        binding.llCash.setOnClickListener {
            onCashClicked.invoke()
        }

        binding.tvCancel.setOnClickListener {
            onCancelClick.invoke()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /** attach listener from parent fragment */
        try {
            mBottomSheetListener = context as CardDialogListener?
        } catch (e: ClassCastException) {
        }
    }

    interface CardDialogListener {
        fun chooseCardClick(foodForCart: FoodForCart)
        fun newCardClick()
    }
}