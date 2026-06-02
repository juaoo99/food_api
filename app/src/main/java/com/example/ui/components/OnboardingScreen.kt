package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    onStartJourney: () -> Unit,
    modifier: Modifier = Modifier
) {
    var animateIn by remember { mutableStateOf(false) }
    val scaleFactor by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(100)
        animateIn = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SageLightBg, Color.White)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "yazio",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = DarkForest,
                    letterSpacing = (-1.5).sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Beginner | Health | 3-7 Days",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = FreshGreen,
                    letterSpacing = 1.sp
                )
            }

            // Beautiful Custom Food Illustration Plate with Pin Annotations
            Box(
                modifier = Modifier
                    .size(310.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                // Background Shadow Plate
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .shadow(32.dp, shape = CircleShape, clip = false)
                        .background(Color.White, shape = CircleShape)
                        .clip(CircleShape)
                ) {
                    // Beautiful Fresh Green Salad Bowl Base using overlapping circles and custom Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        
                        // Draw salad leaves background (Emerald-mint gradient)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)),
                                center = center,
                                radius = size.width * 0.45f
                            ),
                            radius = size.width * 0.44f,
                            center = center
                        )
                        
                        // Overlapping Cucumber slice blobs inside bowl
                        drawCircle(
                            color = Color(0xFFA5D6A7),
                            radius = 35f,
                            center = Offset(center.x - 70f, center.y - 40f)
                        )
                        drawCircle(
                            color = Color(0xFF81C784),
                            radius = 30f,
                            center = Offset(center.x - 50f, center.y - 50f)
                        )

                        // Overlapping Tomatoes wedges inside bowl (Alizarin Red/Rose)
                        drawCircle(
                            color = Color(0xFFFF8A80),
                            radius = 45f,
                            center = Offset(center.x + 40f, center.y - 65f)
                        )
                        drawCircle(
                            color = Color(0xFFFF5252),
                            radius = 35f,
                            center = Offset(center.x + 30f, center.y - 45f)
                        )

                        // Overlapping Tofu cube blobs
                        drawCircle(
                            color = Color(0xFFF5F5F5),
                            radius = 32f,
                            center = Offset(center.x + 60f, center.y + 50f)
                        )

                        // Overlapping Crispy Lettuce base
                        drawCircle(
                            color = Color(0xFF69F0AE),
                            radius = 40f,
                            center = Offset(center.x - 30f, center.y + 70f)
                        )
                        
                        // Draw organic dressings
                        drawCircle(
                            color = Color(0xFFFFD54F), // olive oil drippings
                            radius = 12f,
                            center = center
                        )
                    }
                }

                // Custom Pin lines + Annotation chips exactly like screenshot
                // Annotation 1: Tomato (Top Right)
                LeaderLinePin(
                    label = "Tomato",
                    offsetX = 55.dp,
                    offsetY = (-95).dp,
                    lineStartX = -10f,
                    lineStartY = 30f,
                    lineEndX = -80f,
                    lineEndY = 80f
                )

                // Annotation 2: Cucumber (Top Left)
                LeaderLinePin(
                    label = "Cucumber",
                    offsetX = (-75).dp,
                    offsetY = (-70).dp,
                    lineStartX = 50f,
                    lineStartY = 20f,
                    lineEndX = 110f,
                    lineEndY = 90f
                )

                // Annotation 3: Tofu (Mid Right)
                LeaderLinePin(
                    label = "Tofu",
                    offsetX = 92.dp,
                    offsetY = 15.dp,
                    lineStartX = -10f,
                    lineStartY = -10f,
                    lineEndX = -70f,
                    lineEndY = -30f
                )

                // Annotation 4: Lettuce (Bottom Right)
                LeaderLinePin(
                    label = "Lettuce",
                    offsetX = 65.dp,
                    offsetY = 90.dp,
                    lineStartX = -10f,
                    lineStartY = -20f,
                    lineEndX = -100f,
                    lineEndY = -80f
                )
            }

            // High Typography Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Discover healthy\nmeals, ready in minutes",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForest,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Browse nutritious recipes, track calories, and build better eating habits effortlessly.",
                    fontSize = 14.sp,
                    color = SoftGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                // Custom CTA Button
                Button(
                    onClick = onStartJourney,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FreshGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(54.dp)
                        .testTag("onboarding_start_button")
                ) {
                    Text(
                        text = "Start Your Journey",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderLinePin(
    label: String,
    offsetX: androidx.compose.ui.unit.Dp,
    offsetY: androidx.compose.ui.unit.Dp,
    lineStartX: Float,
    lineStartY: Float,
    lineEndX: Float,
    lineEndY: Float
) {
    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Label Chip
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DarkForest,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .shadow(1.dp, shape = RoundedCornerShape(10.dp))
            )

            // Dynamic Leader Line to center bowl plate
            Canvas(modifier = Modifier.size(16.dp)) {
                drawLine(
                    color = SoftGrey.copy(alpha = 0.6f),
                    start = Offset(lineStartX, lineStartY),
                    end = Offset(lineEndX, lineEndY),
                    strokeWidth = 2.5f
                )
            }
        }
    }
}
