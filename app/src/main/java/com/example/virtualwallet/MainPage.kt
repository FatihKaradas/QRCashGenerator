package com.example.virtualwallet

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.LiveData
import android.Manifest


class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        val name = findViewById<TextView>(R.id.tvUserName)
        val btnTopup = findViewById<Button>(R.id.btnTopup)
        val btnTransfer = findViewById<Button>(R.id.btnTransfer)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val displayName = currentUser?.displayName
        val userId: String
        if (displayName != null) {
            name.text = displayName
            userId = currentUser.uid
            //balance(userId)
        } else {
            name.text = "User"
        }
        btnTopup.setOnClickListener{
        }
        btnTransfer.setOnClickListener{
            startActivity(Intent(this,Transfer::class.java))
        }
    }

    /*private fun balance(userId: String) {
        val tvBalance = findViewById<TextView>(R.id.tvBalanceAmount)
        val db = FirebaseFirestore.getInstance()
        val walletsCollection = db.collection("wallets")
        walletsCollection.document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val wallet = document.data
                    val balance = wallet?.get("balance")?.toString()
                    tvBalance.text = balance ?: "0"
                } else {
                    tvBalance.text = "0"
                    //Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { e ->
                tvBalance.text = "0"
                //Log.w("Firestore", "Error getting document", e)
            }
    }*/
}


