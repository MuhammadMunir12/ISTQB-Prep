package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.BackHandler
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.data.*
import com.example.data.database.*
import com.example.ui.theme.*
import com.example.viewmodel.ChatMessage
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenState
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainAppScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val state by viewModel.screenState.collectAsState()
    val progressState by viewModel.progress.collectAsState()
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercept system Back Press on mobile screens
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        val isOnDashboard = state is ScreenState.Dashboard
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = if (isOnDashboard) "Exit Application?" else "Exit or Return Home?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (isOnDashboard) {
                        "Are you sure you want to close the ISTQB CTFL 4.0 Prep application?"
                    } else {
                        "Would you like to return to the Home page (Dashboard) or exit the application entirely?"
                    },
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                if (!isOnDashboard) {
                    Button(
                        onClick = {
                            viewModel.navigateTo(ScreenState.Dashboard)
                            showExitDialog = false
                        },
                        modifier = Modifier.testTag("dialog_home_button")
                    ) {
                        Text("Go to Home")
                    }
                } else {
                    TextButton(
                        onClick = { showExitDialog = false },
                        modifier = Modifier.testTag("dialog_cancel_button")
                    ) {
                        Text("Cancel")
                    }
                }
            },
            dismissButton = {
                if (!isOnDashboard) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                showExitDialog = false
                                (context as? Activity)?.finish()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.testTag("dialog_close_button")
                        ) {
                            Text("Close App")
                        }
                        TextButton(
                            onClick = { showExitDialog = false },
                            modifier = Modifier.testTag("dialog_cancel_button")
                        ) {
                            Text("Cancel")
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            showExitDialog = false
                            (context as? Activity)?.finish()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Text("Close App")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(state)) {
                BottomNavigationBar(
                    currentState = state,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val current = state) {
                is ScreenState.Dashboard -> DashboardScreen(viewModel, progressState)
                is ScreenState.SyllabusExplorer -> SyllabusExplorerScreen(viewModel)
                is ScreenState.ActiveSection -> ActiveSectionScreen(sectionId = current.sectionId, viewModel = viewModel)
                is ScreenState.AiTutor -> AiTutorScreen(viewModel)
                is ScreenState.MockExamConfig -> MockExamConfigScreen(viewModel)
                is ScreenState.MockExamActive -> MockExamActiveScreen(viewModel, current = current)
                is ScreenState.MockExamReview -> MockExamReviewScreen(attemptId = current.attemptId, viewModel = viewModel)
                is ScreenState.FlashcardDesk -> FlashcardDeskScreen(viewModel)
                is ScreenState.AnalyticsDashboard -> AnalyticsDashboardScreen(viewModel)
                is ScreenState.AdminCenter -> AdminCenterScreen(viewModel)
            }
        }
    }
}

@Composable
fun ThemeToggle(viewModel: MainViewModel) {
    val isDark by viewModel.isDarkTheme.collectAsState()
    IconButton(
        onClick = { viewModel.toggleTheme() },
        modifier = Modifier
            .size(38.dp)
            .background(Color.White.copy(alpha = 0.2f), CircleShape)
            .testTag("theme_toggle")
    ) {
        Text(
            text = if (isDark) "☀️" else "🌙",
            fontSize = 18.sp
        )
    }
}

fun shouldShowBottomBar(state: ScreenState): Boolean {
    return when (state) {
        is ScreenState.Dashboard,
        is ScreenState.SyllabusExplorer,
        is ScreenState.AiTutor,
        is ScreenState.MockExamConfig,
        is ScreenState.FlashcardDesk,
        is ScreenState.AnalyticsDashboard -> true
        else -> false
    }
}

@Composable
fun BottomNavigationBar(
    currentState: ScreenState,
    onNavigate: (ScreenState) -> Unit
) {
    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val items = listOf(
            Triple(ScreenState.Dashboard, Icons.Default.Home, "Home"),
            Triple(ScreenState.SyllabusExplorer, Icons.Default.List, "Syllabus"),
            Triple(ScreenState.AiTutor, Icons.Default.Face, "Tutor"),
            Triple(ScreenState.MockExamConfig, Icons.Default.PlayArrow, "Exams"),
            Triple(ScreenState.FlashcardDesk, Icons.Default.Star, "Cards"),
            Triple(ScreenState.AnalyticsDashboard, Icons.Default.Info, "Analytics")
        )

        items.forEach { (screenState, icon, label) ->
            val selected = when (currentState) {
                is ScreenState.Dashboard -> screenState is ScreenState.Dashboard
                is ScreenState.SyllabusExplorer -> screenState is ScreenState.SyllabusExplorer
                is ScreenState.AiTutor -> screenState is ScreenState.AiTutor
                is ScreenState.MockExamConfig -> screenState is ScreenState.MockExamConfig
                is ScreenState.FlashcardDesk -> screenState is ScreenState.FlashcardDesk
                is ScreenState.AnalyticsDashboard -> screenState is ScreenState.AnalyticsDashboard
                else -> false
            }

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screenState) },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("nav_${label.lowercase(Locale.ROOT)}")
            )
        }
    }
}

// 1. DASHBOARD SCREEN
@Composable
fun DashboardScreen(viewModel: MainViewModel, progress: UserProgressEntity?) {
    val nonNullProg = progress ?: UserProgressEntity()
    val attempts by viewModel.examAttempts.collectAsState()
    val achievements by viewModel.achievements.collectAsState()

    // Calculate dynamic readiness probability
    val examReadiness = remember(attempts) {
        if (attempts.isEmpty()) {
            "15%"
        } else {
            val total = attempts.size
            val passed = attempts.count { it.passed }
            val avgScore = attempts.map { (it.score * 100) / it.totalQuestions }.average()
            val scoreBonus = (avgScore * 0.6).toInt()
            val passBonus = (passed.toFloat() / total * 40).toInt()
            val calculated = kotlin.math.min(100, kotlin.math.max(15, scoreBonus + passBonus))
            "$calculated%"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Welcome Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Welcome, Student!",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                "Ready to conquer your ISTQB CTFL 4.0 exam?",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ThemeToggle(viewModel)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎓", fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔥", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${nonNullProg.streakDays} Day Streak",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Level ${nonNullProg.level}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${nonNullProg.xp} XP",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Daily Goal Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Daily XP Goal",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "${nonNullProg.todayXp} / ${nonNullProg.dailyGoalXp} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val goalPercent = if (nonNullProg.dailyGoalXp > 0) {
                        kotlin.math.min(1.0f, nonNullProg.todayXp.toFloat() / nonNullProg.dailyGoalXp)
                    } else 0f
                    LinearProgressIndicator(
                        progress = { goalPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        // AI Readiness Prediction
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            examReadiness,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Exam Readiness Probability",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Predicted average score based on mock exam metrics & chapter reviews.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // Quick Navigation Buttons
        item {
            Column {
                Text(
                    "Study Activities",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ActivityButton(
                            title = "Syllabus Explorer",
                            emoji = "📚",
                            onClick = { viewModel.navigateTo(ScreenState.SyllabusExplorer) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ActivityButton(
                            title = "AI Tutor Socrates",
                            emoji = "💬",
                            onClick = { viewModel.navigateTo(ScreenState.AiTutor) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ActivityButton(
                            title = "Timed Mock Exams",
                            emoji = "⏱️",
                            onClick = { viewModel.navigateTo(ScreenState.MockExamConfig) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ActivityButton(
                            title = "Spaced Flashcards",
                            emoji = "🎴",
                            onClick = { viewModel.navigateTo(ScreenState.FlashcardDesk) }
                        )
                    }
                }
            }
        }

        // Unlockable Achievements
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "My Achievements",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${achievements.count { it.unlocked }} / ${achievements.size} Unlocked",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                achievements.forEach { badge ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.unlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                        ),
                        border = BorderStroke(1.dp, if (badge.unlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (badge.unlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (badge.id) {
                                        "FIRST_STEPS" -> "🚀"
                                        "AI_TUTOR_CHAT" -> "💬"
                                        "EXAM_PASS" -> "🏆"
                                        "STREAK_3" -> "🔥"
                                        "PERFECT_QUIZ" -> "🎯"
                                        else -> "🏅"
                                    },
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    badge.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (badge.unlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                                )
                                Text(
                                    badge.description,
                                    fontSize = 12.sp,
                                    color = if (badge.unlocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Admin Access row
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.navigateTo(ScreenState.AdminCenter) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_button")
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Launch QA & Admin Lab")
            }
        }
    }
}

@Composable
fun ActivityButton(
    title: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, CardAccentBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(SafeGreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// 2. SYLLABUS EXPLORER SCREEN
@Composable
fun SyllabusExplorerScreen(viewModel: MainViewModel) {
    val chapters = SyllabusData.chapters

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "ISTQB CTFL 4.0 Syllabus",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Browse chapters, learn key terminologies & fulfill learning objectives.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggle(viewModel)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(chapters) { chapter ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Chapter ${chapter.id}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        when (chapter.difficulty) {
                                            "Easy" -> SafeGreenLight
                                            "Medium" -> LearnGold.copy(alpha = 0.15f)
                                            else -> WarningRedLight
                                        },
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    chapter.difficulty,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (chapter.difficulty) {
                                        "Easy" -> EmeraldGreen
                                        "Medium" -> LearnGold
                                        else -> WarningRed
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            chapter.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Includes ${chapter.sections.size} core lessons. Est. time: ${chapter.durationMinutes} minutes.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = CardAccentBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Lessons:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        chapter.sections.forEach { section ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.navigateTo(ScreenState.ActiveSection(section.id)) }
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📖", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            "Section ${section.id}: ${section.title}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            "Objectives: ${section.learningObjectives.joinToString(", ") { it.code }}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. ACTIVE SECTION READ SCREEN
@Composable
fun ActiveSectionScreen(sectionId: String, viewModel: MainViewModel) {
    val section = SyllabusData.chapters.flatMap { it.sections }.find { it.id == sectionId }
    val chapter = SyllabusData.getChapterBySection(sectionId)

    LaunchedEffect(sectionId) {
        viewModel.startReadingSection(sectionId)
    }

    if (section == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Section not found.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simple Top AppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lesson ${section.id}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = section.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            ThemeToggle(viewModel)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
        ) {
            // Objectives Badge Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SafeGreenLight),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Target Learning Objectives:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        section.learningObjectives.forEach { obj ->
                            Text(
                                "• ${obj.code} (${obj.level}): ${obj.description}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Explanation Tabs
            item {
                DetailCard(title = "Simple Explanation 💡") {
                    Text(section.content.simpleExplanation, fontSize = 14.sp)
                }
            }

            item {
                DetailCard(title = "Detailed Syllabus Analysis 📚") {
                    Text(section.content.detailedExplanation, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }

            item {
                DetailCard(title = "Real-World Analogy 🎭") {
                    Text(section.content.realWorldAnalogy, fontSize = 14.sp)
                }
            }

            item {
                DetailCard(title = "Practical Software Testing Example 💻") {
                    Text(section.content.softwareTestingExample, fontSize = 14.sp)
                }
            }

            item {
                DetailCard(title = "Common Mistakes & Pitfalls ⚠️") {
                    Text(section.content.commonMistakes, fontSize = 14.sp, color = WarningRed)
                }
            }

            item {
                DetailCard(title = "Socrates Study Trick & Mnemonic 🧠") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LearnGold.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            section.content.memoryTrick,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LearnGold
                        )
                    }
                }
            }

            item {
                DetailCard(title = "Key Exam Takeaways 🏆") {
                    section.content.keyTakeaways.forEach { takeaway ->
                        Row(modifier = Modifier.padding(bottom = 4.dp)) {
                            Text("✔ ", color = EmeraldGreen, fontWeight = FontWeight.Bold)
                            Text(takeaway, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Quick Ask AI Tutor
            item {
                Button(
                    onClick = {
                        viewModel.navigateTo(ScreenState.AiTutor)
                        viewModel.sendMessageToTutor("Explain section ${section.id} with a new example.")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Face, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Discuss Lesson with Socrates")
                }
            }
        }
    }
}

@Composable
fun DetailCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, CardAccentBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

// 4. AI TUTOR CHAT SCREEN (SOCRATES)
@Composable
fun AiTutorScreen(viewModel: MainViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isTutorLoading.collectAsState()
    var textInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦉", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "AI Tutor Socrates",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "Strictly verified against CTFL 4.0 Syllabus. Never hallucinates.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggle(viewModel)
            }
        }

        // Chat Message Log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (message.isUser) 16.dp else 0.dp,
                                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                                )
                            )
                            .background(
                                if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = if (message.isUser) 0.dp else 1.dp,
                                color = CardAccentBorder,
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (message.isUser) 16.dp else 0.dp,
                                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = if (message.isUser) "You" else "Socrates (Owl AI)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                color = if (message.isUser) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = message.text,
                                fontSize = 13.sp,
                                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, CardAccentBorder)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Socrates is examining the syllabus docs...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Quick suggestion row
        val suggestions = listOf("Explain Pesticide Paradox", "Verify V vs V", "Explain BVA limits")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { tag ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.sendMessageToTutor(tag)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(tag, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Chat Input Panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask Socrates about ISTQB guidelines...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightSlate,
                    unfocusedContainerColor = LightSlate,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendMessageToTutor(textInput)
                        textInput = ""
                        keyboardController?.hide()
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendMessageToTutor(textInput)
                        textInput = ""
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier.testTag("send_button")
            ) {
                Text("Send", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 5. MOCK EXAM CONFIG SCREEN
@Composable
fun MockExamConfigScreen(viewModel: MainViewModel) {
    var examType by remember { mutableStateOf("Full") } // "Full", "Quick", "Chapter", "Sample"
    var totalQuestions by remember { mutableStateOf(40) }
    var selectedChapterId by remember { mutableStateOf(1) }
    var selectedSampleExam by remember { mutableStateOf("A") } // "A", "B", "C", "D"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "ISTQB Mock Exam Laboratory",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Evaluate your preparation. Passed attempts requires 65% score.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggle(viewModel)
            }
        }

        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Select Exam Mode:",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Selector Row
            Row(modifier = Modifier.fillMaxWidth()) {
                val modes = listOf(
                    "Full" to "Full",
                    "Quick" to "Quick",
                    "Chapter" to "Chapter",
                    "Sample" to "Official Sample"
                )
                modes.forEach { (mode, label) ->
                    val isSelected = examType == mode
                    Button(
                        onClick = {
                            examType = mode
                            totalQuestions = if (mode == "Full") 40 else 10
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }

            if (examType == "Chapter") {
                Column {
                    Text(
                        "Target Chapter Syllabus:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SyllabusData.chapters.forEach { chap ->
                        val isSelected = selectedChapterId == chap.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .clickable { selectedChapterId = chap.id },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else CardAccentBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSelected, onClick = { selectedChapterId = chap.id })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Chapter ${chap.id}: ${chap.title}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else if (examType == "Sample") {
                Column {
                    Text(
                        "Select Official Sample Exam:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf("A", "B", "C", "D").forEach { letter ->
                        val isSelected = selectedSampleExam == letter
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .clickable { selectedSampleExam = letter },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else CardAccentBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSelected, onClick = { selectedSampleExam = letter })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Official Sample Exam $letter (CTFL 4.0)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Info Box for weights
                Card(
                    colors = CardDefaults.cardColors(containerColor = SafeGreenLight),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Official Chapter Weightings Covered:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• Ch 1: 8 Qs | Ch 2: 6 Qs | Ch 3: 4 Qs\n" +
                                    "• Ch 4: 11 Qs | Ch 5: 9 Qs | Ch 6: 2 Qs",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Fixed footer button at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    val finalType = if (examType == "Sample") "Sample_$selectedSampleExam" else examType
                    viewModel.navigateTo(
                        ScreenState.MockExamActive(
                            examType = finalType,
                            totalQuestions = if (examType == "Sample") 6 else totalQuestions,
                            chapterId = if (examType == "Chapter") selectedChapterId else null
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_exam_btn")
            ) {
                Text("Launch Exam Session", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmerTranslation"
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim.value, y = translateAnim.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@Composable
fun ExamSkeletonScreen() {
    val brush = shimmerBrush()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar Header Skeleton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 160.dp, height = 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            // Timer box Skeleton
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }

        // Progress Bar Skeleton (static tracks)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false
        ) {
            // Question Stem Card Skeleton
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 80.dp, height = 12.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(brush)
                            )
                            Box(
                                modifier = Modifier
                                    .size(width = 50.dp, height = 12.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(brush)
                            )
                        }
                        
                        // Fake question stem text lines
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                    }
                }
            }

            // 4 Option Cards Skeletons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(4) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, CardAccentBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(brush)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(brush)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Footer Buttons Skeleton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(brush)
            )
        }
    }
}

// 6. ACTIVE EXAM PLAYER SCREEN
@Composable
fun MockExamActiveScreen(viewModel: MainViewModel, current: ScreenState.MockExamActive) {
    val questions by viewModel.activeExamQuestions.collectAsState()
    val questionIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedOption by viewModel.selectedOptionIndex.collectAsState()
    val isChecked by viewModel.isAnswerChecked.collectAsState()
    val timerSec by viewModel.timerSeconds.collectAsState()

    var showConfirmEndDialog by remember { mutableStateOf(false) }

    BackHandler {
        showConfirmEndDialog = true
    }

    if (showConfirmEndDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmEndDialog = false },
            title = {
                Text(
                    text = "End Exam Early?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to end this exam early? Any unanswered questions will be graded as incorrect.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmEndDialog = false
                        viewModel.submitExamAndFinish(current.examType, current.totalQuestions, current.chapterId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("confirm_end_exam_btn")
                ) {
                    Text("End & Grade", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmEndDialog = false },
                    modifier = Modifier.testTag("cancel_end_exam_btn")
                ) {
                    Text("Keep Testing")
                }
            }
        )
    }

    LaunchedEffect(current) {
        viewModel.startExam(current.examType, current.totalQuestions, current.chapterId)
    }

    if (questions.isEmpty()) {
        ExamSkeletonScreen()
        return
    }

    val activeQ = questions.getOrNull(questionIndex) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar Header with Timer & End Exam action
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Active Session: ${if (current.examType.startsWith("Sample_")) "Official Sample Exam " + current.examType.substringAfter("Sample_") else current.examType}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Question ${questionIndex + 1} of ${questions.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Timer display
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = formatTimer(timerSec),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // End Exam Action Button
                OutlinedButton(
                    onClick = { showConfirmEndDialog = true },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                    modifier = Modifier.height(32.dp).testTag("end_exam_action_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "End",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("End", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Progress Bar
        val progressPercent = (questionIndex.toFloat() / questions.size)
        LinearProgressIndicator(
            progress = { progressPercent },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outlineVariant
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Question Stem Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "LO: ${activeQ.learningObjective} (${activeQ.kLevel})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Est. 1 min",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            activeQ.stem,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Options Selection
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val optionPrefixes = listOf("a) ", "b) ", "c) ", "d) ")
                    activeQ.options.forEachIndexed { idx, opt ->
                        val isSelected = selectedOption == idx
                        val isCorrectOpt = activeQ.correctIndex == idx

                        val optColor = when {
                            isChecked && isCorrectOpt -> SafeGreenLight
                            isChecked && isSelected -> WarningRedLight
                            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val strokeColor = when {
                            isChecked && isCorrectOpt -> EmeraldGreen
                            isChecked && isSelected -> WarningRed
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> CardAccentBorder
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isChecked) { viewModel.selectOption(idx) }
                                .testTag("opt_card_$idx"),
                            colors = CardDefaults.cardColors(containerColor = optColor),
                            border = BorderStroke(if (isSelected || isChecked) 2.dp else 1.dp, strokeColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionPrefixes.getOrElse(idx) { "" }.trim().removeSuffix(")"),
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = opt,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Show explanation/rationale after checked
            if (isChecked) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SafeGreenLight),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Owl Explanation & Syllabus Cite:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activeQ.rationale,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Action Panel Bottom (Check, Previous, Next, Finish)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.previousQuestion() },
                enabled = questionIndex > 0
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous")
            }

            if (!isChecked) {
                Button(
                    onClick = { viewModel.checkAnswer() },
                    enabled = selectedOption != null,
                    modifier = Modifier.testTag("check_answer_btn")
                ) {
                    Text("Check Answer", fontWeight = FontWeight.Bold)
                }
            } else {
                if (questionIndex < questions.size - 1) {
                    Button(
                        onClick = { viewModel.nextQuestion() },
                        modifier = Modifier.testTag("next_question_btn")
                    ) {
                        Text("Next Question", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.submitExamAndFinish(current.examType, current.totalQuestions, current.chapterId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LearnGold),
                        modifier = Modifier.testTag("submit_exam_btn")
                    ) {
                        Text("Finish & Submit Exam", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun formatTimer(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(Locale.ROOT, "%02d:%02d", m, s)
}

// 7. MOCK EXAM REVIEW SCREEN
@Composable
fun MockExamReviewScreen(attemptId: Int, viewModel: MainViewModel) {
    val attempts by viewModel.examAttempts.collectAsState()
    val activeAttempt = attempts.find { it.id == attemptId }

    if (activeAttempt == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No exam attempt logged yet.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (activeAttempt.passed) EmeraldGreen else WarningRed)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (activeAttempt.passed) "Congratulations!" else "Result: Needs Review",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Mock exam score breakdown and rationale cited checks.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeToggle(viewModel)
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (activeAttempt.passed) "🎉" else "💪", fontSize = 24.sp)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Your Score", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                "${activeAttempt.score} / ${activeAttempt.totalQuestions}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeAttempt.passed) EmeraldGreen else WarningRed
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Accuracy", fontSize = 11.sp, color = Color.Gray)
                            val percent = (activeAttempt.score * 100) / activeAttempt.totalQuestions
                            Text(
                                "$percent%",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeAttempt.passed) EmeraldGreen else WarningRed
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Time Spent", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                formatTimer(activeAttempt.timeSpentSeconds.toInt()),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Socrates Owl Recommendations:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                val percent = (activeAttempt.score * 100) / activeAttempt.totalQuestions
                val adviceText = if (percent >= 85) {
                    "Your command of these chapters is stellar! Challenge yourself with adaptive custom quizzes or try tutoring your peers in the community forum."
                } else if (percent >= 65) {
                    "You achieved the official passing threshold. To secure a high score in the real exam, study your weak areas via flashcards and vary your test cases."
                } else {
                    "Do not be discouraged! Go to the Syllabus lessons of Chapter 1, 2, or 4 and utilize the Socrates tricks to solidify terms like Verification vs Validation or 2-value boundary choices."
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = LearnGold.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, LearnGold.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🦉", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(adviceText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = LearnGold)
                    }
                }
            }
        }

        // Return Home action
        Box(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { viewModel.navigateTo(ScreenState.Dashboard) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("dashboard_back_btn")
            ) {
                Text("Return to Dashboard", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 8. FLASHCARD DECK (SPACED REPETITION LEITNER SYSTEM)
@Composable
fun FlashcardDeskScreen(viewModel: MainViewModel) {
    val termList = GlossaryData.terms
    val states by viewModel.flashcardStates.collectAsState()

    var cardIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val currentTerm = termList.getOrNull(cardIndex)

    val currentLeitnerBox = remember(currentTerm, states) {
        val st = states.find { it.term == currentTerm?.term }
        st?.boxNumber ?: 1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Leitner Spaced Repetition Deck",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Tap to flip. Know cards progress up boxes; others return to Box 1.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggle(viewModel)
            }
        }

        if (currentTerm == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No cards in the deck.")
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Card ${cardIndex + 1} of ${termList.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            // Flashcard container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .clickable { isFlipped = !isFlipped }
                    .testTag("flashcard_container"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFlipped) SafeGreenLight else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(2.dp, if (isFlipped) EmeraldGreen else CardAccentBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "Leitner Box $currentLeitnerBox",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!isFlipped) {
                        // FRONT
                        Text(
                            text = currentTerm.term,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Tap to flip and check definition",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    } else {
                        // BACK
                        Text(
                            text = currentTerm.term,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentTerm.definition,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Analogy: ${currentTerm.analogy}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Section ref: ${currentTerm.referenceSection}",
                            fontSize = 11.sp,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Leitner box feedback actions
            if (isFlipped) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.recordFlashcardPerformance(currentTerm.term, false)
                            isFlipped = false
                            if (cardIndex < termList.size - 1) cardIndex++ else cardIndex = 0
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_failed")
                    ) {
                        Text("Need Review ✖", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.recordFlashcardPerformance(currentTerm.term, true)
                            isFlipped = false
                            if (cardIndex < termList.size - 1) cardIndex++ else cardIndex = 0
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_known")
                    ) {
                        Text("I Knew It! ✔", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = { isFlipped = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Flip Card", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

// 9. ANALYTICS DASHBOARD
@Composable
fun AnalyticsDashboardScreen(viewModel: MainViewModel) {
    val progress by viewModel.progress.collectAsState()
    val attempts by viewModel.examAttempts.collectAsState()
    val history by viewModel.questionHistory.collectAsState()

    val totalTimeSpent = progress?.totalTimeSpentSeconds ?: 0L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Analytics & Weak Areas",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Track average accuracy, streak consistency, and chapter coverage.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggle(viewModel)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // KPI Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CardAccentBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Quizzes", fontSize = 11.sp, color = Color.Gray)
                            Text("${attempts.size}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CardAccentBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Average Score", fontSize = 11.sp, color = Color.Gray)
                            val avg = if (attempts.isEmpty()) "0%" else {
                                val sum = attempts.map { (it.score * 100) / it.totalQuestions }.sum()
                                "${sum / attempts.size}%"
                            }
                            Text(avg, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Weak Topics Analysis
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Syllabus Performance per Chapter:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        SyllabusData.chapters.forEach { chap ->
                            val chapHistory = history.filter { it.chapterId == chap.id }
                            val total = chapHistory.size
                            val correct = chapHistory.count { it.isCorrect }
                            val percent = if (total > 0) (correct * 100) / total else 0

                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Chapter ${chap.id}: ${chap.title}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("$percent% ($correct/$total Qs)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (percent >= 65) EmeraldGreen else WarningRed)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { percent.toFloat() / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (percent >= 65) EmeraldGreen else WarningRed,
                                    trackColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }

            // History Log List
            item {
                Text(
                    "Recent Attempts:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (attempts.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CardAccentBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Complete your first exam to log statistics here.",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(attempts) { attempt ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CardAccentBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (attempt.examType.startsWith("Sample_")) "Official Sample Exam " + attempt.examType.substringAfter("Sample_") else "${attempt.examType} Mock Quiz",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Accuracy: ${(attempt.score * 100) / attempt.totalQuestions}% | Time: ${formatTimer(attempt.timeSpentSeconds.toInt())}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (attempt.passed) SafeGreenLight else WarningRedLight,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (attempt.passed) "PASSED" else "FAILED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (attempt.passed) EmeraldGreen else WarningRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 10. ADMIN CENTER SCREEN
@Composable
fun AdminCenterScreen(viewModel: MainViewModel) {
    var queryText by remember { mutableStateOf("BVA limits") }
    val generatedQ by viewModel.generatedQuestion.collectAsState()
    val isGenerating by viewModel.isGeneratingQuestion.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "QA RAG Lab & Admin Center",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Regenerate syllabus context and test custom prompt generation.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            ThemeToggle(viewModel)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CardAccentBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Generate Brand New Exam Question:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Uses client-side RAG pipeline to pull direct sections, ensuring ZERO hallucinations.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = queryText,
                            onValueChange = { queryText = it },
                            placeholder = { Text("e.g., Chapter 4 BVA techniques") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = LightSlate,
                                unfocusedContainerColor = LightSlate
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.triggerAdminQuestionGeneration(queryText) },
                            enabled = !isGenerating,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generate_question_btn")
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Synthesizing context...")
                            } else {
                                Text("Synthesize & Generate Question")
                            }
                        }
                    }
                }
            }

            if (generatedQ != null) {
                val q = generatedQ!!
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Generated Scenario-Based Question:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(q.stem, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            val optLabels = listOf("A", "B", "C", "D")
                            q.options.forEachIndexed { i, opt ->
                                val isCorrect = q.correctIndex == i
                                Text(
                                    text = "${optLabels[i]}. $opt ${if (isCorrect) "✔ (Correct)" else ""}",
                                    fontSize = 13.sp,
                                    color = if (isCorrect) EmeraldGreen else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isCorrect) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = CardAccentBorder)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "Cited Learning Objective:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "${q.learningObjective} (K-Level: ${q.kLevel})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Rationale / Explanation:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                q.rationale,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
