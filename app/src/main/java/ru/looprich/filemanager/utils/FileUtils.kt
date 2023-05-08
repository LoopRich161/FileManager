package ru.looprich.filemanager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.looprich.filemanager.R
import java.io.File
import java.text.SimpleDateFormat
import kotlin.math.log10
import kotlin.math.pow

// -----------------------------------------------------------------------------------------------------------------
val dateFormat = SimpleDateFormat("dd.MM.yyyy")

// -----------------------------------------------------------------------------------------------------------------
@Composable
fun getIconForFile(fileName: String): ImageVector {
    // MimeType возможно и подошел бы сюда, но в ТЗ написано про расширения :)
    return when (fileName.substringAfterLast(".")) {
        "pdf" -> ImageVector.vectorResource(id = R.drawable.baseline_picture_as_pdf_24)
        "txt" -> ImageVector.vectorResource(id = R.drawable.baseline_description_24)
        "zip", "rar", "7z" -> ImageVector.vectorResource(id = R.drawable.baseline_folder_zip_24)
        "jpg", "jpeg", "png" -> ImageVector.vectorResource(id = R.drawable.baseline_image_24)
        "doc", "docx" -> ImageVector.vectorResource(id = R.drawable.baseline_description_24)
        "xls", "xlsx" -> ImageVector.vectorResource(id = R.drawable.baseline_description_24)
        "ppt", "pptx" -> ImageVector.vectorResource(id = R.drawable.baseline_description_24)
        else -> ImageVector.vectorResource(id = R.drawable.baseline_file_copy_24)
    }
}

// -----------------------------------------------------------------------------------------------------------------
fun getTextSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return String.format("%.1f %s", size / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}


// -----------------------------------------------------------------------------------------------------------------
fun getMimeTypeFromFile(file: File): String? {
    val uri = Uri.fromFile(file)
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
}

// -----------------------------------------------------------------------------------------------------------------
fun openFile(
    context: Context,
    file: File,
    lifecycleScope: LifecycleCoroutineScope
): () -> Unit {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(file.absolutePath), getMimeTypeFromFile(file))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    return {
        lifecycleScope.launch(Dispatchers.IO) {
            context.startActivity(intent)
        }
    }
}

// -----------------------------------------------------------------------------------------------------------------
fun shareFile(
    context: Context, file: File,
    lifecycleScope: LifecycleCoroutineScope
): () -> Unit {
//    val uri = FileProvider.getUriForFile(
//        context,
//        context.applicationContext.packageName + ".provider",
//        file
//    )
//    val shareIntent = Intent(Intent.ACTION_SEND)
//    shareIntent.type = getMimeType(file.absolutePath)
//    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
//    context.startActivity(Intent.createChooser(shareIntent, "Share file using"))

    val intent = Intent(Intent.ACTION_SEND).apply {
        setDataAndType(Uri.parse(file.absolutePath), getMimeTypeFromFile(file))
    }

    return {
        lifecycleScope.launch(Dispatchers.IO) {
            context.startActivity(intent)
        }
    }
}

