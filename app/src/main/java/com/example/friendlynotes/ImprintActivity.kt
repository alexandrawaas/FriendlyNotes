package com.example.friendlynotes

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ImprintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprint)

        val buttonDeveloper: Button = findViewById(R.id.buttonDeveloper)

        val intent= Intent(this, MainActivity::class.java)

        buttonDeveloper.setOnClickListener {
            val implicitIntentDeveloper = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/alexandrawaas"))
            startActivity(implicitIntentDeveloper)
        }
    }
}