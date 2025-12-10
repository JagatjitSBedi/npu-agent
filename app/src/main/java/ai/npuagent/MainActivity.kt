package ai.npuagent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("npuagent_native")
        }
    }

    external fun logGpuInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log GPU info once
        logGpuInfo()

        // Start HTTP server service
        startService(Intent(this, HttpServerService::class.java))

        // Optionally finish if you want a headless app
        finish()
    }
}
