package com.duke.orca.android.kotlin.calendarlockscreen.main.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.views.InstancesViewPagerActivity
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.views.MonthViewPagerFragment
import com.duke.orca.android.kotlin.calendarlockscreen.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, MonthViewPagerFragment())
            .commit()
    }
}