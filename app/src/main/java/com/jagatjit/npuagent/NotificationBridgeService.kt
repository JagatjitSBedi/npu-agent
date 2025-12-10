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
        val extras = notification.extras.getCharSequence("android.text")?.toString() ?: ""
        
        Log.d(TAG, "Notification: $extras")
        
        if (extras.startsWith("npu:")) {
            val prompt = extras.substringAfter("npu:").trim()
            executor.execute {
                Log.d(TAG, "Executing NPU prompt: $prompt")
                val result = NpuBridge.executePrompt(prompt)
                Log.d(TAG, "Result: $result")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
    }
}
