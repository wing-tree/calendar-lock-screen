package com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels

import android.content.Intent
import android.view.animation.ScaleAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Event
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstancesViewPagerViewModel @Inject constructor(private val repository: CalendarRepository): ViewModel() {
    var lastEvent: Event? = null

    private val _intent = MutableLiveData<Intent>()
    val intent: LiveData<Intent> = _intent

    init {
        viewModelScope.launch(Dispatchers.IO) {
            lastEvent = repository.getLastEvent()
        }
    }

    fun startActivity(intent: Intent) {
        _intent.value = intent
    }
}