package com.duke.orca.android.kotlin.calendarlockscreen.calendar.model

class Week {
    val days = linkedMapOf<Int, Day>()
    fun getFirstDay() = days[days.keys.first()]
}