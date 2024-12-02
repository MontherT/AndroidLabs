package com.example.androidlabs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ToDoAdapter extends BaseAdapter {
    private Context context;
    private List<ToDoItem> toDoList;

    // Constructor
    public ToDoAdapter(Context context, List<ToDoItem> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return toDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.todo_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current item
        ToDoItem currentItem = toDoList.get(position);

        // Set item text and style
        holder.textView.setText(currentItem.getText());
        if (currentItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            holder.textView.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.textView.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
