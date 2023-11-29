package com.firstapp.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView



import android.app.Activity


class HomeActivity : AppCompatActivity() {

    companion object {
        const val PROFILE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Assuming you have passed the user's name from the login activity
        val userName = intent.getStringExtra("USER_NAME")

        // Display the user's name in a TextView
        val welcomeMessage = findViewById<TextView>(R.id.welcomeMessage)
        welcomeMessage.text = "Welcome, $userName!"

        // Logout button to simulate a logout action
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // You can add your logout logic here
            // For example, navigate back to the login screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // For example, navigate back to the login screen
            finish()
        }

        // Create Profile button to navigate to ProfileActivity
        val createProfileButton = findViewById<Button>(R.id.createProfileButton)
        createProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivityForResult(intent, PROFILE_REQUEST_CODE)
        }
    }

    // Handle the result from ProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data from the intent
            val name = data?.getStringExtra("NAME")
            val codingProfile = data?.getStringExtra("CODING_PROFILE")

            // Display the name and coding profile in the Home page
            val profileInfoTextView = findViewById<TextView>(R.id.profileInfoTextView)
            profileInfoTextView.text = "Name: $name\nCoding Profile: $codingProfile"
        }
    }
}
