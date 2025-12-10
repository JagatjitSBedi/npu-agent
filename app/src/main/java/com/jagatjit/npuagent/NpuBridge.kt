package com.jagatjit.npuagent

import android.util.Log

object NpuBridge {
    private const val TAG = "NpuBridge"

    fun executePrompt(prompt: String): String {
        return try {
            // Run your NPU model via Termux
            // Example: assumes you have a script at /data/data/com.termux/files/home/npu-agent/run.sh
            val process = Runtime.getRuntime().exec(
                arrayOf(
                    "/data/data/com.termux/files/usr/bin/bash",
                    "-c",
                    "cd /data/data/com.termux/files/home/npu-agent && python3 -c "from tinygradNPU import TinyGradNPU; agent = TinyGradNPU(); result = agent.infer('$prompt'); print(result)""
                )
            )
            
            val output = process.inputStream.bufferedReader().readText()
            val error = process.errorStream.bufferedReader().readText()
            process.waitFor()
            
            if (process.exitValue() == 0) {
                output.trim()
            } else {
                "Error: $error"
            }
        } catch (e: Exception) {
            Log.e(TAG, "NPU execution failed", e)
            "Exception: ${e.message}"
        }
    }
}
