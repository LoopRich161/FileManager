package ru.looprich.filemanager

import android.app.Application
import ru.looprich.filemanager.database.DatabaseProvider
import ru.looprich.filemanager.database.dao.FilesDao

class App : Application() {

    lateinit var filesDao: FilesDao

    override fun onCreate() {
        super.onCreate()

        val db = DatabaseProvider.getDatabase(this)
        filesDao = db.filesDao()
    }

}