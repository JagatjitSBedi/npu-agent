package com.jagatjit.npuagent

import android.util.Log

object NpuBridge {
    private const val TAG = "NpuBridge"

    fun executePrompt(prompt: String): String {
        return try {
            val process = Runtime.getRuntime().exec(
                arrayOf(
                    "/data/data/com.termux/files/usr/bin/bash",
                    "-c",
                    "echo 'Model inference result for: $prompt'"
                )
            )
            
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            
            if (process.exitValue() == 0) {
                output.trim()
            } else {
                "Error executing model"
            }
        } catch (e: Exception) {
            Log.e(TAG, "NPU execution failed", e)
            "Exception: ${e.message}"
        }
    }
}
