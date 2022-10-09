package com.example.friendlynotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.gson.Gson

class EditFriendContract : ActivityResultContract<Friend?, Friend>() {
    override fun createIntent(context: Context, input:Friend?) = Intent(context, EditFriendActivity::class.java).apply { putExtra("friend", Gson().toJson(input)) }

    override fun parseResult(resultCode: Int, intent: Intent?): Friend? {
        if (resultCode != Activity.RESULT_OK) return null

        val gson: Gson = Gson()
        return gson.fromJson(intent!!.getStringExtra("friend"), Friend::class.java)
    }
}