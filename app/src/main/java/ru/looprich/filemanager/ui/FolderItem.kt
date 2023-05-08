package ru.looprich.filemanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.looprich.filemanager.R
import ru.looprich.filemanager.utils.dateFormat
import java.io.File

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun FolderItem(folder: File, onClick: () -> Unit) {
    val lastModified = dateFormat.format(folder.lastModified())

    FolderItem(name = folder.name, lastModified = lastModified, onClick = onClick)
}

@Composable
fun FolderItem(name: String, lastModified: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_folder_24),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = lastModified,
                color = MaterialTheme.colors.onSurface.copy(0.6f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewFolderItem() {
    LazyColumn {
        item {
            FolderItem(
                name = "folder",
                lastModified = "May 8, 2023"
            ) {}
        }
    }
}
