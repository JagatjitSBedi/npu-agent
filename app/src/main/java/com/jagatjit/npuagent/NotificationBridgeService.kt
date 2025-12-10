package com.jagatjit.npuagent

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.util.concurrent.Executors

class NotificationBridgeService : NotificationListenerService() {

    private val executor = Executors.newSingleThreadExecutor()
    private val TAG = "NotificationBridge"

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.contentIntent?.let { 
            // Extract text from notification
            notification.extras.getCharSequence("android.text")?.toString() ?: ""
        } ?: ""
        
        Log.d(TAG, "Notification: $extras")
        
        // If notification contains "npu:" prefix, execute it as a prompt
        if (extras.startsWith("npu:")) {
            val prompt = extras.substringAfter("npu:").trim()
            executor.execute {
                Log.d(TAG, "Executing NPU prompt: $prompt")
                val result = NpuBridge.executePrompt(prompt)
                Log.d(TAG, "Result: $result")
                // Optionally post result back as notification
                // postResultNotification(result)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optional: log removed notifications
    }
}
