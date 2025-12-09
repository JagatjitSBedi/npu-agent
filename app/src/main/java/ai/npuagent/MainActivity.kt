package ai.npuagent

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
        // No UI; just log GPU info once on startup.
        logGpuInfo()
    }
}
