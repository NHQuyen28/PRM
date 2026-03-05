package com.example.prm.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.InputStream

object ImageUtil {

    fun uriToBase64(context: Context, uri: Uri): String {

        val inputStream: InputStream? =
            context.contentResolver.openInputStream(uri)

        val bytes = inputStream?.readBytes()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}