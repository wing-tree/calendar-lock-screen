package com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.DAYS_PER_WEEK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.WEEKS_PER_MONTH
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Day
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Month

class MonthView : ViewGroup {
    private var onDayClickListener: OnDayClickListener? = null
    private var selectedDayView: DayView? = null
    private val julianDayToDayView = linkedMapOf<Int, DayView>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = (width / DAYS_PER_WEEK).toFloat()

        children.forEachIndexed { index, view ->
            val childHeight = measuredHeight / WEEKS_PER_MONTH
            val left = (index % DAYS_PER_WEEK) * width
            val top = (index / DAYS_PER_WEEK) * childHeight

            view.layout(left.toInt(), top, (left + width).toInt(), (top + childHeight))
        }
    }

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

                addView(DayView(context, day).apply {
                    setOnClickListener {
                        onDayClickListener?.onDayClick(this, day)
                    }

                    julianDayToDayView[day.key] = this
                })
            }
        }
    }

    fun selectDay(day: Day) {
        val dayView = julianDayToDayView[day.key] ?: return

        if (dayView == selectedDayView) return

        selectedDayView?.background = null
        dayView.setBackgroundResource(R.drawable.background_selected)
        selectedDayView = dayView
    }

    fun selectDay(julianDay: Int) {
        val dayView = julianDayToDayView[julianDay] ?: return

        if (dayView == selectedDayView) return

        selectedDayView?.background = null
        dayView.setBackgroundResource(R.drawable.background_selected)
        selectedDayView = dayView
    }

    fun unselect() {
        selectedDayView?.background = null
        selectedDayView = null
    }
}