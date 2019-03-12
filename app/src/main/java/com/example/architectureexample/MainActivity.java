package com.example.architectureexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we create a member variable of our view model

    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.add_note_fab);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);

            }
        });

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Note Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){

            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);

            Toast.makeText(MainActivity.this,"Note Saved",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(MainActivity.this,"Note not saved",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this,"All notes deleted",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
