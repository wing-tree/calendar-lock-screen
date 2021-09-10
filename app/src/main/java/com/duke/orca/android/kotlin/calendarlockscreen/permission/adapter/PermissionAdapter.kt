package com.duke.orca.android.kotlin.calendarlockscreen.permission.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.orca.android.kotlin.calendarlockscreen.application.hide
import com.duke.orca.android.kotlin.calendarlockscreen.application.show
import com.duke.orca.android.kotlin.calendarlockscreen.databinding.PermissionBinding
import com.duke.orca.android.kotlin.calendarlockscreen.permission.model.Permission

class PermissionAdapter(private val items: List<Permission>): RecyclerView.Adapter<PermissionAdapter.ViewHolder>() {
    private var inflater: LayoutInflater? = null

    inner class ViewHolder (private val binding: PermissionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Permission) {
            binding.imageViewIcon.setImageIcon(item.icon)
            binding.textViewPermission.text = item.permissionName

            if (item.isRequired)
                binding.textViewPermission.setTypeface(null, Typeface.BOLD)
            else
                binding.textViewPermission.setTypeface(null, Typeface.NORMAL)

            if (adapterPosition == items.count().dec())
                binding.viewDivider.hide()
            else
                binding.viewDivider.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = this.inflater ?: LayoutInflater.from(parent.context)

        this.inflater = inflater

        return ViewHolder(PermissionBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}