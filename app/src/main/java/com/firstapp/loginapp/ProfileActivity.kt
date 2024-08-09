package com.firstapp.loginapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firstapp.loginapp.Database.DBHelper
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private val TAG:String="CHECK_RESPONSE"
    companion object {
        const val PROFILE_REQUEST_CODE = 1
        const val GALLERY_REQUEST_CODE = 123 // You can use any integer value

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val fetchDataButton = findViewById<Button>(R.id.fetchDataButton)
        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val platformNameEditText = findViewById<EditText>(R.id.editTextPlatformName)
        val platformIdEditText = findViewById<EditText>(R.id.editTextPlatformId)
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val uid = firebaseUser?.uid
        val UserName=firebaseUser?.email

        findViewById<TextView>(R.id.editTextName).text="Name: $UserName"
        // here when photo upload then


        val dbHelper = DBHelper(this)
// read data from database
        val cursor = dbHelper.readUserProfile()
        var check = 0
        while (cursor.moveToNext()) {
            val uidIndex = cursor.getColumnIndex("Uid")
            val nameIndex = cursor.getColumnIndex("Name")
            val imageUriIndex = cursor.getColumnIndex("ImageUri")

            val storedUid = cursor.getString(uidIndex)
            val storedName = cursor.getString(nameIndex)
            val storedImageUri = cursor.getString(imageUriIndex)

            // Do something with the retrieved data
            Log.i(TAG, "Stored UID: $storedUid")
            Log.i(TAG, "Stored Name: $storedName")
            Log.i(TAG, "Stored ImageUri: $storedImageUri")
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val uid = firebaseUser?.uid
            val UserName = firebaseUser?.email

            if (storedUid == uid) {
                val selectedImageUri = storedImageUri
                val imageUri = Uri.parse(selectedImageUri)
                // update profile
                Glide.with(applicationContext)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
                    .placeholder(R.drawable.avatar_ek23) // Placeholder image while loading
                    .error(R.drawable.avatar_ek11) // Error image if loading fails
                    .into(findViewById(R.id.profileImage))



                findViewById<TextView>(R.id.editTextUsername).text = "UserName: $storedName"


            }
        }


            val createProfileButton = findViewById<ImageView>(R.id.profileImage)
            createProfileButton.setOnClickListener {
                // Launch the image picker intent
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, ProfileActivity.GALLERY_REQUEST_CODE)
            }












            fetchDataButton.setOnClickListener {
                // Get the entered data
                val name = nameEditText.text.toString()
                val username = usernameEditText.text.toString()
                val platformName = platformNameEditText.text.toString()
                val platformId = platformIdEditText.text.toString()




                if (name.isEmpty() || username.isEmpty() || platformName.isEmpty() || platformId.isEmpty()) {
                    // Show an error message or take appropriate action
                    showToast(this, "All fields are required")
                    Log.i(TAG, "Error: All fields are required")
                    return@setOnClickListener
                }

                // You can perform any additional validation or data processing here

                // Perform profile saving logic here
                // For simplicity, we'll just finish the activity indicating success

                // Create an intent to pass back the data to the calling activity
                val resultIntent = Intent()
                resultIntent.putExtra("NAME", name)
                resultIntent.putExtra("USERNAME", username)
                resultIntent.putExtra("PLATFORM_NAME", platformName)
                resultIntent.putExtra("PLATFORM_ID", platformId)

                // Set the result and finish the activity
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ProfileActivity.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the result from the image picker
            val selectedImageUri: Uri? = data?.data

            Log.i(TAG, "imageUri: $selectedImageUri")


            // select image
            // if exist then update
            // else insert

            val  dbHelper = DBHelper(this)
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val uid: String? = firebaseUser?.uid
            val name: String = ""

            // select
            if (uid != null) {
                val confirm = dbHelper.readCustomUserProfile(uid)

                if (confirm.moveToFirst()) {
                    // User profile exists
                    // Run the update query
                    dbHelper.updateImageUri(uid, selectedImageUri)
                } else {
                    Log.i(TAG, "Image not found in storage: user not registerd yet")
                    dbHelper.insertImageCodingProfile(uid,selectedImageUri)
                }
                confirm.close() // Close the cursor after using it
            }









            // Update your UI or upload the image to your server
            // Example: Glide.with(applicationContext).load(selectedImageUri).into(createProfileButton)

            // Display the selected image in the createProfileButton ImageView
            Glide.with(applicationContext)
                .load(selectedImageUri)
                .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
                .placeholder(R.drawable.avatar_ek23) // Placeholder image while loading
                .error(R.drawable.avatar_ek11) // Error image if loading fails
                .into(findViewById(R.id.profileImage))

        }
    }
    }





