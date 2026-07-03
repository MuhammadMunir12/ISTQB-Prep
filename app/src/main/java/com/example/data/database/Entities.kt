package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1, // Single user row
    val xp: Int = 0,
    val level: Int = 1,
    val streakDays: Int = 0,
    val lastStudyTimestamp: Long = 0,
    val totalTimeSpentSeconds: Long = 0,
    val dailyGoalXp: Int = 50,
    val todayXp: Int = 0
)

@Entity(tableName = "question_history")
data class QuestionHistoryEntity(
    @PrimaryKey val questionId: String,
    val chapterId: Int,
    val isCorrect: Boolean,
    val responseTimeMs: Long,
    val timestamp: Long,
    val confidenceLevel: Int // 1=Low, 2=Medium, 3=High
)

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val examType: String, // "10", "20", "40", "Chapter", "Adaptive"
    val chapterId: Int?, // null if complete exam
    val score: Int,
    val totalQuestions: Int,
    val timeSpentSeconds: Long,
    val passed: Boolean
)

@Entity(tableName = "flashcard_states")
data class FlashcardStateEntity(
    @PrimaryKey val term: String,
    val boxNumber: Int = 1, // Leitner box 1 to 5
    val nextReviewTimestamp: Long = 0
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val unlocked: Boolean = false,
    val unlockedTimestamp: Long = 0
)
