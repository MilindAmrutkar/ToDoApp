package com.example.android.todoapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.todoapp.R;
import com.example.android.todoapp.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditToDoActivity extends AppCompatActivity {
    private static final String TAG = EditToDoActivity.class.getSimpleName();
    private String todoTitle;
    private EditText editText;
    private Button saveButton, cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        editText = findViewById(R.id.task_edit);
        saveButton = findViewById(R.id.save_edit);
        cancelButton = findViewById(R.id.cancel_edit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            todoTitle = null;
        } else {
            todoTitle = extras.getString("Task");
            editText.setText(todoTitle);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String enteredTask = editText.getText().toString();
                if (TextUtils.isEmpty(enteredTask)) {
                    Toast.makeText(EditToDoActivity.this, "Please enter something!!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enteredTask.length() < 6) {
                    Toast.makeText(EditToDoActivity.this, "Don't have such a small task", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.orderByChild("task").equalTo(todoTitle);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Task task = new Task(enteredTask);
                            snapshot.getRef().setValue(task);
                            Toast.makeText(EditToDoActivity.this, "Changes saved :)", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: ", databaseError.toException());
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
