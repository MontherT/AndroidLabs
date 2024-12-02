package com.example.androidlabs;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList = new ArrayList<>();
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        ListView listView = findViewById(R.id.todo_list);
        EditText todoInput = findViewById(R.id.todo_input);
        Switch urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        // Initialize Adapter
        adapter = new ToDoAdapter(this, toDoList);
        listView.setAdapter(adapter);

        // Add Button Logic
        addButton.setOnClickListener(v -> {
            String text = todoInput.getText().toString().trim();
            boolean isUrgent = urgentSwitch.isChecked();

            if (!text.isEmpty()) {
                // Add new item to the list
                toDoList.add(new ToDoItem(text, isUrgent));
                adapter.notifyDataSetChanged();
                todoInput.setText("");
            } else {
                Toast.makeText(this, getString(R.string.enter_task), Toast.LENGTH_SHORT).show();
            }
        });

        // Press and hold to Delete Item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Show AlertDialog to confirm deletion
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_title))
                    .setMessage(getString(R.string.dialog_message) + " " + position)
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        // Remove item and refresh list
                        toDoList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        });
    }
}
