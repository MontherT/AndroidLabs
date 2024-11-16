package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private boolean previousState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        TextView initialText = findViewById(R.id.textview);
        EditText editedText = findViewById(R.id.edittext);
        Button btn = findViewById(R.id.button);
        CheckBox checkBox = findViewById(R.id.checkbox);


        String message = getString(R.string.hello_text);
        previousState = checkBox.isChecked();
        editedText.setText(message);

        // Set the OnClickListener for button
        btn.setOnClickListener(v -> {
            String textFromEdit = editedText.getText().toString();

            initialText.setText(textFromEdit);

            String toastMessage = getString(R.string.toast_message);
            Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        });

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            final boolean stateBeforeChange = previousState;

            // Localization of State message.
            String checkState = isChecked ? getString(R.string.state_on) : getString(R.string.state_off);
            String stateMessage = getString(R.string.checkbox_state_message, checkState);

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), stateMessage, Snackbar.LENGTH_LONG);

            // Set the action for "Undo"
            snackbar.setAction(getString(R.string.undo_action), view -> checkBox.setChecked(stateBeforeChange));

            snackbar.show();
            previousState = isChecked;
        });
    }
}
