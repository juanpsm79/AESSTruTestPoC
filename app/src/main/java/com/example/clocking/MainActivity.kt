package com.example.clocking

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnOpenAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        findViewById<Button>(R.id.btnSchedule).setOnClickListener {
            Scheduler.scheduleAll(this)
        }
    }
}

object Scheduler {
    private val slotsMonThu = listOf("07:30", "15:00", "15:30", "16:00")
    private val slotsFri = listOf("07:30", "14:30")

    fun scheduleAll(context: android.content.Context) {
        val wm = WorkManager.getInstance(context)
        (slotsMonThu + slotsFri).distinct().forEach { timeStr ->
            val t = LocalTime.parse(timeStr)
            val initialDelay = computeDelay(t)
            val req = PeriodicWorkRequestBuilder<ClockingWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay.toMinutes(), TimeUnit.MINUTES)
                .addTag("clocking-$timeStr")
                .build()
            wm.enqueueUniquePeriodicWork("clocking-$timeStr", ExistingPeriodicWorkPolicy.UPDATE, req)
        }
    }

    private fun computeDelay(target: LocalTime): Duration {
        val now = LocalDateTime.now()
        var next = now.withHour(target.hour).withMinute(target.minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return Duration.between(now, next)
    }
}
