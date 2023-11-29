package com.firstapp.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val login=findViewById<Button>(R.id.Login)
        val signup=findViewById<Button>(R.id.Signup)
        val email=findViewById<TextView>(R.id.Email)
        val password=findViewById<TextView>(R.id.Password)
        signup.setOnClickListener{
            var intent=Intent(this,Registerpage::class.java)
            startActivity(intent)
        }
        login.setOnClickListener{
            if(checking())
            {
                var email= email.text.toString()
                var password=password.text.toString()
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                            task->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                           // val userName = user.email?.displayName ?: "User" // You can get the user's name if available


                            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()

                            // Start the HomeActivity and pass the user's name
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("USER_NAME", email)
                            startActivity(intent)

                            // Finish the current activity (optional)
                            finish()
                        } else {
                            Toast.makeText(this, "Enter valid credentials", Toast.LENGTH_LONG).show()
                        }

                    }
            }
            else{
                Toast.makeText(this,"enter the details", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checking():Boolean{
        val email=findViewById<TextView>(R.id.Email)
        val password=findViewById<TextView>(R.id.Password)
        if(email.text.toString().trim{it<=' '}.isNotEmpty()
            && password.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false

    }
}