package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.NutritionViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: NutritionViewModel,
    modifier: Modifier = Modifier
) {
    val nutrition by viewModel.dailyNutrition.collectAsState()
    var targetKcalInput by remember { mutableStateOf(nutrition.targetKcal.toString()) }
    var showGoalSavedToast by remember { mutableStateOf(false) }

    LaunchedEffect(showGoalSavedToast) {
        if (showGoalSavedToast) {
            kotlinx.coroutines.delay(2000)
            showGoalSavedToast = false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SageLightBg)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Person Info Header
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MintCard, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User photo profile",
                        tint = DarkForest,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Emily Parker",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest
                )

                Text(
                    text = "emily.parker@yaziohealth.com",
                    fontSize = 13.sp,
                    color = SoftGrey
                )
            }
        }

        // Active Goals Configuration Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = FreshGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Caloric & Macro Targets",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DarkForest
                        )
                    }

                    OutlinedTextField(
                        value = targetKcalInput,
                        onValueChange = { targetKcalInput = it },
                        label = { Text("Daily Target (kcal)", color = SoftGrey) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreshGreen,
                            unfocusedBorderColor = BorderGrey
                        )
                    )

                    Button(
                        onClick = {
                            val newTarget = targetKcalInput.toIntOrNull() ?: 2000
                            // Let's directly simulate adding dummy values in backend or update flow manually
                            // by triggering custom logs. To keep it simple, we simulate updating goals.
                            showGoalSavedToast = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FreshGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Calorie Target")
                    }

                    if (showGoalSavedToast) {
                        Text(
                            text = "Target goal simulation updated successfully!",
                            color = FreshGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Quick Stats Cards
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Physical Profiles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkForest
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileInfoMetric(label = "Current Weight", value = "62.4 kg")
                        ProfileInfoMetric(label = "Target Weight", value = "58.0 kg")
                        ProfileInfoMetric(label = "Height", value = "168 cm")
                    }
                }
            }
        }

        // Action Reset Session
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)), // Soft red
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().clickable { viewModel.resetApp() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = CarbsColor)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Reset App State",
                                fontWeight = FontWeight.Bold,
                                color = CarbsColor,
                                fontSize = 15.sp
                            )
                            Text(
                                "Clear logs and return to defaults",
                                color = SoftGrey,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoMetric(
    label: String,
    value: String
) {
    Column {
        Text(text = label, fontSize = 12.sp, color = SoftGrey)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkForest)
    }
}
