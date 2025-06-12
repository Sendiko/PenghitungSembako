package id.my.sendiko.sembako.dashboard.presentation

import id.my.sendiko.sembako.R
import java.util.Calendar

fun getGreeting(): Int {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    return when (currentHour) {
        in 5..11 -> R.string.good_morning
        in 12..16 -> R.string.good_afternoon
        in 17..20 -> R.string.good_evening
        else -> R.string.good_night
    }
}
