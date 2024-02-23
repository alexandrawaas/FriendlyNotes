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
        val buttonLicence: Button = findViewById(R.id.buttonLicence)

        buttonDeveloper.setOnClickListener {
            val implicitIntentDeveloper = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/alexandrawaas"))
            startActivity(implicitIntentDeveloper)
        }

        buttonLicence.setOnClickListener {
            val implicitIntentLicence = Intent(Intent.ACTION_VIEW, Uri.parse("https://raw.githubusercontent.com/alexandrawaas/FriendlyNotes/main/app/src/main/licence"))
            startActivity(implicitIntentLicence)
        }
    }
}