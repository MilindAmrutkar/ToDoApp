package com.example.android.todoapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.todoapp.R;
import com.example.android.todoapp.Utility;
import com.example.android.todoapp.adapter.RecyclerViewAdapter;
import com.example.android.todoapp.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String TASK_TITLE = "com.example.android.todoapp.activities.tasktitle";
    public static final String TASK_ID = "com.example.android.todoapp.activities.taskid";
    public static final String TASK_COMMENT = "com.example.android.todoapp.activities.taskcomment";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText addTaskBox;
    private DatabaseReference databaseReference;
    private List<Task> allTask;
    private Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allTask = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        addTaskButton = findViewById(R.id.add_task_button);
        addTaskBox = findViewById(R.id.add_task_box);
        recyclerView = findViewById(R.id.task_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allTask);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void addTask() {
        String enteredTask = addTaskBox.getText().toString().trim();

        if (TextUtils.isEmpty(enteredTask)) {
            Toast.makeText(MainActivity.this, "What should I add? Please input something..", Toast.LENGTH_SHORT).show();
            return;
        }

        if (enteredTask.length() < 6) {
            Toast.makeText(MainActivity.this, "Really? Your task is less than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskId = databaseReference.push().getKey();

        Task taskObject = new Task(taskId, enteredTask, "");
        databaseReference.child(taskId).setValue(taskObject);

        addTaskBox.setText("");

        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "addTask: TaskId: " + taskId + " TaskTitle: " + enteredTask);

        Utility.hideKeyboard(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTask.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    allTask.add(task);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }
}
