package com.datnt.app_workreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar

class TaskReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.datnt.app_quanlycongviec.NEW_TASK") {
            val taskName = intent.getStringExtra("taskName")
            val taskDescription = intent.getStringExtra("taskDescription")
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Lấy thời gian hiện tại
            val currentTime = Calendar.getInstance()

            // Đặt thời gian lịch nhắc nhở vào 6 giờ sáng
            val targetTime = Calendar.getInstance()
            targetTime.set(Calendar.HOUR_OF_DAY, 6)
            targetTime.set(Calendar.MINUTE, 0)
            targetTime.set(Calendar.SECOND, 0)

            // Nếu thời gian lập lịch đã trôi qua, thì đặt lịch cho ngày tiếp theo
            if (targetTime.before(currentTime)) {
                targetTime.add(Calendar.DAY_OF_YEAR, 1)
            }

            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("taskContent", taskName) // Thay "Nội dung công việc" bằng thông tin công việc thực tế
            intent.putExtra("taskDescriptionN", taskDescription)
            context.sendBroadcast(intent)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                targetTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

        }
    }
}