package space.algoritmos.habit_tracker.notifications

import androidx.work.*
import java.time.Duration
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(workManager: WorkManager) {
    val now = java.time.LocalDateTime.now()
    val targetTime = now.withHour(20).withMinute(0).withSecond(0) // 20h00
    var delay = Duration.between(now, targetTime).toMillis()
    if (delay < 0) delay += TimeUnit.DAYS.toMillis(1) // se já passou das 20h, agenda para amanhã

    val workRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("daily_notification")
        .build()

    workManager.enqueueUniqueWork(
        "daily_notification",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}
