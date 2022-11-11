package com.example.friendlynotes

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.util.Base64
import android.view.View
import com.google.android.material.textview.MaterialTextView
import java.io.ByteArrayOutputStream

fun Any?.nullToString():String = this?.toString() ?: ""

fun String.stringToNull(): String?
{
    return if (this=="") null else this
}

fun MaterialTextView.hideIfNull(text: String?, activity: Activity, vararg views: View?)
{
    val textfield: MaterialTextView = activity.findViewById<MaterialTextView>(this.labelFor)
    if(text==null)
    {
        textfield.visibility = View.GONE
        this.visibility = View.GONE
        views.map { it?.visibility = View.GONE }
    }
    else textfield.text = text
}

fun Bitmap.encodeBase64():String?
{
    val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    if(this.byteCount > 1000) this.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        else
    {
            this.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    }
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun String.decodeBase64Image():Bitmap?
{
    //TODO: null Bedeutung throw?
    val imageByteArray:ByteArray=Base64.decode(this, 0)
    val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
    val squareDimension = bitmap.width.coerceAtMost(bitmap.height)
    return ThumbnailUtils.extractThumbnail(bitmap, squareDimension, squareDimension)
}