package com.jagatjit.npuagent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Launching NPUAgent")
        
        startService(Intent(this, HttpServerService::class.java))
        Log.d("MainActivity", "HTTP server started")
        
        finish()
    }
}
