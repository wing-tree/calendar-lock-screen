package com.duke.orca.android.kotlin.calendarlockscreen.calendar.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.duke.orca.android.kotlin.calendarlockscreen.application.PACKAGE_NAME
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseFragment
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Month
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodel.MonthViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.FragmentMonthViewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MonthViewFragment : BaseFragment<FragmentMonthViewBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMonthViewBinding {
        return FragmentMonthViewBinding.inflate(inflater, container, false)
    }

    private val viewModel by viewModels<MonthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val month = Month(
            arguments?.getInt(Key.YEAR) ?: 0,
            arguments?.getInt(Key.MONTH) ?: 0
        )

        viewBinding.monthView.setMonth(month)

        viewModel.weekOfMonthToInstances.observe(viewLifecycleOwner, {
            Timber.tag("sjk")
            Timber.d("hi? : ${it.get(0).map { it.title }}")
            Timber.d("hi2? : ${it.get(1).map { it.title }}")
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