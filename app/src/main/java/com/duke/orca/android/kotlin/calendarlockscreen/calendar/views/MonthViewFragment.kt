package com.duke.orca.android.kotlin.calendarlockscreen.calendar.views

import android.app.ActivityOptions
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.duke.orca.android.kotlin.calendarlockscreen.application.PACKAGE_NAME
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseFragment
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Day
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Month
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels.MonthViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayView
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.MonthView
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.FragmentMonthViewBinding
import com.duke.orca.android.kotlin.calendarlockscreen.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthViewFragment : BaseFragment<FragmentMonthViewBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMonthViewBinding {
        return FragmentMonthViewBinding.inflate(inflater, container, false)
    }

    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<MonthViewModel>()

    private val month: Month by lazy {
        Month(arguments?.getInt(Key.YEAR) ?: 0, arguments?.getInt(Key.MONTH) ?: 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        viewBinding.monthView.setMonth(month)
        viewBinding.monthView.setOnDayClickListener(object : MonthView.OnDayClickListener {
            override fun onDayClick(dayView: DayView, day: Day) {
                activityViewModel.selectDay(day)
            }
        })

        activityViewModel.selectedDay.observe(viewLifecycleOwner, {
            if (month.month == it.month) {
                viewBinding.monthView.selectDay(it)
            } else {
                viewBinding.monthView.unselect()
            }
        })

        activityViewModel.selectedCalendar.observe(viewLifecycleOwner, {
            if (month.month == it.get(Calendar.MONTH)) {
                viewBinding.monthView.selectDay(it.get(Calendar.JULIAN_DAY))
            } else {
                viewBinding.monthView.unselect()
            }
        })

        viewModel.weekOfMonthToInstances.observe(viewLifecycleOwner, {
            month.setWeekOfMonthToInstances(it)
            viewBinding.monthView.setMonth(month)
        })

        viewModel.getWeekOfMonthToInstances(month.year, month.month)

        return viewBinding.root
    }

    companion object {
        private object Key {
            const val YEAR = "$PACKAGE_NAME.YEAR"
            const val MONTH = "$PACKAGE_NAME.MONTH"
        }

        fun newInstance(year: Int, month: Int): MonthViewFragment {
            return MonthViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(Key.YEAR, year)
                    putInt(Key.MONTH, month)
                }
            }
        }
    }
}