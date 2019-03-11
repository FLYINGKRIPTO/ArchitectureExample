package com.example.architectureexample;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
//we define this class as entity because at runtime table will be created seeing
//this annotation

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    //Room will automatically generate columns for these fields
    private String title;
    private String description;
    private String priority;

    public Note(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}
