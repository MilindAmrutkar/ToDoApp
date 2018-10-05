package com.example.android.todoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.todoapp.R;
import com.example.android.todoapp.RecyclerViewHolders;
import com.example.android.todoapp.model.Task;

import java.util.List;

/**
 * Created by Milind Amrutkar on 26-09-2018.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Task> task;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Task> task) {
        this.task = task;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, task, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
        holder.categoryTitle.setText(task.get(position).getTaskTitle());
    }

    @Override
    public int getItemCount() {
        return this.task.size();
    }
}
