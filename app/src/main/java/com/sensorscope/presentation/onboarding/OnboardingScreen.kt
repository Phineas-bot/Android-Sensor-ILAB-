package com.sensorscope.presentation.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sensorscope.ui.theme.Cyan400
import com.sensorscope.ui.theme.Gray200
import com.sensorscope.ui.theme.Mint300
import com.sensorscope.ui.theme.Slate800
import com.sensorscope.ui.theme.Slate900
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlin.math.absoluteValue

// ─── Data ─────────────────────────────────────────────────────────────────────

private data class PageData(
    val emoji: String,
    val badge: String,
    val title: String,
    val description: String,
    val features: List<Pair<String, String>>,
    val accentColor: Color
)

private val PAGES = listOf(
    PageData(
        emoji = "📡",
        badge = "WELCOME",
        title = "Your Device.\nYour Lab.",
        description = "SensorScope turns your Android phone into a precision scientific instrument, ready to measure the physics happening around you.",
        features = listOf(
            "⚡" to "6 real-time sensor streams",
            "📈" to "Live interactive charts",
            "🔬" to "Hands-on experiments"
        ),
        accentColor = Cyan400
    ),
    PageData(
        emoji = "📊",
        badge = "DASHBOARD",
        title = "Live Sensor\nMonitoring",
        description = "Watch all six built-in sensors update in real time. Each sensor has its own live chart so you can see patterns as they happen.",
        features = listOf(
            "🔵" to "Accelerometer  ·  m/s²",
            "🟢" to "Gyroscope  ·  rad/s",
            "🟡" to "Light · Pressure · Proximity"
        ),
        accentColor = Cyan400
    ),
    PageData(
        emoji = "🧪",
        badge = "LABS",
        title = "Sensor\nExperiments",
        description = "Put your sensors to the test with guided interactive challenges designed to make physics tangible and fun.",
        features = listOf(
            "📳" to "Shake Challenge",
            "🧭" to "Magnetic North Hunt",
            "🏆" to "Real-time completion feedback"
        ),
        accentColor = Mint300
    ),
    PageData(
        emoji = "🔐",
        badge = "LOGS",
        title = "Secure Session\nRecording",
        description = "Record full sensor sessions, review them later, and export raw data as CSV — all gated behind biometric authentication.",
        features = listOf(
            "🗂️" to "Full session history",
            "🔑" to "Biometric authentication",
            "📤" to "One-tap CSV export"
        ),
        accentColor = Mint300
    ),
    PageData(
        emoji = "🚀",
        badge = "ALL SET",
        title = "Ready to\nExplore!",
        description = "The world of physics is already in your pocket. Tap Get Started to begin your first measurement.",
        features = listOf(
            "✅" to "Open Dashboard to monitor sensors",
            "✅" to "Try a lab experiment",
            "✅" to "Record your first session"
        ),
        accentColor = Cyan400
    )
)

// ─── Root composable ──────────────────────────────────────────────────────────

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { PAGES.size })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    val page = PAGES[currentPage]

    val accentColor by animateColorAsState(
        targetValue = page.accentColor,
        animationSpec = tween(durationMillis = 500),
        label = "accentColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
    ) {
        // Ambient background glow
        AmbientGlow(color = accentColor)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // ── Top bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentPage + 1}  /  ${PAGES.size}",
                    color = Gray200.copy(alpha = 0.30f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                if (currentPage < PAGES.size - 1) {
                    TextButton(onClick = onComplete) {
                        Text(
                            text = "Skip",
                            color = Gray200.copy(alpha = 0.42f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Pager ─────────────────────────────────────────────────────────
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 0.dp),
                beyondViewportPageCount = 1
            ) { index ->
                val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
                PageContent(
                    page = PAGES[index],
                    pageOffset = pageOffset
                )
            }

            // ── Bottom controls ───────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 44.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                PageDots(
                    count = PAGES.size,
                    current = currentPage,
                    accentColor = accentColor
                )

                if (currentPage == PAGES.size - 1) {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan400)
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text(
                            text = "Next",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

// ─── Single page content ──────────────────────────────────────────────────────

@Composable
private fun PageContent(page: PageData, pageOffset: Float) {
    val alpha = (1f - pageOffset.absoluteValue * 1.8f).coerceIn(0f, 1f)
    val slideX = (pageOffset * 56f).dp

    // Box handles the slide + fade; inner Column scrolls so nothing is ever clipped
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = slideX)
            .alpha(alpha),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .padding(top = 20.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge chip
            Surface(
                shape = RoundedCornerShape(100),
                color = page.accentColor.copy(alpha = 0.13f)
            ) {
                Text(
                    text = page.badge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = page.accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Animated icon
            SensorIcon(
                emoji = page.emoji,
                accentColor = page.accentColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Title
            Text(
                text = page.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Gray200,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = page.description,
                fontSize = 15.sp,
                color = Gray200.copy(alpha = 0.52f),
                textAlign = TextAlign.Center,
                lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Feature card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Slate800,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    page.features.forEach { (icon, label) ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = icon, fontSize = 20.sp)
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                color = Gray200.copy(alpha = 0.72f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Icon with pulsing glow ───────────────────────────────────────────────────

@Composable
private fun SensorIcon(
    emoji: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_radius"
    )

    Box(
        modifier = modifier.size(112.dp),
        contentAlignment = Alignment.Center
    ) {
        // Pulsing outer glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val r = (size.minDimension / 2f) * pulseRadius
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = pulseAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = r
                ),
                radius = r,
                center = center
            )
        }

        // Solid inner circle
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1C2B45), Slate800)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.65f),
                            accentColor.copy(alpha = 0.10f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
    }
}

// ─── Dot page indicator ───────────────────────────────────────────────────────

@Composable
private fun PageDots(count: Int, current: Int, accentColor: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            val isActive = index == current
            val dotWidth by animateDpAsState(
                targetValue = if (isActive) 30.dp else 8.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "dot_w_$index"
            )
            val dotColor by animateColorAsState(
                targetValue = if (isActive) accentColor else Gray200.copy(alpha = 0.22f),
                animationSpec = tween(durationMillis = 280),
                label = "dot_c_$index"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

// ─── Ambient background glow ──────────────────────────────────────────────────

@Composable
private fun AmbientGlow(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient")
    val breathe by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .offset(y = (-110).dp)
            .alpha(0.16f)
    ) {
        val radius = size.width * 0.65f * breathe
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 1f),
                    Color.Transparent
                ),
                center = Offset(x = size.width / 2f, y = 0f),
                radius = radius
            ),
            radius = radius,
            center = Offset(x = size.width / 2f, y = 0f)
        )
    }
}
