package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DailyNutrition
import com.example.domain.model.MealPlan
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NutritionViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    viewModel: NutritionViewModel,
    modifier: Modifier = Modifier
) {
    val nutrition by viewModel.dailyNutrition.collectAsState()
    val mealPlans by viewModel.mealPlans.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showStatsAdjuster by remember { mutableStateOf(false) }
    var customMealName by remember { mutableStateOf("") }
    var customMealKcal by remember { mutableStateOf("") }
    var customMealCategory by remember { mutableStateOf("Breakfast") }
    var showAddMealForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SageLightBg)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Avatar with dynamic active dot
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color(0xFFFFD180), CircleShape) // Warm peach avatar
                    ) {
                        Text(
                            text = "EM",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkForest,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        // Online green indicator dot
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(FreshGreen, CircleShape)
                                .align(Alignment.BottomEnd)
                                .padding(2.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Hi, Emily",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkForest
                        )
                        Text(
                            text = "Welcome Back",
                            fontSize = 13.sp,
                            color = SoftGrey
                        )
                    }
                }

                // Notification Bell
                IconButton(
                    onClick = { /* Notification trigger */ },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(44.dp)
                        .shadow(1.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = DarkForest
                    )
                }
            }
        }

        // Today's Nutrition Overview title
        item {
            Text(
                text = "Today's Nutrition\nOverview",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkForest,
                lineHeight = 34.sp
            )
        }

        // Custom Search Bar wrapper
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(Color.White, RoundedCornerShape(27.dp))
                    .clickable {
                        viewModel.navigateTo(AppScreen.RECIPES)
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SoftGrey
                    )
                    Text(
                        text = "Search Recipes...",
                        color = SoftGrey,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Main Calorie Gauge Hub
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Eaten", color = SoftGrey, fontSize = 13.sp)
                            Text(
                                "${nutrition.eatenKcal} kcal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkForest
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Burned", color = SoftGrey, fontSize = 13.sp)
                            Text(
                                "${nutrition.burnedKcal} kcal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkForest
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Circular Calorie Gauge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        // Background track circle
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = BorderGrey,
                                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        // Colored progress ring depending on status
                        val isOverBudget = (nutrition.eatenKcal - nutrition.burnedKcal) > nutrition.targetKcal
                        val progressSweepAngle = if (isOverBudget) {
                            360f
                        } else {
                            val net = (nutrition.eatenKcal - nutrition.burnedKcal).coerceAtLeast(0)
                            (net.toFloat() / nutrition.targetKcal.toFloat()).coerceIn(0f, 1f) * 360f
                        }

                        val strokeColor = if (isOverBudget) CarbsColor else FreshGreen

                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = strokeColor,
                                startAngle = -90f,
                                sweepAngle = progressSweepAngle,
                                useCenter = false,
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        // Internal texts
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val diff = (nutrition.eatenKcal - nutrition.burnedKcal - nutrition.targetKcal)
                            if (diff > 0) {
                                Text(
                                    text = "$diff",
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CarbsColor,
                                    lineHeight = 44.sp
                                )
                                Text(
                                    text = "kcal over",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkForest
                                )
                            } else {
                                Text(
                                    text = "${-diff}",
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = FreshGreen,
                                    lineHeight = 44.sp
                                )
                                Text(
                                    text = "kcal left",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkForest
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Expander stats bar
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showStatsAdjuster = !showStatsAdjuster }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showStatsAdjuster) "Close Quick Settings" else "Quick Calorie Settings",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = FreshGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (showStatsAdjuster) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = FreshGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Collapsible Calorie Adjuster Form
                    AnimatedVisibility(
                        visible = showStatsAdjuster,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                "Simulate calorie logging or goal updates below:",
                                fontSize = 12.sp,
                                color = SoftGrey
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.addCustomMeal("Snack Log", "Snack", 100) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MintCard, contentColor = DarkForest),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("+100 kcal")
                                }
                                Button(
                                    onClick = { viewModel.addCustomMeal("Cheat Meal Log", "Dinner", 500) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MintCard, contentColor = DarkForest),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("+500 kcal")
                                }
                            }
                            Button(
                                onClick = { viewModel.resetApp() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Reset Active Session Data")
                            }
                        }
                    }
                }
            }
        }

        // Macronutrients horizontal grid sit side-by-side matches screenshot
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MacroCard(
                    title = "Carbs",
                    currentValue = nutrition.carbsEatenGrams,
                    targetValue = nutrition.carbsTargetGrams,
                    color = CarbsColor,
                    modifier = Modifier.weight(1.0f)
                )
                MacroCard(
                    title = "Protein",
                    currentValue = nutrition.proteinEatenGrams,
                    targetValue = nutrition.proteinTargetGrams,
                    color = ProteinColor,
                    modifier = Modifier.weight(1.0f)
                )
                MacroCard(
                    title = "Fat",
                    currentValue = nutrition.fatEatenGrams,
                    targetValue = nutrition.fatTargetGrams,
                    color = FatColor,
                    modifier = Modifier.weight(1.0f)
                )
            }
        }

        // Section header for today's active meal log
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Meal Plans Today",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest
                )

                IconButton(
                    onClick = { showAddMealForm = !showAddMealForm },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = FreshGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Custom Meal")
                }
            }
        }

        // Collapsible Meal Creator Form
        item {
            AnimatedVisibility(visible = showAddMealForm) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Add Custom Meal Plan Entry", fontWeight = FontWeight.Bold, color = DarkForest)

                        OutlinedTextField(
                            value = customMealName,
                            onValueChange = { customMealName = it },
                            label = { Text("Meal Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = customMealKcal,
                                onValueChange = { customMealKcal = it },
                                label = { Text("Calories") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )

                            var expandedDropdown by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.weight(1f).align(Alignment.CenterVertically)) {
                                Button(
                                    onClick = { expandedDropdown = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MintCard, contentColor = DarkForest),
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(customMealCategory)
                                }
                                DropdownMenu(
                                    expanded = expandedDropdown,
                                    onDismissRequest = { expandedDropdown = false }
                                ) {
                                    listOf("Breakfast", "Lunch", "Dinner", "Snack").forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat) },
                                            onClick = {
                                                customMealCategory = cat
                                                expandedDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val kcal = customMealKcal.toIntOrNull() ?: 150
                                if (customMealName.isNotBlank()) {
                                    viewModel.addCustomMeal(customMealName, customMealCategory, kcal)
                                    customMealName = ""
                                    customMealKcal = ""
                                    showAddMealForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FreshGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Plan & Log calories")
                        }
                    }
                }
            }
        }

        // Listing meal plans
        if (mealPlans.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No foods logged yet.",
                            fontWeight = FontWeight.Medium,
                            color = SoftGrey,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Tap the '+' or browse breakfast recipe recommendations to log active meals.",
                            color = SoftGrey.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(mealPlans, key = { it.id }) { meal ->
                MealPlanItemRow(meal = meal, onDelete = { viewModel.removeMeal(meal.id) })
            }
        }
    }
}

@Composable
fun MacroCard(
    title: String,
    currentValue: Int,
    targetValue: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.shadow(2.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Colored dot indicator matching screenshot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftGrey
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${currentValue}/${targetValue}g",
                fontSize = 14.sp,
                color = DarkForest,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Progress bar
            val fraction = (currentValue.toFloat() / targetValue.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = color,
                trackColor = BorderGrey
            )
        }
    }
}

@Composable
fun MealPlanItemRow(
    meal: MealPlan,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.category,
                    color = FreshGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = meal.mealName,
                    color = DarkForest,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (meal.notes.isNotBlank()) {
                    Text(
                        text = meal.notes,
                        color = SoftGrey,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${meal.calories} kcal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkForest,
                    modifier = Modifier.padding(end = 6.dp)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete meal",
                        tint = CarbsColor.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
