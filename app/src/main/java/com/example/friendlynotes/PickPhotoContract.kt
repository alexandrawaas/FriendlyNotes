package com.example.friendlynotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContract
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.*

class PickPhotoContract: ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) return null
        val thumbnail: Bitmap? = intent?.getParcelableExtra("data")
        val fullPhotoUri: Uri? = intent?.data
        var result: String = ""

        if(thumbnail != null)
        { val baos = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        result = Base64.encodeToString(b, Base64.DEFAULT)}

        return result

    }

}