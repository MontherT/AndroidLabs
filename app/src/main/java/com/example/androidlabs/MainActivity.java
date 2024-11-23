package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText userName = findViewById(R.id.edittext);

        // Load name from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedName = prefs.getString("savedName", null);
        if (savedName != null) {
            userName.setText(savedName);
        }

        Button btnNext = findViewById(R.id.button);
        btnNext.setOnClickListener(v -> {
            String name = userName.getText().toString();

            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("name", name);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText etName = findViewById(R.id.edittext);
        String name = etName.getText().toString();

        // Save name to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedName", name);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if user pressed Thank You
        if (requestCode == 1) {
            if (resultCode == 1) {
                finish(); // Close MainActivity and the app
            }
        }
    }
}
