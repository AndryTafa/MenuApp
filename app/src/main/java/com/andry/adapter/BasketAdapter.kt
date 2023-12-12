package com.andry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.ItemCartFoodBinding
import com.andry.RestaurantKiosk.databinding.ItemCartFoodSummaryBinding
import com.andry.data.models.FoodForCart
import com.andry.data.models.ingredients.IngredientsItem
import com.andry.utils.CustomizeCartViewTypeEnum
import com.bumptech.glide.Glide

class BasketAdapter(
    private val onPlusItemClick: (food: FoodForCart) -> Unit,
    private val onMinusItemClick: (food: FoodForCart) -> Unit,
    private val onDeleteItemClick: (food: FoodForCart) -> Unit
) : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    private var foodList = ArrayList<FoodForCart>()
    private var viewType: CustomizeCartViewTypeEnum =
        CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItems(food: FoodForCart) {
            if (viewType == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD) {
                (binding as ItemCartFoodBinding)
                Glide.with(itemView.context).load(R.drawable.food).error(R.drawable.food)
                    .into(binding.ivFoodImage)
                binding.tvFoodName.text = food.name
                binding.tvFoodPrice.text = "$${food.price}"
                binding.tvQuantity.text = food.quantity.toString()
                binding.ivAddButton.setOnClickListener {
                    onPlusItemClick.invoke(food)
                }
                binding.ivRemoveButton.setOnClickListener {
                    onMinusItemClick.invoke(food)
                }
                binding.ivCancel.setOnClickListener {
                    onDeleteItemClick.invoke(food)
                }
            } else {
                (binding as ItemCartFoodSummaryBinding)
                binding.tvFoodPrice.text = "$${("%.2f".format(food.price * food.quantity))}"
                binding.tvFoodQuantity.text = "x${food.quantity}"
                binding.tvFoodName.text = food.name

            }
        }

        override fun onClick(p0: View?) {

        }

    }


    fun setFoodList(list: List<FoodForCart>) {
        foodList.clear()
        foodList.addAll(mergeFoodList(list))
        notifyDataSetChanged()
    }

    private fun mergeFoodList(foodList: List<FoodForCart>): List<FoodForCart> {
        val foodMap = mutableMapOf<Pair<Int, List<IngredientsItem>>, FoodForCart>()

        for (food in foodList) {
            val existingFood = foodMap[Pair(food.foodId, food.ingredients)]
            if (existingFood != null) {
                val updatedFood =
                    existingFood.copy(quantity = existingFood.quantity + food.quantity)
                foodMap[Pair(food.foodId, food.ingredients)] = updatedFood
            } else {
                foodMap[Pair(food.foodId, food.ingredients)] = food
            }
        }

        return foodMap.values.toList()
    }


    fun setViewType(type: CustomizeCartViewTypeEnum) {
        viewType = type
        notifyDataSetChanged()
    }
    fun getViewType(): CustomizeCartViewTypeEnum {
        return viewType
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder
        val inflater = LayoutInflater.from(parent.context)

        viewHolder = if (viewType == CustomizeCartViewTypeEnum.CUSTOMIZE_CART_WITH_FOOD.ordinal) {
            ViewHolder(ItemCartFoodBinding.inflate(inflater, parent, false))
        } else {
            ViewHolder(ItemCartFoodSummaryBinding.inflate(inflater, parent, false))

        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(foodList[position])
    }

}