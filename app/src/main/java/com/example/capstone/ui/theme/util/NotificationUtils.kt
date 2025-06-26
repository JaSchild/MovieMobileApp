package com.example.capstone.ui.theme.util

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.capstone.R

fun showLeavingSoonNotification(context: Context, title: String, daysLeft: Long) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return
        }
    }

    val builder = NotificationCompat.Builder(context, "movie_channel_id")
        .setSmallIcon(R.drawable.logo)
        .setContentTitle("Leaving Soon!")
        .setContentText("$title is leaving in $daysLeft day(s)!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(title.hashCode(), builder.build())
    }
}
