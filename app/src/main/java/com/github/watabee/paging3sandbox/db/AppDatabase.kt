package com.github.watabee.paging3sandbox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.watabee.paging3sandbox.data.User
import com.github.watabee.paging3sandbox.data.UserManagement

@Database(
    entities = [User::class, UserManagement::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            database ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                    .build()
                    .also { database = it }
            }
    }
}