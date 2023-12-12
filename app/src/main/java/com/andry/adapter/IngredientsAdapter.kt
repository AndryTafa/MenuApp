package com.andry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.ItemIngredientsBinding
import com.andry.data.models.ingredients.IngredientsItem

class IngredientsAdapter(
    private val onItemClick: (ingredientsItemList: List<IngredientsItem>) -> Unit
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredientsList = ArrayList<IngredientsItem>()
    private var selectedCategoryId: Int? = 0

    inner class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(ingredientsItem: IngredientsItem) {
            binding.tvFoodPrice.text = ingredientsItem.name

            if (ingredientsItem.isSelected) {
                binding.tvFoodPrice.background =
                    itemView.context.resources.getDrawable(R.drawable.bg_selected_ingredient)
                binding.tvFoodPrice.setTextColor(itemView.context.resources.getColor(R.color.black))
                binding.ivTick.visibility = View.VISIBLE
            } else {
                binding.tvFoodPrice.background =
                    itemView.context.resources.getDrawable(R.drawable.bg_ingredient)
                binding.tvFoodPrice.setTextColor(itemView.context.resources.getColor(R.color.prepartion_color))
                binding.ivTick.visibility = View.GONE


            }

            binding.tvFoodPrice.setOnClickListener {
                ingredientsList.find { ingredientsItem.id == it.id }?.isSelected =
                    ingredientsList.find { ingredientsItem.id == it.id }?.isSelected != true
                notifyDataSetChanged()
                onItemClick.invoke(ingredientsList)
            }
        }


    }

    fun setIngredientsList(list: List<IngredientsItem>) {
        ingredientsList.clear()
        ingredientsList.addAll(list)
        notifyDataSetChanged()
    }

    fun setSelectedCategory(id: Int) {
        selectedCategoryId = id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemIngredientsBinding =
            ItemIngredientsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(ingredientsList[position])

    }

}