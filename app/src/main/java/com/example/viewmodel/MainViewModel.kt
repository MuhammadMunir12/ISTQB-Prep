package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.api.*
import com.example.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ScreenState {
    object Dashboard : ScreenState()
    object SyllabusExplorer : ScreenState()
    data class ActiveSection(val sectionId: String) : ScreenState()
    object AiTutor : ScreenState()
    object MockExamConfig : ScreenState()
    data class MockExamActive(val examType: String, val totalQuestions: Int, val chapterId: Int? = null) : ScreenState()
    data class MockExamReview(val attemptId: Int) : ScreenState()
    object FlashcardDesk : ScreenState()
    object AnalyticsDashboard : ScreenState()
    object AdminCenter : ScreenState()
}

data class ChatMessage(val sender: String, val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val progressDao = db.userProgressDao()
    private val historyDao = db.questionHistoryDao()
    private val attemptDao = db.examAttemptDao()
    private val flashcardDao = db.flashcardStateDao()
    private val achievementDao = db.achievementDao()

    // Screen Navigation
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Dashboard)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    // Dark Theme preference state
    private val sharedPrefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val _isDarkTheme = MutableStateFlow(sharedPrefs.getBoolean("is_dark_theme", false))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newVal = !_isDarkTheme.value
        _isDarkTheme.value = newVal
        sharedPrefs.edit().putBoolean("is_dark_theme", newVal).apply()
    }

    // Screen back stack
    private val navigationBackStack = mutableListOf<ScreenState>()

    // Local progress state
    val progress = progressDao.getProgress().stateIn(viewModelScope, SharingStarted.Lazily, null)
    val questionHistory = historyDao.getAllHistory().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val examAttempts = attemptDao.getAllAttempts().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val flashcardStates = flashcardDao.getAllStates().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val achievements = achievementDao.getAllAchievements().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // AI Tutor chat state
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "Socrates",
                text = "Hello! I am Socrates, your AI ISTQB Foundation Level Tutor. I can help explain tough software testing topics, guide you with hints through incorrect answers, and construct test mnemonics. What would you like to explore today?",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    private val apiChatHistory = mutableListOf<Part>()

    private val _isTutorLoading = MutableStateFlow(false)
    val isTutorLoading: StateFlow<Boolean> = _isTutorLoading.asStateFlow()

    // Memoization Cache for Exam Questions to improve performance
    private val examCache = mutableMapOf<String, List<ExamQuestion>>()

    // Active Mock Exam State
    val activeExamQuestions = MutableStateFlow<List<ExamQuestion>>(emptyList())
    val currentQuestionIndex = MutableStateFlow(0)
    val selectedOptionIndex = MutableStateFlow<Int?>(null) // index selected for current question
    val isAnswerChecked = MutableStateFlow(false) // immediately show explanation in practice/practice levels
    val answeredQuestionsList = mutableListOf<Int?>() // record user's selected answers
    val examStartTime = MutableStateFlow(0L)
    val timerSeconds = MutableStateFlow(0)
    private var timerJob: kotlinx.coroutines.Job? = null

    // For active section reading tracking
    val activeSectionId = MutableStateFlow<String?>(null)

    // AI Question Generation states in Admin panel
    val generatedQuestion = MutableStateFlow<ExamQuestion?>(null)
    val isGeneratingQuestion = MutableStateFlow(false)

    init {
        initDefaultDbData()
    }

    private fun initDefaultDbData() {
        viewModelScope.launch(Dispatchers.IO) {
            // Verify Progress Row exists
            val existingProg = progressDao.getProgressDirect()
            if (existingProg == null) {
                progressDao.saveProgress(UserProgressEntity())
            }

            // Verify standard achievements
            val existingAchievements = achievementDao.getAllAchievementsDirect()
            if (existingAchievements.isEmpty()) {
                val list = listOf(
                    AchievementEntity("FIRST_STEPS", "First Steps", "Browse your first syllabus section.", "ic_book", false),
                    AchievementEntity("AI_TUTOR_CHAT", "Socratic Dialogue", "Send a message to the AI Socrates Tutor.", "ic_chat", false),
                    AchievementEntity("EXAM_PASS", "Certified Candidate", "Pass a full 40-question mock exam with 65% or more.", "ic_emoji_events", false),
                    AchievementEntity("STREAK_3", "Committed Learner", "Reach a 3-day active study streak.", "ic_local_fire_department", false),
                    AchievementEntity("PERFECT_QUIZ", "Flawless Performance", "Score 100% on a module or practice quiz.", "ic_check_circle", false)
                )
                achievementDao.insertAchievements(list)
            }
        }
    }

    // Navigation Methods
    fun navigateTo(state: ScreenState) {
        navigationBackStack.add(_screenState.value)
        _screenState.value = state
    }

    fun navigateBack() {
        if (navigationBackStack.isNotEmpty()) {
            _screenState.value = navigationBackStack.removeAt(navigationBackStack.lastIndex)
        } else {
            _screenState.value = ScreenState.Dashboard
        }
    }

    // AI Tutor Send Message (with local context RAG)
    fun sendMessageToTutor(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = "Student", text = text, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg
        apiChatHistory.add(Part(text = "User: $text"))

        _isTutorLoading.value = true

        viewModelScope.launch {
            val reply = AiTutorService.getTutorResponse(text, apiChatHistory.toList())
            val botMsg = ChatMessage(sender = "Socrates", text = reply, isUser = false)
            _chatMessages.value = _chatMessages.value + botMsg
            apiChatHistory.add(Part(text = "Model Response: $reply"))
            _isTutorLoading.value = false

            // Award XP for AI Tutor Interaction
            addXp(10)
            unlockAchievement("AI_TUTOR_CHAT")
        }
    }

    // Gamification - Award XP and handle level up
    fun addXp(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProg = progressDao.getProgressDirect() ?: UserProgressEntity()
            val newXp = currentProg.xp + amount
            val newTodayXp = currentProg.todayXp + amount
            
            // Level formula: level = (xp / 100) + 1
            val newLevel = (newXp / 100) + 1
            val levelUp = newLevel > currentProg.level

            // Handle Streaks
            val now = System.currentTimeMillis()
            val oneDayMs = 24 * 60 * 60 * 1000L
            var newStreak = currentProg.streakDays
            if (currentProg.streakDays == 0) {
                newStreak = 1
            } else if (now - currentProg.lastStudyTimestamp > oneDayMs && now - currentProg.lastStudyTimestamp < (oneDayMs * 2)) {
                newStreak += 1
            } else if (now - currentProg.lastStudyTimestamp >= (oneDayMs * 2)) {
                newStreak = 1 // Reset if missed more than 1 day
            }

            if (newStreak >= 3) {
                unlockAchievement("STREAK_3")
            }

            progressDao.saveProgress(
                currentProg.copy(
                    xp = newXp,
                    level = newLevel,
                    streakDays = newStreak,
                    lastStudyTimestamp = now,
                    todayXp = newTodayXp
                )
            )
        }
    }

    private fun unlockAchievement(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            achievementDao.unlockAchievement(id, System.currentTimeMillis())
        }
    }

    // Mock Exam Engine Setup
    fun startExam(examType: String, totalQuestions: Int, chapterId: Int? = null) {
        val cacheKey = "$examType|$totalQuestions|$chapterId"
        viewModelScope.launch(Dispatchers.IO) {
            // Instantly clear existing questions to trigger the skeleton loading UI
            activeExamQuestions.value = emptyList()
            
            // Introduce a short delay (e.g. 800ms) to let the skeleton UI transition smoothly,
            // which dramatically improves perceived user performance.
            delay(800)

            val finalizedQuestions: List<ExamQuestion>
            if (examCache.containsKey(cacheKey)) {
                finalizedQuestions = examCache[cacheKey] ?: emptyList()
            } else {
                val examPool = mutableListOf<ExamQuestion>()

                if (examType.startsWith("Sample_")) {
                    val sampleLetter = examType.substringAfter("Sample_") // "A", "B", "C", "D"
                    val sampleQuestions = OfficialQuestions.getQuestionsForSampleExam(sampleLetter)
                    examPool.addAll(sampleQuestions)
                } else if (examType == "Chapter" && chapterId != null) {
                    // Fetch official questions for this chapter + generate fillers locally if needed
                    val officialForChap = OfficialQuestions.getQuestionsForChapter(chapterId)
                    examPool.addAll(officialForChap)
                    
                    // If official count is low, clone from existing with new random IDs to prevent keys clash instantly
                    while (examPool.size < totalQuestions) {
                        val cloned = officialForChap.randomOrNull() ?: OfficialQuestions.questions.random()
                        examPool.add(cloned.copy(id = cloned.id + "_" + (System.currentTimeMillis() + examPool.size)))
                    }
                } else if (examType == "Full" || totalQuestions == 40) {
                    // Complete Mock Exam (weighted exactly to syllabus specs)
                    // Chapter 1: 8 questions, Chapter 2: 6 questions, Chapter 3: 4 questions,
                    // Chapter 4: 11 questions, Chapter 5: 9 questions, Chapter 6: 2 questions. Total = 40.
                    val distribution = mapOf(1 to 8, 2 to 6, 3 to 4, 4 to 11, 5 to 9, 6 to 2)
                    for ((chap, count) in distribution) {
                        val chapQ = OfficialQuestions.getQuestionsForChapter(chap)
                        examPool.addAll(chapQ.shuffled().take(count))
                        
                        // If we need more to fulfill weightings, add local clones instantly
                        var chapAdded = chapQ.shuffled().take(count).size
                        while (chapAdded < count) {
                            val cloned = chapQ.randomOrNull() ?: OfficialQuestions.questions.random()
                            examPool.add(cloned.copy(id = cloned.id + "_w" + (System.currentTimeMillis() + examPool.size), chapterId = chap))
                            chapAdded++
                        }
                    }
                } else {
                    // Adaptive or custom length: pull randomly from all
                    val shuffledAll = OfficialQuestions.questions.shuffled()
                    examPool.addAll(shuffledAll.take(totalQuestions))
                    while (examPool.size < totalQuestions) {
                        val cloned = OfficialQuestions.questions.random()
                        examPool.add(cloned.copy(id = cloned.id + "_" + System.currentTimeMillis()))
                    }
                }

                val finalCount = if (examType.startsWith("Sample_")) examPool.size else totalQuestions
                finalizedQuestions = examPool.take(finalCount)
                
                // Save to cache for instant future retrieval
                examCache[cacheKey] = finalizedQuestions
            }

            activeExamQuestions.value = finalizedQuestions
            currentQuestionIndex.value = 0
            selectedOptionIndex.value = null
            isAnswerChecked.value = false
            answeredQuestionsList.clear()
            for (i in 0 until finalizedQuestions.size) {
                answeredQuestionsList.add(null)
            }

            examStartTime.value = System.currentTimeMillis()
            timerSeconds.value = 0
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                timerSeconds.value += 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    // Submit question option selected
    fun selectOption(index: Int) {
        if (!isAnswerChecked.value) {
            selectedOptionIndex.value = index
            answeredQuestionsList[currentQuestionIndex.value] = index
        }
    }

    fun checkAnswer() {
        if (selectedOptionIndex.value != null) {
            isAnswerChecked.value = true
        }
    }

    fun nextQuestion() {
        if (currentQuestionIndex.value < activeExamQuestions.value.size - 1) {
            currentQuestionIndex.value += 1
            selectedOptionIndex.value = answeredQuestionsList[currentQuestionIndex.value]
            isAnswerChecked.value = answeredQuestionsList[currentQuestionIndex.value] != null
        }
    }

    fun previousQuestion() {
        if (currentQuestionIndex.value > 0) {
            currentQuestionIndex.value -= 1
            selectedOptionIndex.value = answeredQuestionsList[currentQuestionIndex.value]
            isAnswerChecked.value = true // already checked if moving back
        }
    }

    // Finish Active Mock Exam and calculate results
    fun submitExamAndFinish(examType: String, totalQuestions: Int, chapterId: Int? = null) {
        stopTimer()
        val duration = timerSeconds.value.toLong()
        val now = System.currentTimeMillis()
        
        viewModelScope.launch(Dispatchers.IO) {
            val currentQuestions = activeExamQuestions.value
            var correctCount = 0
            for (i in 0 until currentQuestions.size) {
                val q = currentQuestions[i]
                val selected = answeredQuestionsList[i]
                val correct = selected == q.correctIndex
                if (correct) {
                    correctCount++
                }

                // Save question history to DB
                historyDao.insertHistory(
                    QuestionHistoryEntity(
                        questionId = q.id,
                        chapterId = q.chapterId,
                        isCorrect = correct,
                        responseTimeMs = (duration * 1000L) / currentQuestions.size, // average response time per question
                        timestamp = now,
                        confidenceLevel = 2 // Medium default
                    )
                )
            }

            val scorePercent = (correctCount * 100) / currentQuestions.size
            val passed = scorePercent >= 65 // ISTQB Passing line is 65% (26/40)

            val attempt = ExamAttemptEntity(
                timestamp = now,
                examType = examType,
                chapterId = chapterId,
                score = correctCount,
                totalQuestions = currentQuestions.size,
                timeSpentSeconds = duration,
                passed = passed
            )
            attemptDao.insertAttempt(attempt)

            // Award XP
            val xpGained = correctCount * 5 + (if (passed) 50 else 10)
            addXp(xpGained)

            if (passed && currentQuestions.size == 40) {
                unlockAchievement("EXAM_PASS")
            }
            if (correctCount == currentQuestions.size) {
                unlockAchievement("PERFECT_QUIZ")
            }

            // Retrieve the saved attempt row to pass the correct ID to the view
            val attempts = attemptDao.getAllAttemptsDirect()
            val savedAttemptId = attempts.firstOrNull()?.id ?: 1

            _screenState.value = ScreenState.MockExamReview(savedAttemptId)
        }
    }

    // Leitner Spaced Repetition Logic for Flashcards
    fun recordFlashcardPerformance(term: String, known: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingState = flashcardDao.getAllStatesDirect().find { it.term == term }
                ?: FlashcardStateEntity(term = term)

            // Move box: if known move up (max box 5), if unknown move to box 1
            val newBox = if (known) {
                kotlin.math.min(5, existingState.boxNumber + 1)
            } else {
                1
            }

            // Leitner box multipliers for review intervals:
            // Box 1: 1 hour, Box 2: 1 day, Box 3: 3 days, Box 4: 7 days, Box 5: 14 days
            val intervalDays = when (newBox) {
                1 -> 0 // Review again soon
                2 -> 1
                3 -> 3
                4 -> 7
                5 -> 14
                else -> 1
            }
            val nextReview = System.currentTimeMillis() + (intervalDays * 24 * 60 * 60 * 1000L)

            flashcardDao.insertState(
                existingState.copy(
                    boxNumber = newBox,
                    nextReviewTimestamp = nextReview
                )
            )

            // Award study XP
            addXp(5)
        }
    }

    // Reading sections tracking
    fun startReadingSection(sectionId: String) {
        activeSectionId.value = sectionId
        unlockAchievement("FIRST_STEPS")
        addXp(5)
    }

    // Admin Panel Actions
    fun triggerAdminQuestionGeneration(subject: String) {
        isGeneratingQuestion.value = true
        generatedQuestion.value = null
        viewModelScope.launch {
            val q = AiTutorService.generateAiQuestion(subject)
            generatedQuestion.value = q
            isGeneratingQuestion.value = false
        }
    }
}
