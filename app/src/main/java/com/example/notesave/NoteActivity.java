package com.example.notesave;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.concurrent.Callable;

public class NoteActivity extends AppCompatActivity {
   // public static final String NOTE_INFO = "com.example.NOTE_POSITION";
    public static final String NOTE_POSITION ="com.example.NOTE_POSITION" ;
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNoteposition;
    private boolean mIsCanceling;
   // private String mOriginalNoteCourseId;
  //  private String mOriginalNoteTitle;
 //   private String mOriginalNoteText
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//setting up the view model to manage our state instance values activities life cycle
        ViewModelProvider  viewModelProvider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);
       //restoring the state of the previous activity during configuration changes
        if(mViewModel.mIsNewlyCreated && savedInstanceState !=null)
            mViewModel.restoreState(savedInstanceState);
            mViewModel.mIsNewlyCreated = false;


        mSpinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

         readDisplayStateValues();
        saveOriginalNoteValues();
        mTextNoteTitle = findViewById(R.id.note_text_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if(!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    private void saveOriginalNoteValues() {
         if(mIsNewNote)
             return;
      mViewModel.mOriginalNoteCourseId = mNote.getCourse().getCourseId();

        mViewModel.mOriginalNoteTitle = mNote.getTitle();

        mViewModel.mOriginalNoteText = mNote.getText();
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (mIsCanceling){
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNoteposition);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState !=null)
            mViewModel.saveState(outState);
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);

    }

    private void saveNote() {
        mNote.setCourse((CourseInfo)mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if(mIsNewNote){
            createNewNote();
        } else{
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }
    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteposition = dm.createNewNote();
        mNote = dm.getNotes().get(mNoteposition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id==R.id.action_cancel){
            mIsCanceling = true;
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo course =(CourseInfo)mSpinnerCourses.getSelectedItem();
        String subject =mTextNoteTitle.getText().toString();
        String text = "If you love android Chekout this cool staff\""+ course.getTitle()+ "\"\n"+mTextNoteText.getText();

        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);


    }


}
