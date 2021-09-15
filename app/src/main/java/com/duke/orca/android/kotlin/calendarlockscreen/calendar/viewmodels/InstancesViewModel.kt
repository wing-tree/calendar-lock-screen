package com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Instance
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstancesViewModel @Inject constructor(private val repository: CalendarRepository) : ViewModel() {
    private val _instances = MutableLiveData<List<Instance>>()
    val instances: LiveData<List<Instance>> = _instances

    fun getInstances(julianDay: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.JULIAN_DAY, julianDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }

            println("year, month date : ${calendar.get(Calendar.YEAR)} " +
                    "${calendar.get(Calendar.MONTH)} ${calendar.get(Calendar.DATE)}")

            val begin = calendar.timeInMillis
            val end = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
            }.timeInMillis

            _instances.postValue(repository.getInstances(begin, end))
        }
    }
}