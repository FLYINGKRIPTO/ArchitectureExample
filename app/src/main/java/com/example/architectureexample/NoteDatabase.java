package com.example.architectureexample;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    //here we create a NoteDatabase instance named instance
    //we create this variable because we need to make this class
    //singleton ,
    //singleton means that we cannot create multiple instances of this
    //database
    private static  NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        //synchronized means that at one time only one thread can access this
        //method
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
