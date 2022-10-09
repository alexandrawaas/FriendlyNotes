package com.example.friendlynotes

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val context = this
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("firstStart", AppCompatActivity.MODE_PRIVATE)

        val button: Button = findViewById(R.id.button)

        val intent= Intent(this, MainActivity::class.java)

        if (!sharedPreferences.getBoolean("firstStart", true))
        {
            startActivity(intent)
            finish()
        }

        button.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("firstStart", false)
            editor.commit()
            startActivity(intent)
            finish()
        }
    }
}