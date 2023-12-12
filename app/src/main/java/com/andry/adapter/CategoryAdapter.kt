package com.andry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.ItemCategoryBinding
import com.andry.data.models.Category
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val onItemClick: (category: Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList = ArrayList<Category>()
    private var selectedCategoryId: Int? = 0

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(category: Category) {
            Glide.with(itemView.context).load(R.drawable.food).error(R.drawable.food)
                .into(binding.ivFood)
            binding.tvFood.text = category.name
            if (category.categoryId == selectedCategoryId) {
                binding.ll.background =
                    itemView.context.resources.getDrawable(R.drawable.bg_rounded)
                binding.tvFood.setTextColor(itemView.context.resources.getColor(R.color.white))
            } else {
                binding.ll.setBackgroundResource(0)
                binding.tvFood.setTextColor(itemView.context.resources.getColor(R.color.black))

            }

            binding.ll.setOnClickListener {
                onItemClick.invoke(category)
            }
        }


    }

    fun setCategoryList(list: List<Category>) {
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }

    fun setSelectedCategory(id: Int) {
        selectedCategoryId = id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryBinding =
            ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(categoryList[position])

    }

}