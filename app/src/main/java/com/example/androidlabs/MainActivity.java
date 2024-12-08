package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList = new ArrayList<>();
    private ToDoAdapter adapter;
    private ToDoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Database Helper
        dbHelper = new ToDoDatabaseHelper(this);

        // Initialize Adapter before setting it on the ListView
        adapter = new ToDoAdapter(this, toDoList);

        // Initialize Views
        ListView listView = findViewById(R.id.todo_list);
        EditText todoInput = findViewById(R.id.todo_input);
        Switch urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // Load tasks from database
        loadToDos();

        // Add Button Logic
        addButton.setOnClickListener(v -> {
            String text = todoInput.getText().toString().trim();
            boolean isUrgent = urgentSwitch.isChecked();

            if (!text.isEmpty()) {
                // Save to database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ToDoDatabaseHelper.COLUMN_TEXT, text);
                values.put(ToDoDatabaseHelper.COLUMN_URGENT, isUrgent ? 1 : 0);
                long newRowId = db.insert(ToDoDatabaseHelper.TABLE_NAME, null, values);

                // Debugging: Check if the insert was successful
                if (newRowId != -1) {
                    Log.d("DatabaseDebug", "Task inserted with ID: " + newRowId);
                } else {
                    Log.d("DatabaseDebug", "Failed to insert task.");
                }

                // Add to list and refresh ListView
                toDoList.add(new ToDoItem(text, isUrgent));
                adapter.notifyDataSetChanged();
                todoInput.setText("");
            } else {
                Toast.makeText(this, getString(R.string.enter_task), Toast.LENGTH_SHORT).show();
            }
        });

        // Press and hold to Delete Item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_title))
                    .setMessage(getString(R.string.dialog_message) + " " + position)
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        // Delete from database
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String whereClause = ToDoDatabaseHelper.COLUMN_TEXT + " = ?";
                        String[] whereArgs = {toDoList.get(position).getText()};
                        db.delete(ToDoDatabaseHelper.TABLE_NAME, whereClause, whereArgs);

                        // Remove from list and refresh
                        toDoList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        });
    }

    private void loadToDos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ToDoDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        Log.d("DatabaseDebug", "Cursor Count: " + cursor.getCount());

        toDoList.clear();

        // Loop through cursor and add tasks to the list
        while (cursor.moveToNext()) {
            String text = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.COLUMN_TEXT));
            boolean isUrgent = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.COLUMN_URGENT)) == 1;

            Log.d("DatabaseDebug", "Row: " + text + ", Urgent: " + isUrgent);

            toDoList.add(new ToDoItem(text, isUrgent));
        }

        cursor.close();

        Log.d("DatabaseDebug", "Tasks loaded: " + toDoList.size());

        // Notify the adapter to refresh the ListView
        adapter.notifyDataSetChanged();
    }


    private void printCursor(Cursor c) {
        Log.d("DatabaseDebug", "Database Version: " + dbHelper.getReadableDatabase().getVersion());
        Log.d("DatabaseDebug", "Number of Columns: " + c.getColumnCount());
        Log.d("DatabaseDebug", "Column Names: " + Arrays.toString(c.getColumnNames()));
        Log.d("DatabaseDebug", "Number of Rows: " + c.getCount());

        while (c.moveToNext()) {
            String row = "Row " + c.getPosition() + ": ";
            for (int i = 0; i < c.getColumnCount(); i++) {
                row += c.getColumnName(i) + " = " + c.getString(i) + ", ";
            }
            Log.d("DatabaseDebug", row);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close database connection
        super.onDestroy();
    }
}

