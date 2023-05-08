package ru.looprich.filemanager.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import ru.looprich.filemanager.MainActivity
import ru.looprich.filemanager.R
import ru.looprich.filemanager.utils.*
import java.io.File

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun FileItem(
    context: Context,
    file: File,
    lifecycleScope: LifecycleCoroutineScope
) {
    val fileIcon = getIconForFile(fileName = file.name)

    val size = getTextSize(file.length())
    val lastModified = dateFormat.format(file.lastModified())

    val fileEntity = MainActivity.app.filesDao.getFileByPath(file.absolutePath)
    val isModified = if (fileEntity != null) {
        println("path: ${file.absolutePath}, oldHash: ${fileEntity.oldHash}, newHash: ${fileEntity.newHash}")

        if (fileEntity.oldHash == null)
            false
        else
            fileEntity.oldHash != fileEntity.newHash
    } else {
        false
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = openFile(
                    context = context,
                    file = file,
                    lifecycleScope = lifecycleScope
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = fileIcon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(40.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$size | $lastModified" + if (isModified) " | обновлен" else "",
                fontSize = 14.sp
            )
        }

        IconButton(
            onClick = shareFile(
                context = context,
                file = file,
                lifecycleScope = lifecycleScope
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_share_24),
                contentDescription = "Share file"
            )
        }
    }
}

