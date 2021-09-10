package com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.duke.orca.android.kotlin.calendarlockscreen.application.BLANK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.VISIBLE_INSTANCE_COUNT
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Day
import com.duke.orca.android.kotlin.calendarlockscreen.application.toPx
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.CALENDAR_COLOR_END
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.CALENDAR_COLOR_START
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.DATE_BOTTOM
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.DATE_START
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.DATE_TOP
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.INSTANCE_BOTTOM
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.INSTANCE_START_LARGE
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView.Margin.INSTANCE_START_SMALL
import com.duke.orca.android.kotlin.calendarlockscreen.color.ColorCalculator
import java.util.*

class DayView : View {
    private object Height {
        const val INSTANCE = 11
    }

    private object Margin {
        const val CALENDAR_COLOR_END = 1
        const val CALENDAR_COLOR_START = 1
        const val DATE_BOTTOM = 6F
        const val DATE_START = 2F
        const val DATE_TOP = 4F
        const val INSTANCE_BOTTOM = 2
        const val INSTANCE_END = 2
        const val INSTANCE_START_LARGE = 4F
        const val INSTANCE_START_SMALL = 2F
    }

    private object TextSize {
        const val DATE = 11F
        const val INSTANCE = 10F
        const val INVISIBLE_INSTANCE_COUNT = 9F
    }

    private object Width {
        const val CALENDAR_COLOR = 2
    }

    private var day: Day? = null
    private val bounds = Rect()

    private lateinit var textPaint: TextPaint
    private lateinit var instanceTextPaint: TextPaint
    private lateinit var invisibleInstanceCountPaint: TextPaint

    private val eventColorPaint = Paint()
    private val eventColorRect = Rect()

    constructor(context: Context, day: Day): super(context) {
        this.day = day

        val dayOfWeek = day.dayOfWeek

        textPaint = TextPaint().apply {
            isAntiAlias = true
            textSize = TextSize.DATE.toPx
            color = when(dayOfWeek) {
                Calendar.SATURDAY -> ContextCompat.getColor(context, R.color.light_blue_400)
                Calendar.SUNDAY -> ContextCompat.getColor(context, R.color.red_400)
                else -> Color.WHITE
            }
            alpha = if (day.isTranslucent) ALPHA else 255
        }

        instanceTextPaint = TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.high_emphasis)
            isAntiAlias = true
            textSize = TextSize.INSTANCE.toPx
            style = Paint.Style.FILL
        }

        invisibleInstanceCountPaint = TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.high_emphasis)
            isAntiAlias = true
            textSize = TextSize.INVISIBLE_INSTANCE_COUNT.toPx
            style = Paint.Style.FILL
        }
    }

    private constructor(context: Context): super(context)
    private constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    private constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val day = this.day ?: return
        val text = day.date.toString()
        var currentY = DATE_TOP.toPx

        with(bounds) {
            textPaint.getTextBounds(text, 0, text.length, this)
            currentY += height().toFloat()
        }

        canvas.drawText(text, DATE_START.toPx, currentY, textPaint)

        currentY += DATE_BOTTOM.toPx

        day.visibleInstances.forEachIndexed { index, instance ->
            if (index > VISIBLE_INSTANCE_COUNT) {
                return@forEachIndexed
            }

            bounds.set(0, 0, width, Height.INSTANCE.toPx)

            instance?.let {
                val right = this.width * it.spanCount - CALENDAR_COLOR_END.toPx

                canvas.save()
                canvas.clipRect(0, 0, right, this.height)

                val title = if (it.isVisible) {
                    eventColorPaint.color = instance.calendarColor
                    eventColorPaint.alpha = if (it.isTranslucent) ALPHA else 255
                    instanceTextPaint.color = ColorCalculator.onBackgroundColor(
                        instance.calendarColor,
                        ContextCompat.getColor(context, R.color.high_emphasis_dark),
                        ContextCompat.getColor(context, R.color.high_emphasis_light)
                    )
                    instanceTextPaint.alpha = if (it.isTranslucent) ALPHA else 255

                    instance.title
                } else {
                    eventColorPaint.color = Color.TRANSPARENT
                    instanceTextPaint.color = Color.TRANSPARENT
                    BLANK
                }

//                if (item !is CalendarItem.DayOfMonth) {
//                    calendarColorPaint.alpha = ALPHA
//                    instanceTextPaint.alpha = ALPHA
//                }

                if (it.isVisible) {
                    val bottom = currentY.toInt() + bounds.height()

                    if (instance.isFillBackgroundColor()) {
                        canvas.drawRect(eventColorRect.apply {
                            set(CALENDAR_COLOR_START.toPx, currentY.toInt(), right, bottom)
                        }, eventColorPaint)
                    } else {
                        canvas.drawRect(eventColorRect.apply {
                            set(CALENDAR_COLOR_START.toPx, currentY.toInt(), 2.inc().toPx, bottom)
                        }, eventColorPaint)
                    }
                }

                val x = if (it.isFillBackgroundColor())
                    INSTANCE_START_SMALL.toPx
                else
                    INSTANCE_START_LARGE.toPx

                var baselineShift = instanceTextPaint.height() - bounds.height()

                if (baselineShift < 0F) {
                    baselineShift = 0F
                }

                canvas.drawText(
                    title,
                    x,
                    currentY + bounds.height() - baselineShift.toPx,
                    instanceTextPaint
                )

                canvas.restore()
                currentY += (bounds.height() + INSTANCE_BOTTOM.toPx).toFloat()
            } ?: let {
                canvas.drawRect(eventColorRect.apply {
                    set(
                        0,
                        currentY.toInt(),
                        width,
                        currentY.toInt() + bounds.height()
                    )
                }, eventColorPaint.apply { color = Color.TRANSPARENT })

                currentY += (bounds.height() + INSTANCE_BOTTOM.toPx).toFloat()
            }
        }

//        if (item.instances.count() > VISIBLE_INSTANCE_COUNT) {
//            canvas.drawText(
//                "+${(item.instances.count() - VISIBLE_INSTANCE_COUNT)}",
//                INSTANCE_START_SMALL.toPx,
//                currentY + bounds.height() - INSTANCE_END.toPx,
//                invisibleInstanceCountPaint
//            )
//        }
    }

    private fun TextPaint.height() = fontMetrics.run {
        descent - ascent
    }

    companion object {
        private const val ALPHA = 128
    }
}