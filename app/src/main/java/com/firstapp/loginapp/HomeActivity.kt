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

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
            if (codingProfile != null) {
                getData(codingProfile)
            }

            // Display the name and coding profile in the Home page
//            val welcomeMessageTextView = findViewById<TextView>(R.id.messageTextView)
//            val codingProfileTextView = findViewById<TextView>(R.id.messageTextView2)
//
//            welcomeMessageTextView.text = "Welcome, $name!"
//            codingProfileTextView.text = "Coding Profile: $codingProfile"

        }
    }

    private fun getData( coding:String) {
        RetrofitInstance.apiInterface.getData(coding).enqueue(object : Callback<ResponseDataClass?> {
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
                           Log.i(TAG, "onResponse:")
                           Log.i(TAG, "  firstName: ${resultX.firstName}")
                           Log.i(TAG, "  lastName: ${resultX.lastName}")
                           Log.i(TAG, "  lastOnlineTimeSeconds: ${resultX.lastOnlineTimeSeconds}")
                           Log.i(TAG, "  rating: ${resultX.rating}")
                           Log.i(TAG, "  friendOfCount: ${resultX.friendOfCount}")
                           Log.i(TAG, "  titlePhoto: ${resultX.titlePhoto}")
                           Log.i(TAG, "  handle: ${resultX.handle}")
                           Log.i(TAG, "  avatar: ${resultX.avatar}")
                           Log.i(TAG, "  contribution: ${resultX.contribution}")
                           Log.i(TAG, "  organization: ${resultX.organization}")
                           Log.i(TAG, "  rank: ${resultX.rank}")
                           Log.i(TAG, "  maxRating: ${resultX.maxRating}")
                           Log.i(TAG, "  registrationTimeSeconds: ${resultX.registrationTimeSeconds}")
                           Log.i(TAG, "  maxRank: ${resultX.maxRank}")
                           // Add more properties as needed
                       }

                       if (!resultList.isNullOrEmpty()) {
                           val firstResult = resultList[0]
                           // now handling to display image



                           val responseData = response.body()

                           // Assuming responseData is not null and has an avatar property
                           val avatarUrl = responseData?.result?.get(0)?.avatar

                           // Load the avatar image into the ImageView using Glide (or Picasso)
                           Glide.with(applicationContext)
                               .load(avatarUrl)
                               .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
                               .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                               .error(R.drawable.baseline_person_24) // Error image if loading fails
                               .into(findViewById(R.id.createProfileButton))

                           // Set the text for dynamically created TextViews
                           findViewById<TextView>(R.id.firstNameTextView).text = "First Name: ${firstResult.firstName}"
                           findViewById<TextView>(R.id.lastNameTextView).text = "Last Name: ${firstResult.lastName}"
                           findViewById<TextView>(R.id.handleTextView).text = "Handle: ${firstResult.handle}"
                           findViewById<TextView>(R.id.ratingTextView).text = "Rating: ${firstResult.rating}"
                           findViewById<TextView>(R.id.rankTextView).text = "Rank: ${firstResult.rank}"
                           findViewById<TextView>(R.id.maxratingTextView).text = "MaxRating: ${firstResult.maxRating}"
                           findViewById<TextView>(R.id.maxrankTextView).text = "MaxRank: ${firstResult.maxRank}"

                           // Set more TextViews for other properties as needed
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
