package com.duke.orca.android.kotlin.calendarlockscreen.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.duke.orca.android.kotlin.calendarlockscreen.R
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

internal object PermissionChecker {
    private var snackbar: Snackbar? = null

    private fun checkPermission(context: Context, permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    internal fun checkPermissions(context: Context, permissions: List<String>): Boolean {
        permissions.forEach {
            with(checkPermission(context, it)) {
                if (this.not()) {
                    return false
                }
            }
        }

        return true
    }

    internal fun checkPermissions(
        context: Context,
        permissions: List<String>,
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.grantedPermissionResponses.map { it.permissionName }.containsAll(permissions)) {
                        onPermissionsGranted.invoke()
                    }

                    if (report.deniedPermissionResponses.map { it.permissionName }.containsAll(permissions)) {
                        onPermissionsDenied.invoke()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    internal fun canDrawOverlays(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun dismissSnackbar(): Boolean {
        if (snackbar?.isShownOrQueued == true) {
            snackbar?.dismiss()
            return true
        }

        return false
    }

    fun showRequestCalendarPermissionSnackbar(viewGroup: ViewGroup, activityResultLauncher: ActivityResultLauncher<Intent>?) {
        val context = viewGroup.context

        snackbar?.dismiss()

        snackbar = Snackbar.make(viewGroup, context.getString(R.string.calendar_permission_rationale), Snackbar.LENGTH_INDEFINITE)
            .setAction(context.getString(R.string.allow)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", it.context.packageName, null)

                activityResultLauncher?.launch(intent.apply { data = uri })
            }
            .setActionTextColor(ContextCompat.getColor(context, R.color.teal_400))

        snackbar?.show()
    }
}