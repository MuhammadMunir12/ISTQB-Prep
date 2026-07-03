package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getProgress(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getProgressDirect(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgressEntity)
}

@Dao
interface QuestionHistoryDao {
    @Query("SELECT * FROM question_history")
    fun getAllHistory(): Flow<List<QuestionHistoryEntity>>

    @Query("SELECT * FROM question_history")
    suspend fun getAllHistoryDirect(): List<QuestionHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: QuestionHistoryEntity)

    @Query("DELETE FROM question_history")
    suspend fun clearHistory()
}

@Dao
interface ExamAttemptDao {
    @Query("SELECT * FROM exam_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<ExamAttemptEntity>>

    @Query("SELECT * FROM exam_attempts ORDER BY timestamp DESC")
    suspend fun getAllAttemptsDirect(): List<ExamAttemptEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttemptEntity)
}

@Dao
interface FlashcardStateDao {
    @Query("SELECT * FROM flashcard_states")
    fun getAllStates(): Flow<List<FlashcardStateEntity>>

    @Query("SELECT * FROM flashcard_states")
    suspend fun getAllStatesDirect(): List<FlashcardStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: FlashcardStateEntity)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievementsDirect(): List<AchievementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Query("UPDATE achievements SET unlocked = 1, unlockedTimestamp = :timestamp WHERE id = :id")
    suspend fun unlockAchievement(id: String, timestamp: Long)
}
