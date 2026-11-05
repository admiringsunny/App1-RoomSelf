package com.learn.app1_roomself.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.app1_roomself.dao.StudentDao
import com.learn.app1_roomself.models.Student

@Database(
    entities = [Student::class],
    version = 1,
    exportSchema = false
)
abstract class StudentDB: RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object{

        @Volatile
        private var INSTANCE: StudentDB? = null

        fun getDatabase(context: Context): StudentDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDB::class.java,
                    "student_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}