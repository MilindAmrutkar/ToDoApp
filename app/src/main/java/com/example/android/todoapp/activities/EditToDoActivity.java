package com.example.android.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.todoapp.R;
import com.example.android.todoapp.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditToDoActivity extends AppCompatActivity {
    private static final String TAG = EditToDoActivity.class.getSimpleName();
    private String taskTitle, taskComment, taskId;
    private EditText taskEditText, commentEditText;
    private Button saveButton, cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        taskEditText = findViewById(R.id.task_edit);
        commentEditText = findViewById(R.id.task_comment);
        saveButton = findViewById(R.id.save_edit);
        cancelButton = findViewById(R.id.cancel_edit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            taskTitle = null;
            taskComment = null;
            taskId = null;
        } else {
            taskId = extras.getString(MainActivity.TASK_ID);
            taskTitle = extras.getString(MainActivity.TASK_TITLE);
            taskComment = extras.getString(MainActivity.TASK_COMMENT);
            Log.d(TAG, "extraId: " + taskId + " extraTitle: " + taskTitle + " extraComment: " + taskComment);
            taskEditText.setText(taskTitle);
            commentEditText.setText(taskComment);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting new values from fields
                final String enteredTask = taskEditText.getText().toString();
                final String enteredComment = commentEditText.getText().toString();

                String titleStatus = checkForEmptyFields(enteredTask);
                String commentStatus = checkForEmptyFields(enteredComment);

                Log.d(TAG, "onClick: titleStatus: " + titleStatus + " commentStatus: " + commentStatus);

                if (titleStatus != null && commentStatus != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks").child(taskId);

                    Task task = new Task(taskId, enteredTask, enteredComment);

                    ref.setValue(task);
                    Toast.makeText(EditToDoActivity.this, "Changes updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String checkForEmptyFields(String editTextContent) {
        if (TextUtils.isEmpty(editTextContent)) {
            Toast.makeText(EditToDoActivity.this, "Please give me something!!!!", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (editTextContent.length() < 6) {
            Toast.makeText(EditToDoActivity.this, "Give me more than 6 characters :)", Toast.LENGTH_SHORT).show();
            return null;
        }
        return "OK";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
