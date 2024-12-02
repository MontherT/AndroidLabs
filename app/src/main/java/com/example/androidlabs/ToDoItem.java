package com.example.androidlabs;

public class ToDoItem {
    private String text;
    private boolean isUrgent;

    // Constructor
    public ToDoItem(String text, boolean isUrgent) {
        this.text = text;
        this.isUrgent = isUrgent;
    }

    // Getters
    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return isUrgent;
    }
}
