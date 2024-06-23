package com.example.myjournal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotesDetailActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText, tagsText;
    ImageButton saveNoteButton;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        tagsText = findViewById(R.id.tags_text);
        saveNoteButton = findViewById(R.id.save_note_button);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewButton = findViewById(R.id.delete_note_text_view_button);

        // receive Data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
            loadNoteFromFirebase();
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your Note");
            deleteNoteTextViewButton.setVisibility(View.VISIBLE);
        }

        saveNoteButton.setOnClickListener((v)->saveNote());
        deleteNoteTextViewButton.setOnClickListener((v)-> deleteNoteFromFirebase());


    }

    void loadNoteFromFirebase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    Note note = task.getResult().toObject(Note.class);

                    if(note != null){
                        titleEditText.setText(note.getTitle());
                        contentEditText.setText(note.getContent());
                        tagsText.setText(note.getTags());  // Set the tags
                    }
                } else {
                    Utility.showToast(NotesDetailActivity.this, "Failed to load note");
                }
            }
        });
    }


    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String tags = tagsText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is Required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTags(tags);
        note.setTimestamp(Timestamp.now() );

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            // Update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else {
            // Create new Note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NotesDetailActivity.this,"Note Added Successfully");
                    finish();
                }else {
                    // notes is not added
                    Utility.showToast(NotesDetailActivity.this,"Failed while adding Notes");
                }
            }
        });

    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is deleted
                    Utility.showToast(NotesDetailActivity.this,"Note Deleted Successfully");
                    finish();
                }else {
                    // notes is not added
                    Utility.showToast(NotesDetailActivity.this,"Failed while Deleting Note");
                }
            }
        });
    }



}