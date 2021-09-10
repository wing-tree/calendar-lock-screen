package com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.duke.orca.android.kotlin.calendarlockscreen.R
import java.util.*

class DayOfWeekItemView : View {
    private val daysOfWeek = context.resources.getStringArray(R.array.days_of_week)
    private var dayOfWeek = 0

    private lateinit var textPaint: TextPaint

    constructor(context: Context, dayOfWeek: Int): super(context) {
        this.dayOfWeek = dayOfWeek

        textPaint = TextPaint().apply {
            isAntiAlias = true
            color = when(dayOfWeek) {
                Calendar.SATURDAY.dec() -> ContextCompat.getColor(context, R.color.light_blue_400)
                Calendar.SUNDAY.dec() -> ContextCompat.getColor(context, R.color.red_400)
                else -> Color.WHITE
            }
        }
    }

    private constructor(context: Context): super(context)
    private constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    private constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val text = daysOfWeek[dayOfWeek]

        canvas.drawText(text, 0F, textPaint.height(), textPaint)
    }

    private fun TextPaint.height() = fontMetrics.run {
        descent() - ascent()
    }
}