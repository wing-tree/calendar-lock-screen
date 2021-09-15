package com.duke.orca.android.kotlin.calendarlockscreen.calendar.adapter

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.views.InstancesViewFragment

class InstancesViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        val amount = position - START_POSITION
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, amount)
        }

        val julianDay = calendar.get(Calendar.JULIAN_DAY)

        return InstancesViewFragment.newInstance(julianDay)
    }

    companion object {
        private const val ITEM_COUNT = Int.MAX_VALUE
        const val START_POSITION = ITEM_COUNT / 2
    }
}