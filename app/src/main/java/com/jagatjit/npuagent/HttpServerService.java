package com.jagatjit.npuagent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HttpServerService extends Service {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;
    private static final String TAG = "HttpServerService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!running) {
            running = true;
            executor.execute(this::runServer);
            Log.d(TAG, "HTTP server started on port 8080");
        }
        return START_STICKY;
    }

    private void runServer() {
        try {
            ServerSocket server = new ServerSocket(8080);
            while (running) {
                java.net.Socket socket = server.accept();
                executor.execute(() -> handleRequest(socket));
            }
            server.close();
        } catch (Exception e) {
            Log.e(TAG, "Server error", e);
        }
    }

    private void handleRequest(java.net.Socket socket) {
        try (socket) {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(socket.getInputStream())
            );
            String line = reader.readLine();
            if (line == null) return;
            
            String[] parts = line.split(" ");
            if (parts.length < 2) return;
            
            String path = parts[1];
            String response;
            
            if (path.equals("/")) {
                response = buildResponse(200, "NPUAgent HTTP bridge alive");
            } else if (path.startsWith("/infer?")) {
                String query = path.substring(path.indexOf('?') + 1);
                Map<String, String> params = new HashMap<>();
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2) {
                        params.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()));
                    }
                }
                String prompt = params.getOrDefault("prompt", "");
                Log.d(TAG, "Received prompt: " + prompt);
                String result = NpuBridge.executePrompt(prompt);
                Log.d(TAG, "NPU result: " + result);
                String jsonBody = "{"status":"ok","result":"" + result + ""}";
                response = buildResponse(200, jsonBody);
            } else if (path.equals("/status")) {
                long ts = System.currentTimeMillis();
                response = buildResponse(200, "{"status":"running","timestamp":" + ts + "}");
            } else {
                response = buildResponse(404, "Not found");
            }
            
            socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e(TAG, "Request handling error", e);
        }
    }

    private String buildResponse(int code, String body) {
        String statusText = (code == 200) ? "OK" : "Not Found";
        int contentLen = body.length();
        return "HTTP/1.1 " + code + " " + statusText + "
" +
               "Content-Type: application/json
" +
               "Content-Length: " + contentLen + "
" +
               "Connection: close

" +
               body;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        running = false;
        executor.shutdownNow();
        super.onDestroy();
    }
}
