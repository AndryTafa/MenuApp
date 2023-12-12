package com.andry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.ItemFoodBinding
import com.andry.data.models.Food
import com.andry.data.models.FoodForCart
import com.bumptech.glide.Glide

class FoodAdapter(
    private val onItemClick: (food: Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private var foodList = ArrayList<Food>()
    private var cart = ArrayList<FoodForCart>()

    inner class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItems(food: Food) {
            Glide.with(itemView.context).load(R.drawable.food).error(R.drawable.food)
                .into(binding.ivFoodImage)
            binding.tvFoodName.text = food.name
            binding.tvFoodPrice.text = "$${food.price}"
            binding.clOuter.setOnClickListener {
                onItemClick.invoke(food)
            }
        }

        override fun onClick(p0: View?) {

        }

    }

    fun setFoodList(list: List<Food>) {
        foodList.clear()
        foodList.addAll(list)
        notifyDataSetChanged()
    }

    fun setCart(list: List<FoodForCart>) {
        cart.clear()
        cart.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemFoodBinding =
            ItemFoodBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(foodList[position])
    }


}