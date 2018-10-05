package com.example.android.todoapp.model;

/**
 * Created by Milind Amrutkar on 26-09-2018.
 */
public class Task {
    private String taskId;
    private String taskTitle;
    private String taskComment;

    public Task() {
    }

    public Task(String taskId, String taskTitle) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
    }

    public Task(String taskId, String taskTitle, String comment) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskComment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskComment() {
        return taskComment;
    }
}

