package com.firstapp.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView



import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout.TabGravity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag


class HomeActivity : AppCompatActivity() {
    private val TAG:String="CHECK_RESPONSE"

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
        val logoutButton = findViewById<ImageView>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // You can add your logout logic here
            // For example, navigate back to the login screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // For example, navigate back to the login screen
            finish()
        }

        // Create Profile button to navigate to ProfileActivity
        val createProfileButton = findViewById<ImageView>(R.id.createProfileButton)
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
            // fetch data from api
            getData()

            // Display the name and coding profile in the Home page
            val welcomeMessageTextView = findViewById<TextView>(R.id.messageTextView)
            val codingProfileTextView = findViewById<TextView>(R.id.messageTextView2)

            welcomeMessageTextView.text = "Welcome, $name!"
            codingProfileTextView.text = "Coding Profile: $codingProfile"

        }
    }

    private fun getData() {
        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<ResponseDataClass?> {
            override fun onResponse(
                call: Call<ResponseDataClass?>,
                response: Response<ResponseDataClass?>
            ) {
               if(response.isSuccessful)
               {
                   // getting 200
                   response.body()?.let {
                       // access the list of result
                       val resultList=it.result
                       for(resultX in resultList)
                       {
                           Log.i(TAG, "onResponse: ${resultX.firstName} ${resultX.lastName}")
                           Log.i(TAG, "Avatar: ${resultX.avatar}")
                           Log.i(TAG, "Contribution: ${resultX.contribution}")
                           // Add more properties as needed
                       }
                   }
               }
            }

            override fun onFailure(call: Call<ResponseDataClass?>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
