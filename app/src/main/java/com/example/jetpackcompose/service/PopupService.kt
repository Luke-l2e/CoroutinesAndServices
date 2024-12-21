package com.example.jetpackcompose.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.jetpackcompose.MainActivity
import com.example.jetpackcompose.ui.views.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Service that displays periodic notifications in the foreground.
 * The service fetches notification intervals from settings and informs the user with notifications.
 */
class PopupService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var delayMillis: Long = -1L
    private var i = 1
    private val dataStore by lazy { applicationContext.dataStore }
    private var isNotificationEnabled: Boolean = false

    /**
     * BroadcastReceiver to listen for updates to the timer option and update the notification interval.
     */
    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val newTimerOption = intent?.getStringExtra("timer_option") ?: "Deactivated"
            updateTimerOption(newTimerOption)
        }
    }

    /**
     * Called when the service is created. Initializes the notification channel, starts the foreground service,
     * and registers the update receiver. It also initializes the timer based on stored settings.
     */
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = getNotification("Start popup service")
        startForeground(i, notification)
        registerUpdateReceiver()
        initializeTimerFromSettings()
    }

    /**
     * Called when the service is destroyed. Removes any pending notifications and unregisters the update receiver.
     */
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showNotificationRunnable)
        unregisterReceiver(updateReceiver)
    }

    /**
     * Called when the service is started. If a timer option has been set, it schedules notifications at the specified interval.
     *
     * @param intent The intent that started the service.
     * @param flags Additional data about how the service should be started.
     * @param startId A unique identifier for this start request.
     * @return The return value determines what happens if the service is killed.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (delayMillis != -1L) {
            handler.removeCallbacks(showNotificationRunnable)
            handler.post(showNotificationRunnable)
        }
        return START_STICKY
    }

    /**
     * Called when a client binds to the service. In this case, binding is not supported, so it returns null.
     *
     * @param intent The intent that was used to bind to the service.
     * @return Always returns null.
     */
    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * A Runnable that periodically sends a notification if the notification timer is enabled.
     */
    private val showNotificationRunnable = object : Runnable {
        override fun run() {
            if (isNotificationEnabled) {
                sendNotification("Hello World $i")
                i++
            }
            handler.postDelayed(this, delayMillis)
        }
    }

    /**
     * Updates the timer option and restarts the notification schedule. If the timer option is deactivated, the service stops.
     *
     * @param option The new timer option (e.g., "10s", "30s", "60s").
     */
    private fun updateTimerOption(option: String) {
        delayMillis = timerOptionToMillis(option)
        isNotificationEnabled = delayMillis != -1L
        handler.removeCallbacks(showNotificationRunnable)

        if (delayMillis == -1L) {
            stopSelf()
        } else {
            handler.postDelayed(showNotificationRunnable, delayMillis)
        }
    }

    /**
     * Fetches the timer option from the app's settings.
     *
     * @return The timer option as a string (e.g., "10s", "30s").
     */
    private suspend fun fetchTimerOptionFromSettings(): String {
        val key = stringPreferencesKey("timer_option_key")
        val timerOption = dataStore.data.map { preferences ->
            preferences[key] ?: "Deactivated"
        }.first()

        return timerOption
    }

    /**
     * Registers the update receiver to listen for timer option updates.
     */
    private fun registerUpdateReceiver() {
        ContextCompat.registerReceiver(
            this,
            updateReceiver,
            IntentFilter("com.example.jetpackcompose.UPDATE_TIMER"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    /**
     * Converts a string representing a timer option (e.g., "10s", "30s") to a corresponding delay in milliseconds.
     *
     * @param option The timer option as a string.
     * @return The delay in milliseconds, or -1 if the option is invalid.
     */
    private fun timerOptionToMillis(option: String): Long {
        return when (option) {
            "10s" -> 10_000L
            "30s" -> 30_000L
            "60s" -> 60_000L
            "30 min" -> 30 * 60 * 1000L
            "60 min" -> 60 * 60 * 1000L
            else -> -1L
        }
    }

    /**
     * Initializes the timer from the app's settings and starts the notification schedule if necessary.
     */
    private fun initializeTimerFromSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            val timerOption = fetchTimerOptionFromSettings()
            delayMillis = timerOptionToMillis(timerOption)

            if (delayMillis != -1L) {
                isNotificationEnabled = true
                handler.post(showNotificationRunnable)
            }
        }
    }

    /**
     * Sends a notification with the specified message.
     *
     * @param message The message to display in the notification.
     */
    private fun sendNotification(message: String) {
        if (ActivityCompat.checkSelfPermission(
                this@PopupService,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notificationManager = NotificationManagerCompat.from(this)
        val notification = getNotification(message)
        notificationManager.notify(1, notification)
    }

    /**
     * Builds a notification with the specified content text.
     *
     * @param contentText The text to display in the notification.
     * @return The built notification.
     */
    private fun getNotification(contentText: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "popup_service_channel")
            .setContentTitle("Popup Service")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    /**
     * Creates a notification channel for the service if the Android version is Oreo or higher.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "popup_service_channel",
                "Popup Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications from Popup Service"
                enableLights(true)
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
