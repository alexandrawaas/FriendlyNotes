package com.example.friendlynotes

import android.app.Activity
import android.view.View
import com.google.android.material.textview.MaterialTextView

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