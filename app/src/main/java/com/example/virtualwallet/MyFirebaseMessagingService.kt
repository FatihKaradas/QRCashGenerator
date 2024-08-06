package com.example.virtualwallet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        val clickableText1 = remoteMessage.data["clickableText1"]
        val clickableText1Color = remoteMessage.data["clickableText1Color"]
        val clickableText2 = remoteMessage.data["clickableText2"]
        val clickableText2Color = remoteMessage.data["clickableText2Color"]

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Burada mesajı işleyin ve göstermek için showNotification() çağırın
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body,clickableText1, clickableText1Color, clickableText2, clickableText2Color)
        }

    }

    private fun showNotification(title: String?, message: String?,text1: String?, color1: String?, text2: String?, color2: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }


        val intent = Intent(this, Transfer::class.java)
        intent.putExtra("text1", text1)
        intent.putExtra("color1", color1)
        intent.putExtra("text2", text2)
        intent.putExtra("color2", color2)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Bildirim simgesi
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
