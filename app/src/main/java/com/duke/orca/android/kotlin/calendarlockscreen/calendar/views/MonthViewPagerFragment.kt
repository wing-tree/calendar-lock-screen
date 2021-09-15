package com.duke.orca.android.kotlin.calendarlockscreen.calendar.views

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseFragment
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.DAYS_PER_WEEK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.MONTHS_PER_YEAR
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.adapter.MonthViewAdapter
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.widgets.DayOfWeekItemView
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.FragmentMonthViewPagerBinding
import com.duke.orca.android.kotlin.calendarlockscreen.main.viewmodel.MainViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.permission.views.PermissionRationaleDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MonthViewPagerFragment : BaseFragment<FragmentMonthViewPagerBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMonthViewPagerBinding {
        return FragmentMonthViewPagerBinding.inflate(inflater, container, false)
    }

    private val activityViewModel by activityViewModels<MainViewModel>()
    private val adapter by lazy { MonthViewAdapter(this) }
    private val months by lazy { resources.getStringArray(R.array.months) }
    private val offscreenPageLimit = 3

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val amount = position - MonthViewAdapter.START_POSITION
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.MONTH, amount)
                }

                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                viewBinding.textViewMonth.text = months[month]
                "$year${getString(R.string.year)}".also {
                    viewBinding.textViewYear.text = it
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initializeViews()

        if (PermissionRationaleDialogFragment.permissionsGranted(requireContext()).not()) {
            PermissionRationaleDialogFragment().also {
                it.show(childFragmentManager, it.tag)
            }
        }

        activityViewModel.selectedDay.observe(viewLifecycleOwner, {
            parentFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, InstancesViewPagerActivity.newInstance(it.julianDay))
                .addToBackStack(null)
                .commit()
        })

        activityViewModel.selectedCalendar.observe(viewLifecycleOwner, {
            val year = it.get(Calendar.YEAR)
            val month = it.get(Calendar.MONTH)
            scrollTo(year, month, smoothScroll = true)
        })

        return viewBinding.root
    }

    private fun initializeViews() {
        repeat(DAYS_PER_WEEK) {
            viewBinding.dayOfWeekView.addView(DayOfWeekItemView(requireContext(), it))
        }

        viewBinding.viewPager2.adapter = adapter
        viewBinding.viewPager2.offscreenPageLimit = offscreenPageLimit
        viewBinding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
        viewBinding.viewPager2.setCurrentItem(MonthViewAdapter.START_POSITION, false)

//        viewBinding.imageViewInsert.setOnClickListener {
//            viewModel.selectedItem?.let {
//                viewModel.insertEvent(currentYear, currentMonth, it.date)
//            }
//        }
//
//        viewBinding.textViewToday.text = viewModel.today.get(Calendar.DATE).toString()
    }

    private fun scrollTo(year: Int, month: Int, smoothScroll: Boolean) {
        val currentItem = viewBinding.viewPager2.currentItem

        val from = Calendar.getInstance().apply {
            add(Calendar.MONTH, currentItem - MonthViewAdapter.START_POSITION)
        }

        val to = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
        }

        val fromYear = from.get(Calendar.YEAR)
        val fromMonth = from.get(Calendar.MONTH)

        val toYear = to.get(Calendar.YEAR)
        val toMonth = to.get(Calendar.MONTH)

        val amount = toMonth - fromMonth + (toYear - fromYear) * MONTHS_PER_YEAR

        viewBinding.viewPager2.setCurrentItem(currentItem + amount, smoothScroll)
    }
}