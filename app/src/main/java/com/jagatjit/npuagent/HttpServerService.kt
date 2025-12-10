package com.jagatjit.npuagent

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.net.ServerSocket
import java.net.URLDecoder
import java.util.concurrent.Executors

class HttpServerService : Service() {

    private val executor = Executors.newSingleThreadExecutor()
    @Volatile private var running = false
    private val TAG = "HttpServerService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!running) {
            running = true
            executor.execute { runServer() }
            Log.d(TAG, "HTTP server started on port 8080")
        }
        return START_STICKY
    }

    private fun runServer() {
        try {
            val server = ServerSocket(8080)
            while (running) {
                val socket = server.accept()
                executor.execute { handleRequest(socket) }
            }
            server.close()
        } catch (e: Exception) {
            Log.e(TAG, "Server error", e)
        }
    }

    private fun handleRequest(socket: java.net.Socket) {
        try {
            socket.use { sock ->
                val reader = sock.getInputStream().bufferedReader()
                val line = reader.readLine() ?: return@use
                
                val parts = line.split(" ")
                if (parts.size < 2) return@use
                
                val path = parts[1]
                val response = when {
                    path == "/" -> buildResponse(200, "NPUAgent HTTP bridge alive")
                    path.startsWith("/infer?") -> {
                        val query = path.substringAfter("?")
                        val params = mutableMapOf<String, String>()
                        query.split("&").forEach {
                            val (k, v) = it.split("=", limit = 2)
                            params[k] = URLDecoder.decode(v, "UTF-8")
                        }
                        val prompt = params["prompt"] ?: ""
                        
                        Log.d(TAG, "Received prompt: $prompt")
                        val result = NpuBridge.executePrompt(prompt)
                        Log.d(TAG, "NPU result: $result")
                        
                        val jsonBody = "{"status":"ok","result":"$result"}"
                        buildResponse(200, jsonBody)
                    }
                    path == "/status" -> {
                        val ts = System.currentTimeMillis()
                        buildResponse(200, "{"status":"running","timestamp":$ts}")
                    }
                    else -> buildResponse(404, "Not found")
                }
                
                sock.getOutputStream().write(response.toByteArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Request handling error", e)
        }
    }

    private fun buildResponse(code: Int, body: String): String {
        val status = if (code == 200) "OK" else "Not Found"
        val codeStr = code.toString()
        val contentLen = body.toByteArray().size
        
        val sb = StringBuilder()
        sb.append("HTTP/1.1 ").append(codeStr).append(" ").append(status).append("
")
        sb.append("Content-Type: application/json
")
        sb.append("Content-Length: ").append(contentLen).append("
")
        sb.append("Connection: close
")
        sb.append("
")
        sb.append(body)
        return sb.toString()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        running = false
        executor.shutdownNow()
        super.onDestroy()
    }
}
