package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        TextView tvWelcome = findViewById(R.id.textViewWelcome);
        Button btnThankYou = findViewById(R.id.btnThankYou);
        Button btnDontCallMe = findViewById(R.id.btnDontCallMe);

        // Retrieve name from intent
        String name = getIntent().getStringExtra("name");
        if (name != null) {
            String welcomeMessage = getString(R.string.welcome_text, name);
            tvWelcome.setText(welcomeMessage);
        }

        // Handle "Thank You" button click
        btnThankYou.setOnClickListener(v -> {
            setResult(1);
            finish();
        });

        // Handle "Don't call me that" button click
        btnDontCallMe.setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }
}
