package com.duke.orca.android.kotlin.calendarlockscreen.calendar.model

import com.duke.orca.android.kotlin.calendarlockscreen.calendar.VISIBLE_INSTANCE_COUNT

data class Day (
    val date: Int,
    val dayOfWeek: Int,
    val isTranslucent: Boolean,
    val julianDay: Int,
    val month: Int,
    val year: Int,
    val nextKey: Int,
    val visibleInstances: Array<Instance?> = arrayOfNulls(VISIBLE_INSTANCE_COUNT),
    var instanceCount: Int = 0
) {
    val key = julianDay
}