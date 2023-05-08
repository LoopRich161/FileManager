package ru.looprich.filemanager.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.looprich.filemanager.R
import java.text.SimpleDateFormat

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