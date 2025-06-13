package id.my.sendiko.sembako.grocery.form.presentation.images

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.OutputStream

fun saveBitmapToMediaStore(context: Context, bitmap: Bitmap, displayName: String): Uri? {
    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    var imageUri: Uri? = null
    var outputStream: OutputStream? = null

    try {
        imageUri = context.contentResolver.insert(imageCollection, contentValues)
        imageUri?.let { uri ->
            outputStream = context.contentResolver.openOutputStream(uri)
            outputStream?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
            }
        }
    } catch (e: Exception) {
        // Handle exceptions, e.g., IOException
        e.printStackTrace()
        imageUri?.let {
            // Clean up if something went wrong
            context.contentResolver.delete(it, null, null)
        }
        imageUri = null
    } finally {
        outputStream?.close()
    }
    return imageUri
}