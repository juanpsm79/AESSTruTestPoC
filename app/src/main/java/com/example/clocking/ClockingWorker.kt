package com.example.clocking

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class ClockingWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        if (!isScheduledForToday()) return Result.success()

        val url = "https://idglghr.pn.cegid.cloud/servlet/CheckSecurity/JSP/ssco_menu/index_renhash_297faf5f27b412a225e88c0c9547b444_l2_dl_oIDES_v1.html#reload"
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        applicationContext.startActivity(i)
        return Result.success()
    }

    private fun isScheduledForToday(): Boolean {
        val day = LocalDate.now().dayOfWeek
        val now = LocalTime.now().withSecond(0).withNano(0)
        return when (day) {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY ->
                now in setOf(LocalTime.of(7, 30), LocalTime.of(15, 0), LocalTime.of(15, 30), LocalTime.of(16, 0))
            DayOfWeek.FRIDAY -> now in setOf(LocalTime.of(7, 30), LocalTime.of(14, 30))
            else -> false
        }
    }
}
