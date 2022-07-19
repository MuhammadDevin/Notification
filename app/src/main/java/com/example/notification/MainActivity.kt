package com.example.notification

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null
    private var CHANNEL_ID = "channel_id"

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // register channel kedalam sistem
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "Countdown", "ini merupakan deskripsi")
        binding.btnStart.setOnClickListener {
            countDownTimer.start()
        }

        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {
                // mesukin text dari string
                binding.timer.text = getString(R.string.time_reamining, p0 / 1000)
            }

            override fun onFinish() {
                displayNotification()
            }

        }


    }

    private fun displayNotification() {
        // menampilkan notifikasi
        var notificationID = 30

        // add your code
        val intent = Intent(this, move_position::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val snoozeIntent = Intent(this, move_position::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Countdown Timer")
            .setContentText("your timer end")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_hw, getString(R.string.snooze),
                snoozePendingIntent)
            .build()

        notificationManager?.notify(notificationID, notification)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        // validasi notif akan dibuat jika version SDK 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

}