package com.example.notesave;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

  //  private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });

        //method call to initialize display of list of courses to listActivity
        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

        private void initializeDisplayContent () {

                final RecyclerView  recyclerNotes =(RecyclerView) findViewById(R.id.list_notes);
                final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
                recyclerNotes.setLayoutManager(notesLayoutManager);
                List<NoteInfo> notes = DataManager.getInstance().getNotes();
                final NoteRecyclerAdapter noteRecyclerAdapter = new NoteRecyclerAdapter(this  ,notes);
                recyclerNotes.setAdapter(noteRecyclerAdapter);
        }


    }
