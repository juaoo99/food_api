package com.example.domain.repository

import com.example.domain.model.DailyNutrition
import com.example.domain.model.MealPlan
import com.example.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {
    fun getDailyNutrition(): Flow<DailyNutrition>
    fun getMealPlans(): Flow<List<MealPlan>>
    fun getRecipes(): Flow<List<Recipe>>
    
    suspend fun addMealPlan(mealPlan: MealPlan)
    suspend fun removeMealPlan(mealId: String)
    suspend fun addEatenCalories(calories: Int, carbs: Int, protein: Int, fat: Int)
    suspend fun searchRecipes(query: String): Flow<List<Recipe>>
    suspend fun toggleFavoriteRecipe(recipeId: String)
    suspend fun resetMockData()
}
