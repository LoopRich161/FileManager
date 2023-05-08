package ru.looprich.filemanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.looprich.filemanager.utils.dateFormat
import ru.looprich.filemanager.utils.getIconForFile
import java.io.File

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun FileItem(
    file: File
) {
    val fileSize = file.length().toFloat() / 1024 / 1024

    val size = "%.2f MB".format(fileSize);
    val lastModified = dateFormat.format(file.lastModified())

    FileItem(name = file.name, size = size, lastModified = lastModified)
}

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun FileItem(
    name: String,
    size: String,
    lastModified: String
) {
    val fileIcon = getIconForFile(fileName = name)

    Row(
        modifier = Modifier.fillMaxWidth(),
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
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$size | $lastModified",
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewFileItem() {
    LazyColumn {
        item {
            FileItem(
                name = "file_name.txt",
                size = "1.23 MB",
                lastModified = "May 7, 2023"
            )
        }
    }
}
