package ai.npuagent

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class HttpServerService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.i("NPUAgent", "HttpServerService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("NPUAgent", "HttpServerService onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i("NPUAgent", "HttpServerService onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
