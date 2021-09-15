package com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels

import androidx.lifecycle.*
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.WeekOfMonthToInstances
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthViewModel @Inject constructor(private val repository: CalendarRepository) : ViewModel() {
    private val _weekOfMonthToInstances = MutableLiveData<WeekOfMonthToInstances>()
    val weekOfMonthToInstances: LiveData<WeekOfMonthToInstances>
        get() = _weekOfMonthToInstances

    fun getWeekOfMonthToInstances(year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _weekOfMonthToInstances.postValue(repository.getWeekOfMonthToInstances(year, month))
        }
    }
}