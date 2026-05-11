package projet.ecare.presentation.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import projet.ecare.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_HIGH = "channel_priority_high_v2"
        const val CHANNEL_MEDIUM = "channel_priority_medium_v2"
        const val CHANNEL_LOW = "channel_priority_low_v2"
    }

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createClickIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("destination", "notifications")
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_HIGH,
                    "Priorité Élevée",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Rappels critiques"
                    enableLights(true)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_MEDIUM,
                    "Priorité Moyenne",
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannel(
                    CHANNEL_LOW,
                    "Priorité Faible",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
            manager.createNotificationChannels(channels)
        }
    }

    @android.annotation.SuppressLint("MissingPermission")
    fun showNotification(title: String, message: String, priorite: String) {

        createNotificationChannels()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) return
        }

        val (channelId, priorityLevel) = when (priorite) {
            "Élevée" -> CHANNEL_HIGH to NotificationCompat.PRIORITY_HIGH
            "Moyenne" -> CHANNEL_MEDIUM to NotificationCompat.PRIORITY_DEFAULT
            else -> CHANNEL_LOW to NotificationCompat.PRIORITY_LOW
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priorityLevel)
            .setAutoCancel(true)
            .setContentIntent(createClickIntent())
            .setColor(
                if (priorite == "Élevée")
                    0xFFFF0000.toInt()
                else
                    0xFF2E7D67.toInt()
            )

        if (priorite == "Faible") {
            builder.setSilent(true)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        }

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}