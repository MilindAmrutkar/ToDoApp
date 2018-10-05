package com.example.android.todoapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.todoapp.activities.EditToDoActivity;
import com.example.android.todoapp.activities.MainActivity;
import com.example.android.todoapp.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Milind Amrutkar on 26-09-2018.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    private ImageView markIcon;
    public TextView categoryTitle;
    private ImageView deleteIcon;
    private List<Task> taskObject;
    private Context context;

    public RecyclerViewHolders(final View itemView, final List<Task> taskObject, final Context context) {
        super(itemView);
        this.taskObject = taskObject;
        categoryTitle = itemView.findViewById(R.id.task_title);
        markIcon = itemView.findViewById(R.id.task_icon);
        deleteIcon = itemView.findViewById(R.id.task_delete);
        this.context = context;

        categoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditToDoActivity.class);
                intent.putExtra(MainActivity.TASK_ID, taskObject.get(getAdapterPosition()).getTaskId());
                intent.putExtra(MainActivity.TASK_TITLE, taskObject.get(getAdapterPosition()).getTaskTitle());
                intent.putExtra(MainActivity.TASK_COMMENT, taskObject.get(getAdapterPosition()).getTaskComment());
                context.startActivity(intent);
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Task deleted :) ", Toast.LENGTH_SHORT).show();
                final String taskId = taskObject.get(getAdapterPosition()).getTaskId();
                Log.d(TAG, "Task Id: " + taskId);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks").child(taskId);
                ref.removeValue();
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
