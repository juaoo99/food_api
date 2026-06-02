package com.example.data.repository

import com.example.data.api.RetrofitInstance
import com.example.domain.model.DailyNutrition
import com.example.domain.model.MealPlan
import com.example.domain.model.Recipe
import com.example.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.math.absoluteValue

class NutritionRepositoryImpl : NutritionRepository {

    private val initialNutrition = DailyNutrition(
        eatenKcal = 3143,
        burnedKcal = 650,
        targetKcal = 3337, // 3143 - 650 = 2493 net, target 3337 (456 over target net, or layout showing 456 kcal over)
        carbsEatenGrams = 271,
        carbsTargetGrams = 359,
        proteinEatenGrams = 202,
        proteinTargetGrams = 143,
        fatEatenGrams = 169,
        fatTargetGrams = 359
    )

    private val mockRecipes = listOf(
        Recipe(
            name = "Hard-Boiled Egg & Oatmeal",
            description = "A breakfast staple packed with clean morning energy and complex carbohydrates.",
            category = "Breakfast",
            calories = 945,
            imageDescription = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
            ingredients = listOf("2 Free-Range Eggs", "80g Whole Oats", "200ml Almond Milk", "1tbsp Chia Seeds", "Half Banana"),
            prepTimeMin = 10
        ),
        Recipe(
            name = "Healthy Ice Cream Yogurt",
            description = "High-protein dessert substitute with greek yogurt and fresh mix berries.",
            category = "Breakfast",
            calories = 280,
            imageDescription = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
            ingredients = listOf("200g Greek Yogurt 0%", "100g Mixed Organic Berries", "1tsp Stevia sweetener", "Organic Mint"),
            prepTimeMin = 5
        ),
        Recipe(
            name = "Avocado Toast & Smoked Salmon",
            description = "Sourdough toast loaded with monounsaturated healthy fats and lean pristine protein.",
            category = "Breakfast",
            calories = 420,
            imageDescription = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
            ingredients = listOf("1 Slice Rye Sourdough", "Half Hass Avocado", "50g Smoked Salmon", "Lemon juice", "Chilli flakes"),
            prepTimeMin = 8,
            isFavorite = true
        )
    )

    private val initialMealPlans = listOf(
        MealPlan(
            mealName = "Greek Yogurt w/ Berries & Cocoa",
            category = "Breakfast",
            calories = 280,
            date = "Today",
            notes = "Loaded with antioxidants and probiotic cultures"
        ),
        MealPlan(
            mealName = "Hard-Boiled Egg & Oatmeal",
            category = "Breakfast",
            calories = 945,
            date = "Today",
            notes = "Standard muscle building breakfast base"
        )
    )

    private val _nutritionState = MutableStateFlow(initialNutrition)
    private val _mealPlansState = MutableStateFlow(initialMealPlans)
    private val _recipesState = MutableStateFlow(mockRecipes)

    override fun getDailyNutrition(): Flow<DailyNutrition> = _nutritionState.asStateFlow()

    override fun getMealPlans(): Flow<List<MealPlan>> = _mealPlansState.asStateFlow()

    override fun getRecipes(): Flow<List<Recipe>> = _recipesState.asStateFlow()

    override suspend fun addMealPlan(mealPlan: MealPlan) {
        val currentList = _mealPlansState.value.toMutableList()
        currentList.add(mealPlan)
        _mealPlansState.value = currentList
        
        // When we add a meal plan, let's also add its calories to our daily eaten nutrition
        // estimating proportional macros for simulation:
        val addedKcal = mealPlan.calories
        val estCarbs = (addedKcal * 0.4f / 4f).toInt()
        val estProtein = (addedKcal * 0.3f / 4f).toInt()
        val estFat = (addedKcal * 0.3f / 9f).toInt()
        
        addEatenCalories(addedKcal, estCarbs, estProtein, estFat)
    }

    override suspend fun removeMealPlan(mealId: String) {
        val currentList = _mealPlansState.value.toMutableList()
        val foundMeal = currentList.find { it.id == mealId }
        if (foundMeal != null) {
            currentList.remove(foundMeal)
            _mealPlansState.value = currentList
            
            // Deduct the calories and macros
            val dedKcal = foundMeal.calories
            val dedCarbs = (dedKcal * 0.4f / 4f).toInt()
            val dedProtein = (dedKcal * 0.3f / 4f).toInt()
            val dedFat = (dedKcal * 0.3f / 9f).toInt()
            
            val currentNu = _nutritionState.value
            _nutritionState.value = DailyNutrition(
                eatenKcal = (currentNu.eatenKcal - dedKcal).coerceAtLeast(0),
                burnedKcal = currentNu.burnedKcal,
                targetKcal = currentNu.targetKcal,
                carbsEatenGrams = (currentNu.carbsEatenGrams - dedCarbs).coerceAtLeast(0),
                carbsTargetGrams = currentNu.carbsTargetGrams,
                proteinEatenGrams = (currentNu.proteinEatenGrams - dedProtein).coerceAtLeast(0),
                proteinTargetGrams = currentNu.proteinTargetGrams,
                fatEatenGrams = (currentNu.fatEatenGrams - dedFat).coerceAtLeast(0),
                fatTargetGrams = currentNu.fatTargetGrams
            )
        }
    }

    override suspend fun addEatenCalories(calories: Int, carbs: Int, protein: Int, fat: Int) {
        val current = _nutritionState.value
        _nutritionState.value = DailyNutrition(
            eatenKcal = current.eatenKcal + calories,
            burnedKcal = current.burnedKcal,
            targetKcal = current.targetKcal,
            carbsEatenGrams = current.carbsEatenGrams + carbs,
            carbsTargetGrams = current.carbsTargetGrams,
            proteinEatenGrams = current.proteinEatenGrams + protein,
            proteinTargetGrams = current.proteinTargetGrams,
            fatEatenGrams = current.fatEatenGrams + fat,
            fatTargetGrams = current.fatTargetGrams
        )
    }

    override suspend fun searchRecipes(query: String): Flow<List<Recipe>> {
        val apiQuery = if (query.isBlank()) "a" else query
        try {
            val response = RetrofitInstance.api.searchMeals(apiQuery)
            val mapped = response.meals?.map { dto ->
                val calculatedKcal = 300 + (dto.strMeal.length * 9) + (dto.idMeal.hashCode() % 100).absoluteValue
                Recipe(
                    id = dto.idMeal,
                    name = dto.strMeal,
                    description = dto.strInstructions ?: "No description provided.",
                    category = dto.strCategory ?: "Healthy",
                    calories = calculatedKcal,
                    imageDescription = dto.strMealThumb ?: "",
                    ingredients = dto.toIngredientsList(),
                    prepTimeMin = 10 + (dto.strMeal.length % 25),
                    isFavorite = false
                )
            } ?: emptyList()

            if (mapped.isNotEmpty()) {
                val currentFavorites = _recipesState.value.filter { it.isFavorite }.associateBy { it.id }
                val merged = mapped.map { recipe ->
                    if (currentFavorites.containsKey(recipe.id)) {
                        recipe.copy(isFavorite = true)
                    } else {
                        recipe
                    }
                }
                _recipesState.value = merged
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return _recipesState.asStateFlow()
    }

    override suspend fun toggleFavoriteRecipe(recipeId: String) {
        val list = _recipesState.value.map {
            if (it.id == recipeId) {
                it.copy(isFavorite = !it.isFavorite)
            } else {
                it
            }
        }
        _recipesState.value = list
    }

    override suspend fun resetMockData() {
        _nutritionState.value = initialNutrition
        _mealPlansState.value = initialMealPlans
        _recipesState.value = mockRecipes
    }
}
