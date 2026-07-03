package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProgressEntity::class,
        QuestionHistoryEntity::class,
        ExamAttemptEntity::class,
        FlashcardStateEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProgressDao(): UserProgressDao
    abstract fun questionHistoryDao(): QuestionHistoryDao
    abstract fun examAttemptDao(): ExamAttemptDao
    abstract fun flashcardStateDao(): FlashcardStateDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "istqb_prep_database"
                )
                .fallbackToDestructiveMigration() // Simple for prototype/learning apps
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
