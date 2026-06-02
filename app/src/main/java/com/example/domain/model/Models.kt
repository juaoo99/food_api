package com.example.domain.model

import java.util.UUID

data class DailyNutrition(
    val eatenKcal: Int,
    val burnedKcal: Int,
    val targetKcal: Int,
    val carbsEatenGrams: Int,
    val carbsTargetGrams: Int,
    val proteinEatenGrams: Int,
    val proteinTargetGrams: Int,
    val fatEatenGrams: Int,
    val fatTargetGrams: Int
) {
    val remainingKcal: Int
        get() = targetKcal - eatenKcal + burnedKcal

    val percentCarbs: Float
        get() = (carbsEatenGrams.toFloat() / carbsTargetGrams.toFloat()).coerceIn(0f, 1f)

    val percentProtein: Float
        get() = (proteinEatenGrams.toFloat() / proteinTargetGrams.toFloat()).coerceIn(0f, 1f)

    val percentFat: Float
        get() = (fatEatenGrams.toFloat() / fatTargetGrams.toFloat()).coerceIn(0f, 1f)
}

data class MealPlan(
    val id: String = UUID.randomUUID().toString(),
    val mealName: String,
    val category: String, // "Breakfast", "Lunch", "Dinner", "Snack"
    val calories: Int,
    val date: String,     // e.g. "Today", "Tomorrow"
    val notes: String = ""
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: String, // "Breakfast", "Lunch", "Dinner"
    val calories: Int,
    val imageDescription: String,
    val ingredients: List<String>,
    val prepTimeMin: Int = 15,
    val isFavorite: Boolean = false
)
