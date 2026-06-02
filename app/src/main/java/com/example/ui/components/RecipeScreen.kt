package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.domain.model.Recipe
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NutritionViewModel

fun getMealImageUrl(baseUrl: String, size: String): String {
    if (baseUrl.isBlank()) return ""
    val cleanUrl = baseUrl.removeSuffix("/preview").removeSuffix("/small").removeSuffix("/medium").removeSuffix("/large")
    return when (size) {
        "small" -> "$cleanUrl/preview"
        "medium" -> cleanUrl
        "large" -> cleanUrl
        else -> cleanUrl
    }
}

fun getCleanIngredientUrl(ingredient: String, size: String = "medium"): String {
    val nameOnly = ingredient.split(" - ").first().trim()
    val formattedName = nameOnly.replace(" ", "_").lowercase()
    return when (size) {
        "small" -> "https://www.themealdb.com/images/ingredients/$formattedName-small.png"
        "medium" -> "https://www.themealdb.com/images/ingredients/$formattedName-medium.png"
        "large" -> "https://www.themealdb.com/images/ingredients/$formattedName-large.png"
        "original" -> "https://www.themealdb.com/images/ingredients/$formattedName.png"
        else -> "https://www.themealdb.com/images/ingredients/$formattedName-medium.png"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: NutritionViewModel,
    modifier: Modifier = Modifier
) {
    val recipes by viewModel.recipes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()

    var showLoggedBanner by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showLoggedBanner) {
        if (showLoggedBanner != null) {
            kotlinx.coroutines.delay(2000)
            showLoggedBanner = null
        }
    }

    Box(modifier = modifier.fillMaxSize().background(SageLightBg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Back Arrow matches layout
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                            .shadow(1.dp, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back to Dashboard",
                            tint = DarkForest
                        )
                    }

                    Text(
                        text = "Recipes Catalog",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkForest,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(40.dp))
                }
            }

            // Screen subtitle
            item {
                Text(
                    text = "Search Live\nNutritious Meals",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkForest,
                    lineHeight = 34.sp
                )
            }

            // Interactive Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Search recipes (e.g. Lime, Chicken, Cake)...", color = SoftGrey) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon", tint = SoftGrey) },
                    modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(24.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = FreshGreen,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
            }

            // Breakfast recommendation panel matching right mockup
            item {
                Text(
                    text = "Featured Recommendation",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest
                )
            }

            item {
                val recommendedBreakfast = recipes.find { it.category == "Breakfast" || it.calories > 400 } ?: recipes.firstOrNull()
                if (recommendedBreakfast != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SageLightBg)
                            ) {
                                if (recommendedBreakfast.imageDescription.isNotEmpty()) {
                                    AsyncImage(
                                        model = recommendedBreakfast.imageDescription,
                                        contentDescription = recommendedBreakfast.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(FreshGreen, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Top recommendation",
                                        color = FreshGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = recommendedBreakfast.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DarkForest,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${recommendedBreakfast.calories} kcal • ${recommendedBreakfast.category}",
                                    color = SoftGrey,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.logRecipeAsMeal(recommendedBreakfast)
                                    showLoggedBanner = "Logged ${recommendedBreakfast.name}!"
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MintCard,
                                    contentColor = DarkForest
                                ),
                                modifier = Modifier
                                    .size(46.dp)
                                    .shadow(1.dp, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Log recommended meal",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // List Title for Nutritious items
            item {
                Text(
                    text = "Healthy Recipe Catalog Results",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Grid scroll representing cards
            if (recipes.isEmpty()) {
                item {
                    Text(
                        "No recipes loaded. Search above to query live database.",
                        color = SoftGrey,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                    )
                }
            } else {
                items(recipes) { recipe ->
                    RecipeIdeaCard(
                        recipe = recipe,
                        onSeeRecipe = { viewModel.selectRecipe(recipe) },
                        onFavoriteToggle = { viewModel.toggleFavorite(recipe.id) },
                        onLogMeal = {
                            viewModel.logRecipeAsMeal(recipe)
                            showLoggedBanner = "Logged ${recipe.name}!"
                        }
                    )
                }
            }
        }

        // Float Toast-like banner to indicate logging success
        AnimatedVisibility(
            visible = showLoggedBanner != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkForest),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .shadow(8.dp, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Logged icon", tint = FreshGreen)
                    Text(
                        text = showLoggedBanner ?: "",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // High-Fidelity Custom Detail modal dialog sheet matching screen details
        selectedRecipe?.let { recipe ->
            RecipeDetailsDialog(
                recipe = recipe,
                onDismiss = { viewModel.selectRecipe(null) },
                onLogRecipe = {
                    viewModel.logRecipeAsMeal(recipe)
                    viewModel.selectRecipe(null)
                    showLoggedBanner = "Logged ${recipe.name}!"
                }
            )
        }
    }
}

@Composable
fun RecipeIdeaCard(
    recipe: Recipe,
    onSeeRecipe: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onLogMeal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(24.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MintCard, SageLightBg)
                        )
                    )
            ) {
                if (recipe.imageDescription.isNotEmpty()) {
                    AsyncImage(
                        model = recipe.imageDescription,
                        contentDescription = recipe.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )

                // Heart Favoriting
                IconButton(
                    onClick = onFavoriteToggle,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = CarbsColor
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(36.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like recipe",
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Plus sign to add straight to planner
                IconButton(
                    onClick = onLogMeal,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = FreshGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(38.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add meal",
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Food category badge
                Text(
                    text = recipe.category.uppercase(),
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(FreshGreen, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .align(Alignment.BottomStart)
                )
            }

            // Description contents
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recipe.description,
                    color = SoftGrey,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${recipe.calories} kcal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkForest
                    )

                    Button(
                        onClick = onSeeRecipe,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkForest, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("See Recipe", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsDialog(
    recipe: Recipe,
    onDismiss: () -> Unit,
    onLogRecipe: () -> Unit
) {
    var sizeSpec by remember { mutableStateOf("medium") } // "small", "medium", "large"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Main visual header mapping preview
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MintCard)
                    ) {
                        if (recipe.imageDescription.isNotEmpty()) {
                            AsyncImage(
                                model = getMealImageUrl(recipe.imageDescription, sizeSpec),
                                contentDescription = recipe.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                    )
                                )
                        )

                        Text(
                            text = recipe.category.uppercase(),
                            color = FreshGreen,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        )
                    }
                }

                // Specification selector bar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(SageLightBg, RoundedCornerShape(12.dp)).padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Thumb Specs:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkForest,
                            modifier = Modifier.padding(start = 6.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("small", "medium", "large").forEach { size ->
                                val isSelected = sizeSpec == size
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) FreshGreen else Color.White)
                                        .clickable { sizeSpec = size }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = size.uppercase(),
                                        color = if (isSelected) Color.White else SoftGrey,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = recipe.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkForest
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = recipe.description,
                        fontSize = 13.sp,
                        color = SoftGrey
                    )
                }

                // Ingredients checklist with image resolutions
                item {
                    Divider(color = BorderGrey, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ingredients with Live Thumbnails (${sizeSpec.uppercase()}):",
                        fontWeight = FontWeight.Bold,
                        color = DarkForest,
                        fontSize = 14.sp
                    )
                }

                items(recipe.ingredients) { ingredient ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SageLightBg, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = getCleanIngredientUrl(ingredient, sizeSpec),
                                contentDescription = ingredient,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Column {
                            Text(
                                text = ingredient,
                                fontSize = 13.sp,
                                color = DarkForest,
                                fontWeight = FontWeight.Bold
                            )
                            val cleanedIng = ingredient.split(" - ").first().trim().replace(" ", "_").lowercase()
                            Text(
                                text = "url: mealdb/.../${cleanedIng}${if(sizeSpec != "large") "-$sizeSpec" else ""}.png",
                                fontSize = 8.sp,
                                color = SoftGrey
                            )
                        }
                    }
                }

                item {
                    Divider(color = BorderGrey, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = MintCard, contentColor = DarkForest),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Dismiss")
                        }

                        Button(
                            onClick = onLogRecipe,
                            colors = ButtonDefaults.buttonColors(containerColor = FreshGreen, contentColor = Color.White),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Log ${recipe.calories} kcal")
                        }
                    }
                }
            }
        }
    }
}
