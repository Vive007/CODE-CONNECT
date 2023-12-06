package com.firstapp.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView



import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firstapp.loginapp.Database.DBHelper
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private val TAG:String="CHECK_RESPONSE"

    companion object {
        const val PROFILE_REQUEST_CODE = 0
        const val GALLERY_REQUEST_CODE = 1 // You can use any integer value
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // accessing data from different database.


        val auth = FirebaseAuth.getInstance()

// Check if a user is signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, you can access user information
            val name = currentUser.displayName
            val email = currentUser.email
            val phoneNumber = currentUser.phoneNumber
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val uid = firebaseUser?.uid
            Log.i(TAG, "UniqueID: ${uid}")// getting UniqueID of user

            // if this uid present in data base uid-> present in profile table means rgisterd user







            // don't move to create profile i have to  call the api wwth therir coding id

            val  dbHelper = DBHelper(this)

// Insert data into USERPROFILE table
//            val Uid: String = "71gx3zSfOcdm2k5BQFibLPVed6Z2"
//            val Name: String = "vivek kumar"
//            val ImageUri:String = "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000034/ORIGINAL/NONE/image%2Fjpeg/1848661045"
//
//            val insertedId = dbHelper.insertUserProfile(Uid, Name, ImageUri)

// read data from database
            val cursor = dbHelper.readUserProfile()
            var check =0
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

                if(storedUid==uid)
                {
                    val selectedImageUri =storedImageUri
                    // update profile
                    Glide.with(applicationContext)
                        .load(selectedImageUri)
                        .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
                        .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                        .error(R.drawable.baseline_person_24) // Error image if loading fails
                        .into(findViewById(R.id.createProfileButton))

                    // now call the api
                    if (storedUid != null) {
                        val cursor = dbHelper.readCodingProfile()

                        while (cursor.moveToNext()) {
                            val codingProfileIdIndex = cursor.getColumnIndex("Id")
                            val uidIndex = cursor.getColumnIndex("Uid")
                            val nameIndex = cursor.getColumnIndex("Pname")
                            val codingProfileIndex = cursor.getColumnIndex("Pid")

                            val codingProfileId = cursor.getInt(codingProfileIdIndex)
                            val storedUid = cursor.getString(uidIndex)
                            val storedName = cursor.getString(nameIndex)
                            val storedCodingProfile = cursor.getString(codingProfileIndex)

                            // Do something with the retrieved data
                            Log.i(TAG, "Coding Profile ID: $codingProfileId")
                            Log.i(TAG, "Stored UID: $storedUid")
                            Log.i(TAG, "Stored Name: $storedName")
                            Log.i(TAG, "Stored Coding Profile: $storedCodingProfile")


                            // calling api
                            if(storedName=="codeforces")
                            {
                                getData(storedCodingProfile)
                            }else if(storedName=="leetcode")
                            {
                                getLeetcodeData(storedCodingProfile)

                            }else
                            {
                                Log.i(TAG, "error message: Your coding profile not found")
                            }
                        }

                        cursor.close()
                    } else {
                        Log.e(TAG, "User UID is null")
                    }








                }
            }

            cursor.close()










            // Use the retrieved information as needed
            // ...

            // Note: displayName and phoneNumber might be null if not set by the user
        }

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

        // create profile to upload image
        val createProfileButton = findViewById<ImageView>(R.id.createProfileButton)
        createProfileButton.setOnClickListener {
            // Launch the image picker intent
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        // Create Profile button to navigate to ProfileActivity
        val createCodingProfileButton = findViewById<ImageView>(R.id.createCodingProfile)
        createCodingProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivityForResult(intent, PROFILE_REQUEST_CODE)
        }

//        val imagePath = "/home/vivek007/Desktop/admin.jpg"
//
//// Load the image using Glide
//        Glide.with(this)
//            .load("file://$imagePath") // Use file:// scheme for local files
//            .into(createProfileButton)

        // if signed user already submitted their coding platform and profile id will be displayed here
    }

    // Handle the result from ProfileActivity
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the result from ProfileActivity
            val name: String? = data?.getStringExtra("NAME")?.lowercase()
            val codingProfile: String? = data?.getStringExtra("CODING_PROFILE")

            if (name != null && codingProfile != null) {
                // now insert the coding data to database
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val uid: String? = firebaseUser?.uid

                if (uid != null) {
                    val dbHelper = DBHelper(this)
                    // select
                    val getiingId =dbHelper.getIdCodingProfile(uid,name,codingProfile);
                    //update
                    if (getiingId != null) {
                        // Uid found, do something with it
                        // run the update query
                        val rowsAffected=  dbHelper.updateCodingProfile(getiingId,uid,name,codingProfile)

                        if (rowsAffected > 0) {
                            // Update was successful, and rowsAffected contains the number of rows updated
                            Log.i(TAG, "Coding profile updated successfully. Rows affected: $rowsAffected")
                        } else {
                            // Update failed
                            Log.e(TAG, "Failed to update coding profile.")
                        }


//                        Log.i(TAG, "Uid found: $resultUid")
                        // so run the update query
                    } else {
                        // Uid not found
                        // run the insert query
                        dbHelper.insertCodingProfile(uid,name,codingProfile)


                    }
                    // insert



//                    dbHelper.insertCodingProfile(uid, name, codingProfile)
                } else {
                    Log.e(TAG, "User UID is null")
                }
            } else {
                Log.e(TAG, "Name or codingProfile is null")
            }



















            if (codingProfile != null) {
                // if name is codeforces then call getdata
                if(name=="codeforces")
                {
                    getData(codingProfile);
                }else if(name=="leetcode")
                {
                    getLeetcodeData(codingProfile)

                }else
                {
                    Log.i(TAG, "error message: Your coding profile not found")
                }
//                getData(codingProfile)
//                getLeetcodeData(codingProfile)
            }
        } else if (requestCode == PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            // Handle if the user canceled the operation (e.g., pressing the back button)
            // Add any specific logic you want to execute in this case
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the result from the image picker
            val selectedImageUri: Uri? = data?.data
            Log.i(TAG, "imageUri: $selectedImageUri")


            // select image
            // if exist then update
            // else insert

            val  dbHelper = DBHelper(this)
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val uid: String? = firebaseUser?.uid
            val name: String = "vivek"

            // select
            if (uid != null) {
                val confirm = dbHelper.readCustomUserProfile(uid)
                if (confirm.moveToFirst()) {
                    // User profile exists
                    // Run the update query
                    dbHelper.updateImageUri(uid, selectedImageUri)
                } else {
                    // User profile doesn't exist
                    // Run the insert query
                    dbHelper.insertUserProfile(uid, name, selectedImageUri)
                }
                confirm.close() // Close the cursor after using it
            }









            // Update your UI or upload the image to your server
            // Example: Glide.with(applicationContext).load(selectedImageUri).into(createProfileButton)

            // Display the selected image in the createProfileButton ImageView
            Glide.with(applicationContext)
                .load(selectedImageUri)
                .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
                .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                .error(R.drawable.baseline_person_24) // Error image if loading fails
                .into(findViewById(R.id.createProfileButton))

        }
    }

    private fun getLeetcodeData(profileId:  String) {
        RetrofitLeetcode.leetCodeApiInterface.getData(profileId)
            .enqueue(object : Callback<ResponseDataLeetcodeClass?> {
                override fun onResponse(
                    call: Call<ResponseDataLeetcodeClass?>,
                    response: Response<ResponseDataLeetcodeClass?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseData ->
                            // Display the data
                            logLeetCodeData(responseData)
                        }
                    } else {
                        // Handle unsuccessful response (e.g., show an error message)
                        Log.e(TAG, "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseDataLeetcodeClass?>, t: Throwable) {
                    // Handle failure (e.g., show an error message)
                    Log.e(TAG, "Request failed", t)
                }
            })


    }

    private fun logLeetCodeData(responseData: ResponseDataLeetcodeClass) {
        Log.i(TAG, "contributionPoint: ${responseData.contributionPoint}")
        Log.i(TAG, "easySolved: ${responseData.easySolved}")
        Log.i(TAG, "hardSolved: ${responseData.hardSolved}")
        Log.i(TAG, "mediumSolved: ${responseData.mediumSolved}")
        Log.i(TAG, "ranking: ${responseData.ranking}")
        Log.i(TAG, "reputation: ${responseData.reputation}")

        // Log submissionCalendar data
        Log.i(TAG, "submissionCalendar: ${responseData.submissionCalendar}")

        Log.i(TAG, "totalEasy: ${responseData.totalEasy}")
        Log.i(TAG, "totalHard: ${responseData.totalHard}")
        Log.i(TAG, "totalMedium: ${responseData.totalMedium}")
        Log.i(TAG, "totalQuestions: ${responseData.totalQuestions}")
        Log.i(TAG, "totalSolved: ${responseData.totalSolved}")

        // Log totalSubmissions list
        for (submission in responseData.totalSubmissions) {
            Log.i(TAG, "Submission - difficulty: ${submission.difficulty}")
            Log.i(TAG, "Submission - count: ${submission.count}")
            Log.i(TAG, "Submission - submissions: ${submission.submissions}")
        }


    }


    // Display the name and coding profile in the Home page
//            val welcomeMessageTextView = findViewById<TextView>(R.id.messageTextView)
//            val codingProfileTextView = findViewById<TextView>(R.id.messageTextView2)
//
//            welcomeMessageTextView.text = "Welcome, $name!"
//            codingProfileTextView.text = "Coding Profile: $codingProfile"



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
//                           val avatarUrl = responseData?.result?.get(0)?.avatar
//
//                           // Load the avatar image into the ImageView using Glide (or Picasso)
//                           Glide.with(applicationContext)
//                               .load(avatarUrl)
//                               .apply(RequestOptions.circleCropTransform()) // Optional: Apply a circular transformation
//                               .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
//                               .error(R.drawable.baseline_person_24) // Error image if loading fails
//                               .into(findViewById(R.id.createProfileButton))

//                           // Set the text for dynamically created TextViews
//                           findViewById<TextView>(R.id.firstNameTextView).text = "First Name: ${firstResult.firstName}"
//                           findViewById<TextView>(R.id.lastNameTextView).text = "Last Name: ${firstResult.lastName}"
//                           findViewById<TextView>(R.id.handleTextView).text = "Handle: ${firstResult.handle}"
//                           findViewById<TextView>(R.id.ratingTextView).text = "Rating: ${firstResult.rating}"
//                           findViewById<TextView>(R.id.rankTextView).text = "Rank: ${firstResult.rank}"
//                           findViewById<TextView>(R.id.maxratingTextView).text = "MaxRating: ${firstResult.maxRating}"
//                           findViewById<TextView>(R.id.maxrankTextView).text = "MaxRank: ${firstResult.maxRank}"

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
