package com.example.architectureexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we create a member variable of our view model

    private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        //we assign the noteViewModel member variable
        //but we don't call a new noteViewModel
        //because then we will create a new instance with every new activity
        //
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        //Now we can take our noteViewModel and call our getAllNotes getter method
        //and since this returns LiveData
        //we need to call observe, which is a liveData method
        //we need to pass two arguments
        //first one is the LifeCycle Owner

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //this will be triggered everytime data in our
                //Live Data object changes

                //we will update our recycler view here
                // this will only be called when our activity is in the foreground
                //and when the activity is destroyed
                //this will not hold any refrence to this activity any more

                adapter.setNotes(notes);
                Toast.makeText(MainActivity.this,"onChanged",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
