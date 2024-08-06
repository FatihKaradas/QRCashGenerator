package com.example.virtualwallet

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.qrcode.QRCodeWriter

class Transfer : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

       
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
        val TransferMoney = findViewById<EditText>(R.id.TransferMoney)
        val TransferButton = findViewById<Button>(R.id.TransferButton)
        val currentUser = FirebaseAuth.getInstance().currentUser


        if (currentUser != null) {
            val userId = currentUser.uid
            TransferButton.setOnClickListener {
                val transferamount = TransferMoney.text.toString().trim()
                generateQRCode(userId, qrCodeImageView)
                Log.e("Amount:", transferamount)
                requestPermission()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
        val clickableText1 = intent.getStringExtra("text1")
        val clickableText1Color = intent.getStringExtra("color1")
        val clickableText2 = intent.getStringExtra("text2")
        val clickableText2Color = intent.getStringExtra("color2")

        // UI bileşenlerini güncelleyin
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        if (clickableText1 != null && clickableText1Color != null) {
            button2.visibility = View.VISIBLE
            button2.text = clickableText1
            button2.setTextColor(Color.parseColor(clickableText1Color))
            button2.setOnClickListener {
                // Tıklanabilir metin işlemini yapın
                button2.visibility = View.INVISIBLE
                button3.visibility = View.INVISIBLE
                Toast.makeText(this,"İşlem reddedildi",Toast.LENGTH_SHORT).show()
            }
        }

        if (clickableText2 != null && clickableText2Color != null) {
            button3.visibility = View.VISIBLE
            button3.text = clickableText2
            button3.setTextColor(Color.parseColor(clickableText2Color))
            button3.setOnClickListener {
                // Tıklanabilir metin işlemini yapın
                button2.visibility = View.INVISIBLE
                button3.visibility = View.INVISIBLE
                Toast.makeText(this,"İşlem Onaylandı",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQRCode(text: String, imageView: ImageView) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) -0x1000000 else -0x1)
                }
            }
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
    fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }
}
