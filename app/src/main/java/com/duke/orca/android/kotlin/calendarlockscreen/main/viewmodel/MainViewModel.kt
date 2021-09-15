package com.duke.orca.android.kotlin.calendarlockscreen.main.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Day

class MainViewModel : ViewModel() {
    private val _selectedCalendar = MutableLiveData<Calendar>()
    val selectedCalendar: LiveData<Calendar> = _selectedCalendar

    private val _selectedDay = MutableLiveData<Day>()
    val selectedDay: LiveData<Day> = _selectedDay

    fun selectDay(day: Day) {
        _selectedDay.value = day
    }

    fun selectCalendar(calendar: Calendar) {
        _selectedCalendar.value = calendar
    }
}