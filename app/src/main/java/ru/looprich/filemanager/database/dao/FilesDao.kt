package ru.looprich.filemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.looprich.filemanager.database.entity.FileEntity

@Dao
interface FilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(file: FileEntity)

    @Query("SELECT * FROM files WHERE path = :path")
    fun getFileByPath(path: String): FileEntity?

}
