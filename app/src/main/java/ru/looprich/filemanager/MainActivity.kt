package ru.looprich.filemanager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.looprich.filemanager.database.entity.FileEntity
import ru.looprich.filemanager.ext.md5
import ru.looprich.filemanager.ui.screen.MainScreen
import ru.looprich.filemanager.ui.theme.FileManagerTheme
import java.io.File

class MainActivity : AppCompatActivity() {

    // -----------------------------------------------------------------------------------------------------------------
    private val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val readPermissionRequestCode = 161

    // -----------------------------------------------------------------------------------------------------------------
    companion object {
        lateinit var app: App
    }

    // -----------------------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as App

        if (!hasReadPermission()) {
            requestReadPermission()
        } else {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val files = scanFiles(Environment.getExternalStorageDirectory())
                    files.forEach {
                        val path = it.absolutePath
                        val oldEntity = app.filesDao.getFileByPath(path)
                        val newHash = it.md5()

                        val newEntity: FileEntity = if (oldEntity != null) {
                            val oldHash = oldEntity.newHash
                            FileEntity(path = path, oldHash = oldHash, newHash = newHash)
                        } else {
                            FileEntity(path = path, oldHash = null, newHash = newHash)
                        }

                        if (!newEntity.oldHash.equals(newEntity.newHash)) {
                            app.filesDao.insertFile(newEntity)
                        }
                    }
                }

            }

            setContent {
                FileManagerTheme {
                    MainScreen()
                }
            }

        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private suspend fun scanFiles(directory: File): List<File> = withContext(Dispatchers.IO) {
        val files = mutableListOf<File>()
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                files.addAll(scanFiles(file))
            } else if (!file.isHidden) {
                files.add(file)
            }
        }
        return@withContext files
    }

    // -----------------------------------------------------------------------------------------------------------------
    private fun hasReadPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            readPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // -----------------------------------------------------------------------------------------------------------------
    private fun requestReadPermission() {
        if (shouldShowRequestPermissionRationale(readPermission)) {
            setContent {
                WhiteScreen(
                    message = "Вам необходимо предоставить разрешение на чтение, чтобы использовать это приложение.",
                    buttonText = "Предоставить разрешение",
                    onClick = {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(readPermission),
                            readPermissionRequestCode
                        )
                    }
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readPermission),
                readPermissionRequestCode
            )
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == readPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setContent {
                    FileManagerTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    WhiteScreen(
                        message = "Разрешение на чтение было отклонено. Вы можете снова предоставить разрешение в настройках приложения.",
                        buttonText = "Предоставить разрешение",
                        onClick = {
                            requestReadPermission()
                        }
                    )
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Composable
    fun WhiteScreen(message: String, buttonText: String, onClick: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(message)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onClick) {
                    Text(buttonText)
                }
            }
        }
    }
}
