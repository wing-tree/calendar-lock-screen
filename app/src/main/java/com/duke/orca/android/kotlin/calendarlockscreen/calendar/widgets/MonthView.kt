package com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.DAYS_PER_WEEK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.WEEKS_PER_MONTH
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Day
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Month

class MonthView : ViewGroup {
    private var onDayClickListener: OnDayClickListener? = null

    interface OnDayClickListener {
        fun onDayClick(dayView: DayView, day: Day)
    }

    fun setOnDayClickListener(onItemClickListener: OnDayClickListener) {
        this.onDayClickListener = onItemClickListener
    }

    fun setMonth(month: Month) {
        removeAllViews()
        repeat(WEEKS_PER_MONTH) { weekOfMonth ->
            month.get(weekOfMonth).days.forEach {
                val day = it.value

                addView(DayView(context, day))
            }
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = (width / DAYS_PER_WEEK).toFloat()

        children.forEachIndexed { index, view ->
            val left = (index % DAYS_PER_WEEK) * width
            val top = (index / DAYS_PER_WEEK) * measuredHeight / WEEKS_PER_MONTH

            view.layout(left.toInt(), top.toInt(), (left + width).toInt(), (top + height).toInt())
        }
    }
}