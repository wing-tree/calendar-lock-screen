package com.duke.orca.android.kotlin.calendarlockscreen.calendar.model

class WeekOfMonthToInstances {
    private val weekOfMonthToInstances = linkedMapOf<Int, List<Instance>>()

    fun set(weekOfMonth: Int, instances: List<Instance>) {
        weekOfMonthToInstances[weekOfMonth] = instances
    }

    fun get(weekOfMonth: Int): List<Instance> {
        return weekOfMonthToInstances[weekOfMonth] ?: emptyList()
    }
}