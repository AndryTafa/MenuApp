package com.andry .presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andry.data.models.Category
import com.andry.data.models.Customer
import com.andry.data.models.Food
import com.andry.data.models.FoodForCart
import com.andry.data.models.ingredients.IngredientsItem
import com.andry.data.repositories.categories.CategoryRepository
import com.andry.data.repositories.food.FoodRepository
import com.andry.data.repositories.ingredients.IngredientsRepository
import com.andry.utils.Resource
import com.andry.utils.ServeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientsRepository: IngredientsRepository
) : ViewModel() {


    val customers = MutableLiveData<List<Customer>>()

    private val _basket = MutableLiveData<MutableList<Food>>(mutableListOf())
    val basket: LiveData<MutableList<Food>> get() = _basket

    private var _cart = MutableStateFlow<List<FoodForCart>>(listOf())
    val cart: StateFlow<List<FoodForCart>> = _cart

    private var _categoryList = MutableStateFlow<Resource<List<Category>>>(Resource.Loading)
    val categoryList: StateFlow<Resource<List<Category>>> get() = _categoryList

    private var _foodList: List<Food> = listOf()

    private var _filteredFoodList = MutableStateFlow<Resource<List<Food>>>(Resource.Loading)
    val filteredFoodList: StateFlow<Resource<List<Food>>> get() = _filteredFoodList

    private var _ingredientsList: List<IngredientsItem> = listOf()

    private var _filteredIngredientsList = MutableStateFlow<List<IngredientsItem>>(listOf())
    private var _selectedIngredientsList = MutableStateFlow<List<IngredientsItem>>(listOf())
    val filteredIngredientsList: StateFlow<List<IngredientsItem>> get() = _filteredIngredientsList

    private var _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> get() = _selectedCategory

    private var _selectedFood = MutableStateFlow<FoodForCart?>(null)
    val selectedFood: StateFlow<FoodForCart?> get() = _selectedFood


    private val _totalCost = MutableStateFlow<Double>(0.0)
    val totalCost: StateFlow<Double> get() = _totalCost

    private var serveType: ServeTypes = ServeTypes.IN_RESTAURANT

    init {
        println("inittttttt")
        getCategoriesFromRemote(true)
        getFoodsFromRemote(true)
        getIngredientsFromRemote(true)
    }

    private fun getCategoriesFromLocale() = viewModelScope.launch {

        categoryRepository.allCategories.collect { response ->
            if (response.isEmpty()) {
//                getCategoriesFromRemote(true)
            } else {
                println("C came from locale")
                _categoryList.emit(Resource.Success(response))
                setSelectedCategory(response[0])
            }

        }
        println("asdas")
    }

    private fun getCategoriesFromRemote(isSavedToLocale: Boolean) {
        viewModelScope.launch {
            categoryRepository.getCategoriesFromDatabase().collect {
                when (it) {
                    is Resource.Loading -> {
                        _categoryList.emit(Resource.Loading)
                    }

                    is Resource.Success -> {
                        _categoryList.emit(it)
                        setSelectedCategory(it.data[0])

                        if (isSavedToLocale) {
                            viewModelScope.launch {
                                categoryRepository.insertCategoriesToLocaleDb(it.data)
                            }
                        }

                    }

                    is Resource.Failure -> {
                        _categoryList.emit(Resource.Failure(it.e))
                        getCategoriesFromLocale()
                    }

                    else -> {}
                }
            }

        }
    }

    private fun getFoodsFromLocale() = viewModelScope.launch {

        foodRepository.allFoods.collect { response ->
            if (response.isEmpty()) {
//                getFoodsFromRemote(true)
            } else {
                println("came from locale")
                _foodList = response
                _filteredFoodList.emit(Resource.Success(response.filter {
                    it.categoryId == selectedCategory.value?.categoryId
                }))

            }
        }
    }
    private fun getIngredientsFromLocale() = viewModelScope.launch {

        ingredientsRepository.allIngredients.collect { response ->
            if (response.isEmpty()) {
//                getFoodsFromRemote(true)
            } else {
                println("came from locale")
                _ingredientsList = response
                _filteredIngredientsList.emit(response)

            }
        }
    }

    private fun getFoodsFromRemote(isSavedToLocale: Boolean) {
        println("came from remote")
        viewModelScope.launch {
            foodRepository.getFoodsFromDatabase().collect {
                when (it) {
                    is Resource.Loading -> {
                        _filteredFoodList.emit(Resource.Loading)
                    }

                    is Resource.Success -> {
                        _foodList = it.data
                        _filteredFoodList.emit(Resource.Success(it.data.filter { food ->
                            food.categoryId == selectedCategory.value?.categoryId
                        }))
                        if (isSavedToLocale) {
                            viewModelScope.launch {
                                foodRepository.insertAllFoodsToLocaleDb(it.data)
                            }
                        }
                    }

                    is Resource.Failure -> {
                        _filteredFoodList.emit(Resource.Failure(it.e))
                        getFoodsFromLocale()
                    }

                    else -> {}
                }
            }

        }
    }
    private fun getIngredientsFromRemote(isSavedToLocale: Boolean) {
        println("came from remote")
        viewModelScope.launch {
            ingredientsRepository.getIngredientsFromDatabase().collect {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        _ingredientsList = it.data
                        Log.d("ingredientsList", _ingredientsList.toString())
                        _filteredIngredientsList.emit(it.data)
                        if (isSavedToLocale) {
                            viewModelScope.launch {
                                ingredientsRepository.insertAllIngredientsToLocaleDb(it.data)
                            }
                        }
                    }

                    is Resource.Failure -> {
                        _filteredIngredientsList.emit(listOf())
                        getIngredientsFromLocale()
                    }

                    else -> {}
                }
            }

        }
    }

    fun updateIngredients(ingredientsList: List<IngredientsItem>){
        Log.d("updateIngredients", ingredientsList.toString())
        _selectedIngredientsList.update { ingredientsList.filter { it.isSelected } }
    }


    fun setSelectedCategory(category: Category?) {
        viewModelScope.launch {
            _selectedCategory.emit(category)
            _filteredFoodList.update {
                Resource.Success(_foodList.filter {
                    it.categoryId == category?.categoryId
                })
            }
        }
    }

    fun addToBasket() {

        _selectedFood.update { it?.copy(ingredients = _filteredIngredientsList.value.filter { it.isSelected }) }
        viewModelScope.launch {
            _selectedFood.value?.let {food->
                if (_cart.value.contains(food)) {
                    val updatedCart = _cart.value.toMutableList()

                    val existingCartItem = updatedCart.find { it.foodId == food.foodId && it.ingredients == food.ingredients }

                    if (existingCartItem != null) {
                        existingCartItem.quantity = (existingCartItem.quantity + food.quantity)
                    }
                    _cart.emit(updatedCart)
                } else {
                    val newValue = _cart.value + listOf(food)
                    _cart.emit(newValue)
                }

                println(_cart.value)
                _totalCost.value = _totalCost.value.plus(food.price * food.quantity)

            }
        }
        _selectedFood.update { null }
        _filteredIngredientsList.update { _ingredientsList }
    }

    fun setSelectedFood(food: FoodForCart){
        _selectedFood.update { food }
    }

    fun increaseSelectedFoodQuantity(){
        _selectedFood.update { it?.copy(quantity = it.quantity + 1) }
    }

    fun decreaseSelectedFoodQuantity(closeDialog: () -> Unit){
        if ((_selectedFood.value?.quantity ?: 1) > 1){
            _selectedFood.update { it?.copy(quantity = it.quantity - 1) }
        }
        else if((_selectedFood.value?.quantity ?: 0) == 1){
            _selectedFood.value = null
            closeDialog.invoke()
        }
    }

    fun increaseCartItemQuantity(food: FoodForCart) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val updatedCart = _cart.value.toMutableList()

                val existingCartItem = updatedCart.find { it.foodId == food.foodId &&  it.ingredients == food.ingredients}

                if (existingCartItem != null) {
                    existingCartItem.quantity = (existingCartItem.quantity + 1)
                }

                _cart.emit(updatedCart)
            }

        }
        _totalCost.value = abs(_totalCost.value + food.price)

    }

    fun decreaseCartItemQuantity(food: FoodForCart) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val updatedCart = _cart.value.toMutableList()

                val existingCartItem = updatedCart.find { it.foodId == food.foodId && it.ingredients == food.ingredients}

                if (existingCartItem != null && existingCartItem.quantity > 0) {
                    existingCartItem.quantity = (existingCartItem.quantity - 1)
                    if (existingCartItem.quantity == 0){
                        deleteCartItem(existingCartItem)
                    }
                }
                _cart.emit(updatedCart)
            }

        }
        println("decreaseCartItemQuantity")
        _totalCost.value = abs(_totalCost.value - food.price)
    }

    fun deleteCartItem(food: FoodForCart) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val updatedCart = mergeFoodList(_cart.value).toMutableList()

                val existingCartItem = updatedCart.find { it.foodId == food.foodId && it.ingredients == food.ingredients }

                if (existingCartItem != null) {
                    updatedCart.remove(food)
                }
                _cart.emit(updatedCart)
            }
        }
        _totalCost.value = abs((_totalCost.value - (food.price * food.quantity)))
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

    fun setServeType(serveTypes: ServeTypes){
        serveType = serveTypes
    }


    fun calculateTotalPrice(): Double {
        var totalPrice = 0.0

        for (food in _cart.value) {
            totalPrice += food.quantity * food.price
        }

        return abs(totalPrice)
    }

    fun filterIngredientsList(foodId:Int){
        val list = _ingredientsList.filter { it.foodId == foodId }.map{
            it.copy(isSelected = true)
        }

        _filteredIngredientsList.update { list }
        _selectedIngredientsList.update { list }
    }


    fun clearBasket() {
        _cart.value = listOf()
        _totalCost.value = 0.0
    }
}