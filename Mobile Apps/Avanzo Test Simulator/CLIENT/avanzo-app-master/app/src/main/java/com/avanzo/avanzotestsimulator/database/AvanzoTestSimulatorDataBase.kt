package com.avanzo.avanzotestsimulator.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.avanzo.avanzotestsimulator.database.dao.NotesHistoryDao
import com.avanzo.avanzotestsimulator.database.dao.QuestionDao
import com.avanzo.avanzotestsimulator.database.dao.RankingDao
import com.avanzo.avanzotestsimulator.database.dao.SubjectDao
import com.avanzo.avanzotestsimulator.database.dao.UserDao
import com.avanzo.avanzotestsimulator.database.model.NotesHistoryModel
import com.avanzo.avanzotestsimulator.database.model.QuestionModel
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.database.model.UserModel

@Database(entities = [RankingModel::class, UserModel::class, SubjectModel::class, QuestionModel::class, NotesHistoryModel::class], version = 9)
abstract class AvanzoTestSimulatorDataBase : RoomDatabase() {

    //abstract fun answerDao(): AnswerDao
    abstract fun questionDao(): QuestionDao
    //abstract fun quizDao(): QuizDao
    abstract fun rankingDao(): RankingDao
    abstract fun subjectDao(): SubjectDao
    abstract fun userDao(): UserDao
    abstract fun notesHistoryDao(): NotesHistoryDao


    companion object {
        @Volatile
        private var INSTANCE: AvanzoTestSimulatorDataBase? = null

        //Si la instancia no existe se construye de manera segura
        fun newInstance(application: Application): AvanzoTestSimulatorDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    AvanzoTestSimulatorDataBase::class.java,
                    "avanzo_test_simulator"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}