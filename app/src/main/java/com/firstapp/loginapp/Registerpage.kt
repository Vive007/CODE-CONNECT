package com.firstapp.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Registerpage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerpage)
        auth= FirebaseAuth.getInstance()
        val submit=findViewById<Button>(R.id.submit)
        val email=findViewById<TextView>(R.id.email)
        val registerPassword=findViewById<TextView>(R.id.registerPassword)
        val name=findViewById<TextView>(R.id.Name)
        val phone=findViewById<TextView>(R.id.Phone)
        submit.setOnClickListener{
            if(checking()){
                var email=email.text.toString()
                var password=registerPassword.text.toString()
                var name=name.text.toString()
                var phone=phone.text.toString()
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        task->
                        if(task.isSuccessful)
                        {
                            var intent=Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this,"Sign-up sucessfull",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this,"Sign-up failed",Toast.LENGTH_LONG).show()
                        }
                    }

            }
            else{
                Toast.makeText(this,"Enter the details",Toast.LENGTH_LONG).show()
            }
        }
    }
private fun checking():Boolean{
    val email=findViewById<TextView>(R.id.email)
    val registerPassword=findViewById<TextView>(R.id.registerPassword)
    val name=findViewById<TextView>(R.id.Name)
    val phone=findViewById<TextView>(R.id.Phone)
    if(name.text.toString().trim{it<=' '}.isNotEmpty()
        && phone.text.toString().trim{it<=' '}.isNotEmpty()
        && email.text.toString().trim{it<=' '}.isNotEmpty()
        && registerPassword.text.toString().trim{it<=' '}.isNotEmpty())
    {
        return true
    }
    return false
}
}