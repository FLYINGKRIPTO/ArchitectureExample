package com.example.architectureexample;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    //we define Data Access Methods here

    //we don't need to define the operations inside this method
    //we just annotate it with @Insert and Room database takes care of all
    //other stuff

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    //For specific queries other than above three
    //like in our note taking app we need to delete all notes at once

    @Query("DELETE FROM note_table")
    void deleteAllNOtes();

    //method to get all notes
   // @Query("SELECT * From note_table ORDER BY priority DESC ")
   // List<Note> getAllNote();

    //Room can be turned to LiveData
    //So we wrap List<Note> in LiveData
    //SO now we can observe this object List<Note> now
    @Query("SELECT * From note_table ORDER BY priority DESC ")
    LiveData<List<Note>> getAllNote();

    //as soon as there are any changes in our note_table
    //the value List<Note> will automatically be changed
    //and our activity will be notified



}
