package com.jagatjit.npuagent

import android.app.Service
import android.content.Intent
import android.os.IBinder

class HttpServerService : Service() {
    override fun onCreate() {
        super.onCreate()
        // TODO: start your HTTP server here
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: stop your HTTP server here
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
