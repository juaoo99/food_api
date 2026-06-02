package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.components.BottomNavigationBar
import com.example.ui.components.DashboardScreen
import com.example.ui.components.OnboardingScreen
import com.example.ui.components.ProfileScreen
import com.example.ui.components.RecipeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NutritionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Instantiate our MVVM View Model
            val viewModel = remember { NutritionViewModel() }
            val currentScreen by viewModel.currentScreen.collectAsState()

            MyApplicationTheme {
                Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                    when (screen) {
                        AppScreen.ONBOARDING -> {
                            OnboardingScreen(
                                onStartJourney = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        else -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = {
                                    BottomNavigationBar(
                                        currentScreen = screen,
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }
                            ) { innerPadding ->
                                Box(modifier = Modifier.padding(innerPadding)) {
                                    when (screen) {
                                        AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                                        AppScreen.RECIPES -> RecipeScreen(viewModel = viewModel)
                                        AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
