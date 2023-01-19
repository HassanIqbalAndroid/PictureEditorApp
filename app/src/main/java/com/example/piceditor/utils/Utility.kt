package com.example.piceditor.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.piceditor.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create and temp file and return its uri.
 */
fun getTempFileUri(context: Context): Uri {

    val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())

    val tempFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", context.cacheDir)

    return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tempFile)
}