package com.duke.orca.android.kotlin.calendarlockscreen.calendar.views

import android.content.ContentUris
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.orca.android.kotlin.calendarlockscreen.application.PACKAGE_NAME
import com.duke.orca.android.kotlin.calendarlockscreen.base.BaseFragment
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.adapter.InstanceAdapter
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Instance
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels.InstancesViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.viewmodels.InstancesViewPagerViewModel
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.FragmentInstancesViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstancesViewFragment : BaseFragment<FragmentInstancesViewBinding>() {
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInstancesViewBinding {
        return FragmentInstancesViewBinding.inflate(inflater, container, false)
    }

    private val activityViewModel by activityViewModels<InstancesViewPagerViewModel>()
    private val viewModel by viewModels<InstancesViewModel>()

    private val adapter = InstanceAdapter().apply {
        setOnItemClickListener(object : InstanceAdapter.OnItemClickListener {
            override fun onItemClick(item: Instance) {
                val contentUris = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, item.eventId)
                val intent = Intent(Intent.ACTION_EDIT).apply {
                    data = contentUris
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, item.begin)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, item.end)
                }

                activityViewModel.startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val julianDay = arguments?.getInt(Key.JULIAN_DAY) ?: 0
        val date = Calendar.getInstance().apply {
            set(Calendar.JULIAN_DAY, julianDay)
        }.get(Calendar.DATE)

        viewBinding.textViewDate.text = date.toString()
        viewBinding.recyclerView.apply {
            adapter = this@InstancesViewFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.getInstances(julianDay)

        viewModel.instances.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        return viewBinding.root
    }

    companion object {
        private object Key {
            const val JULIAN_DAY = "$PACKAGE_NAME.JULIAN_DAY"
        }

        fun newInstance(julianDay: Int): InstancesViewFragment {
            return InstancesViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(Key.JULIAN_DAY, julianDay)
                }
            }
        }
    }
}