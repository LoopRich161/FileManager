package ru.looprich.filemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.looprich.filemanager.database.dao.FilesDao
import ru.looprich.filemanager.database.entity.FileEntity

@Database(entities = [FileEntity::class], version = 1)
abstract class FilesDatabase : RoomDatabase() {
    abstract fun filesDao(): FilesDao
}

// -----------------------------------------------------------------------------------------------------------------
object DatabaseProvider {
    private var instance: FilesDatabase? = null

    fun getDatabase(context: Context): FilesDatabase {
        synchronized(this) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilesDatabase::class.java,
                    "files_database"
                ).allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}

