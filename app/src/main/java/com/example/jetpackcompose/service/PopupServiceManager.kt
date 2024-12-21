package com.example.jetpackcompose.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jetpackcompose.service.PopupService

/**
 * Manages the interaction with the PopupService, including requesting notification permissions
 * and starting the service.
 *
 * @param context The context in which the manager operates, typically an Activity or Application context.
 */
class PopupServiceManager(private val context: Context) {

    /**
     * Requests the necessary permission to display notifications.
     * If permission is granted, the PopupService is started; otherwise, a toast message
     * is shown informing the user that notifications won't work.
     */
    fun requestPermission() {
        val requestPermissionLauncher =
            (context as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) startPopupService()
                else Toast.makeText(
                    context,
                    "Permission denied, notifications won't work",
                    Toast.LENGTH_LONG
                ).show()
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Starts the PopupService as a foreground service.
     * This service will handle pop-up notifications based on the app's requirements.
     */
    fun startPopupService() {
        val serviceIntent = Intent(context, PopupService::class.java)
        context.startForegroundService(serviceIntent)
    }
}
