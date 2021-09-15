package com.duke.orca.android.kotlin.calendarlockscreen.calendar.views

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.duke.orca.android.kotlin.calendarlockscreen.application.PACKAGE_NAME
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseActivity
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseDialogFragment
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseFragment
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.adapter.InstancesViewAdapter
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.pagetransformer.PageTransformer
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.ActivityInstancesViewPagerBinding
import com.duke.orca.android.kotlin.calendarlockscreen.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstancesViewPagerActivity : BaseFragment<ActivityInstancesViewPagerBinding>() {
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityInstancesViewPagerBinding {
        return ActivityInstancesViewPagerBinding.inflate(inflater, container, false)
    }

    object Key {
        const val YEAR = "$PACKAGE_NAME.YEAR"
        const val MONTH = "$PACKAGE_NAME.MONTH"
        const val DATE = "$PACKAGE_NAME.DATE"
        const val JULIAN_DAY = "$PACKAGE_NAME.JULIAN_DAY"
        const val VIEW_PARAMS = "$PACKAGE_NAME.VIEW_PARAMS"
    }

    private val activityViewModel by activityViewModels<MainViewModel>()
    private val adapter by lazy { InstancesViewAdapter(this) }
    private val offscreenPageLimit = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val julianDay = arguments?.getInt(Key.JULIAN_DAY, 0) ?: 0

        initializeViews()
        viewBinding.viewPager2.setCurrentItem(getStartPosition(julianDay), false)

        return viewBinding.root
    }

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val amount = position - InstancesViewAdapter.START_POSITION
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.DATE, amount)
                }

                activityViewModel.selectCalendar(calendar)
            }
        }
    }

    private fun initializeViews() {
        viewBinding.viewPager2.adapter = adapter
        viewBinding.viewPager2.offscreenPageLimit = offscreenPageLimit
        viewBinding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
        viewBinding.viewPager2.setPageTransformer(PageTransformer())
    }

    private fun getStartPosition(julianDay: Int): Int {
        return InstancesViewAdapter.START_POSITION - (Calendar.getInstance().get(Calendar.JULIAN_DAY) - julianDay)
    }

    companion object {
        fun newInstance(julianDay: Int): InstancesViewPagerActivity {
            val args = Bundle()

            args.putInt(Key.JULIAN_DAY, julianDay)

            val fragment = InstancesViewPagerActivity()
            fragment.arguments = args
            return fragment
        }
    }
}