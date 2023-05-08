package ru.looprich.filemanager.ui.screen

import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import ru.looprich.filemanager.R
import ru.looprich.filemanager.ui.FileItem
import ru.looprich.filemanager.ui.FolderItem
import ru.looprich.filemanager.ui.UpFolderItem
import ru.looprich.filemanager.utils.getFileCreationTime
import java.io.File
import java.io.FileFilter

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun MainScreen(
) {
    var sortCriteria by remember { mutableStateOf(SortCriteria.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASCENDING) }

    var currentFolder: File by remember { mutableStateOf(Environment.getExternalStorageDirectory()) }

    val allFiles = currentFolder.listFiles(FileFilter { !it.isHidden }) ?: arrayOf<File>()
    val files =
        allFiles.filterNot { it.isDirectory }.sortedWith(getComparator(sortCriteria, sortOrder))
    val folders =
        allFiles.filter { it.isDirectory }.sortedWith(getComparator(sortCriteria, sortOrder))

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifecycleOwner.lifecycleScope

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    sortCriteria =
                        if (sortCriteria == SortCriteria.SIZE) SortCriteria.NAME else SortCriteria.SIZE
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (sortCriteria == SortCriteria.SIZE) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
                    contentColor = if (sortCriteria == SortCriteria.SIZE) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground
                )
            ) {
                Text("Размер")
            }

            Button(
                onClick = {
                    sortCriteria =
                        if (sortCriteria == SortCriteria.DATE) SortCriteria.NAME else SortCriteria.DATE
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (sortCriteria == SortCriteria.DATE) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
                    contentColor = if (sortCriteria == SortCriteria.DATE) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground
                )
            ) {
                Text("Дата")
            }

            Button(
                onClick = {
                    sortCriteria =
                        if (sortCriteria == SortCriteria.TYPE) SortCriteria.NAME else SortCriteria.TYPE
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (sortCriteria == SortCriteria.TYPE) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
                    contentColor = if (sortCriteria == SortCriteria.TYPE) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground
                )
            ) {
                Text("Расширение")
            }

            Button(
                onClick = {
                    sortOrder =
                        if (sortOrder == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground
                )
            ) {
                Icon(
                    if (sortOrder == SortOrder.ASCENDING) ImageVector.vectorResource(id = R.drawable.baseline_arrow_upward_24) else ImageVector.vectorResource(
                        id = R.drawable.baseline_arrow_downward_24
                    ),
                    contentDescription = if (sortOrder == SortOrder.ASCENDING) "Ascending" else "Descending"
                )
            }
        }

        Divider(modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            if (currentFolder != Environment.getExternalStorageDirectory()) {
                item {
                    UpFolderItem {
                        currentFolder =
                            currentFolder.parentFile ?: Environment.getExternalStorageDirectory()
                    }
                }
            }
            items(folders.size) { index ->
                val folder = folders[index]
                FolderItem(folder = folder) {
                    currentFolder = folder
                }
            }
            items(files.size) { index ->
                FileItem(
                    context = LocalContext.current,
                    file = files[index],
                    lifecycleScope = lifecycleScope
                )
            }
        }
    }

}

// -----------------------------------------------------------------------------------------------------------------
fun getComparator(sortCriteria: SortCriteria, sortOrder: SortOrder): Comparator<File> {
    return when (sortCriteria) {
        SortCriteria.NAME -> compareBy<File> { it.name }
        SortCriteria.SIZE -> compareBy<File> { it.length() }
        SortCriteria.DATE -> compareBy<File> { getFileCreationTime(it) }
        SortCriteria.TYPE -> compareBy<File> { it.name.substringAfterLast(".") }
    }.let { comparator ->
        if (sortOrder == SortOrder.DESCENDING) comparator.reversed() else comparator
    }
}


// -----------------------------------------------------------------------------------------------------------------
enum class SortCriteria {
    NAME, SIZE, DATE, TYPE
}

// -----------------------------------------------------------------------------------------------------------------
enum class SortOrder {
    ASCENDING, DESCENDING
}