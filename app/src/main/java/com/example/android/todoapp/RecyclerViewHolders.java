package com.example.android.todoapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.todoapp.activities.EditToDoActivity;
import com.example.android.todoapp.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Milind Amrutkar on 26-09-2018.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    public static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public ImageView markIcon;
    public TextView categoryTitle;
    public ImageView deleteIcon;
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
                intent.putExtra("Task", taskObject.get(getAdapterPosition()).getTask());
                context.startActivity(intent);

            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Task deleted :) ", Toast.LENGTH_SHORT).show();
                final String taskTitle = taskObject.get(getAdapterPosition()).getTask();
                Log.d(TAG, "Task Title " + taskTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.orderByChild("task").equalTo(taskTitle);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }
}
