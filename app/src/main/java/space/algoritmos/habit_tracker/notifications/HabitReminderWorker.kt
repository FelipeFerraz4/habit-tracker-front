package space.algoritmos.habit_tracker.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import space.algoritmos.habit_tracker.R

class HabitReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(context, "HABIT_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Hora de fazer seus hábitos!")
            .setContentText("Não esqueça de completar seus hábitos do dia.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }

        notificationManager.notify(2, builder.build()) // ID diferente da outra notificação
        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Lembretes de Hábitos"
        val descriptionText = "Canal usado para notificações de lembrança de hábitos"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("HABIT_REMINDER_CHANNEL", name, importance)
        channel.description = descriptionText

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
