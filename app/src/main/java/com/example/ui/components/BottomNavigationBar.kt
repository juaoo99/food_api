package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkForest
import com.example.ui.theme.DarkOnyx
import com.example.ui.theme.FreshGreen
import com.example.ui.viewmodel.AppScreen

@Composable
fun BottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    // Elegant Floating Pill container mirroring the screenshot
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .navigationBarsPadding(), // Account for bottom gesture bar notch
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = Color(0xFF1E2824), // Sleek obsidian/slate background
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(12.dp, RoundedCornerShape(32.dp)),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item 1: Home Dashboard
                BottomNavItem(
                    selected = currentScreen == AppScreen.DASHBOARD,
                    onClick = { onNavigate(AppScreen.DASHBOARD) },
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    contentDescription = "Dashboard"
                )

                // Item 2: Recipes
                BottomNavItem(
                    selected = currentScreen == AppScreen.RECIPES,
                    onClick = { onNavigate(AppScreen.RECIPES) },
                    selectedIcon = Icons.Filled.Search,
                    unselectedIcon = Icons.Outlined.Search,
                    contentDescription = "Recipes"
                )

                // Item 3: Profile Settings
                BottomNavItem(
                    selected = currentScreen == AppScreen.PROFILE,
                    onClick = { onNavigate(AppScreen.PROFILE) },
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = if (selected) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = contentDescription,
                tint = if (selected) FreshGreen else Color.White.copy(alpha = 0.65f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
