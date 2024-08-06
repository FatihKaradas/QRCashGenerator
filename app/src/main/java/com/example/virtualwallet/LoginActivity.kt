package com.example.virtualwallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val tvCreateAccount = findViewById<TextView>(R.id.tvCreateAccount)
        val btnSignin = findViewById<Button>(R.id.btnSignIn)

        tvCreateAccount.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

        btnSignin.setOnClickListener{
            validation()
        }
    }
    private fun validation(){
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val fields = listOf(
            etEmail to email,
            etPassword to password,
        )

        for((editText,value) in fields){
            if (value.isEmpty()){
                // Boş alanları işaretler ve işlem çubuğunu gizler
                editText.error ="${editText.hint} is required"
                return
            }
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainPage::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Login Failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }
}