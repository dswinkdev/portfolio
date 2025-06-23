package com.example.eventrack2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;

    // SharedPreferences for storing user credentials
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Initialize UI components
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.BTNlogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Initialize SharedPreferences for storing login credentials
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Handle login button click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Handle register button click to switch to RegisterActivity
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    // Method to handle login
    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Simple validation
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve stored username and password from SharedPreferences
        String storedUsername = sharedPreferences.getString("username", "");
        String storedPassword = sharedPreferences.getString("password", "");

        // Check if the entered credentials match the stored ones
        if (username.equals(storedUsername) && password.equals(storedPassword)) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            // Navigate to EventsActivity
            Intent intent = new Intent(LoginActivity.this, EventsActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Show error message
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open RegisterActivity
    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
