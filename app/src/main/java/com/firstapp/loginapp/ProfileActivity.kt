package com.firstapp.loginapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val saveProfileButton = findViewById<Button>(R.id.saveProfileButton)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val codingProfileEditText = findViewById<EditText>(R.id.codingProfileEditText)

        saveProfileButton.setOnClickListener {
            // Get the entered data
            val name = nameEditText.text.toString()
            val codingProfile = codingProfileEditText.text.toString()

            // You can perform any additional validation or data processing here


            // Perform profile saving logic here
            // For simplicity, we'll just finish the activity indicating success

            // Create an intent to pass back the data to HomeActivity
            val resultIntent = Intent()
            resultIntent.putExtra("NAME", name)
            resultIntent.putExtra("CODING_PROFILE", codingProfile)

            // Set the result and finish the activity
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
