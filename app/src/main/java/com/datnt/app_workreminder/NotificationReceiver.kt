package com.datnt.app_workreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val taskContent = intent?.getStringExtra("taskContent")
        val taskDescription = intent?.getStringExtra("taskDescriptionN")

        if (taskContent != null && taskDescription != null) {
            // Hiển thị thông báo với nội dung công việc
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = "task_notification_channel"
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Nhắc nhở công việc")
                .setContentText("$taskContent /n $taskDescription")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Đảm bảo rằng bạn đã tạo channel notification trước
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Công việc",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = "Thông báo nhắc nhở công việc"
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(1, notificationBuilder.build())
        }
    }
}