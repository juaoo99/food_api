package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NutritionRepositoryImpl
import com.example.domain.model.DailyNutrition
import com.example.domain.model.MealPlan
import com.example.domain.model.Recipe
import com.example.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    ONBOARDING,
    DASHBOARD,
    RECIPES,
    PROFILE
}

class NutritionViewModel(
    private val repository: NutritionRepository = NutritionRepositoryImpl()
) : ViewModel() {

    // Navigation and UX state
    private val _currentScreen = MutableStateFlow(AppScreen.ONBOARDING)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    // Core Data Flows from Clean Architecture repository
    val dailyNutrition: StateFlow<DailyNutrition> = repository.getDailyNutrition()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DailyNutrition(0, 0, 2000, 0, 100, 0, 100, 0, 100)
        )

    val mealPlans: StateFlow<List<MealPlan>> = repository.getMealPlans()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // FlatMapLatest automatically re-registers search filter flow when query changes
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val recipes: StateFlow<List<Recipe>> = _searchQuery
        .flatMapLatest { query ->
            repository.searchRecipes(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectRecipe(recipe: Recipe?) {
        _selectedRecipe.value = recipe
    }

    fun addCustomMeal(name: String, category: String, calories: Int, notes: String = "") {
        viewModelScope.launch {
            val meal = MealPlan(
                mealName = name,
                category = category,
                calories = calories,
                date = "Today",
                notes = notes
            )
            repository.addMealPlan(meal)
        }
    }

    fun logRecipeAsMeal(recipe: Recipe) {
        viewModelScope.launch {
            val meal = MealPlan(
                mealName = recipe.name,
                category = recipe.category,
                calories = recipe.calories,
                date = "Today",
                notes = "Logged from Recipe Guidelines"
            )
            repository.addMealPlan(meal)
        }
    }

    fun removeMeal(mealId: String) {
        viewModelScope.launch {
            repository.removeMealPlan(mealId)
        }
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            repository.toggleFavoriteRecipe(recipeId)
        }
    }

    fun resetApp() {
        viewModelScope.launch {
            repository.resetMockData()
            _searchQuery.value = ""
            _selectedRecipe.value = null
        }
    }
}
