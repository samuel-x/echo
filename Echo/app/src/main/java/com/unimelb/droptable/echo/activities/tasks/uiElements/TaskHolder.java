package com.unimelb.droptable.echo.activities.tasks.uiElements;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantDetails;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskHolder extends RecyclerView.ViewHolder {

    private Context parentContext;
    private ImmutableTask thisTask;

    private final TextView titleField;
    private final TextView taskNotes;
    private final TextView taskAddress;
    private final TextView taskCategory;
    private final TextView taskSubCategory;
    private final Button taskButton;

    public TaskHolder(@NonNull View itemView) {
        super(itemView);
        titleField = itemView.findViewById(R.id.taskTitle);
        taskNotes = itemView.findViewById(R.id.taskNotes);
        taskAddress = itemView.findViewById(R.id.taskAddress);
        taskCategory = itemView.findViewById(R.id.taskCategory);
        taskSubCategory = itemView.findViewById(R.id.taskSubcategory);
        taskButton = itemView.findViewById(R.id.acceptTask);

        taskButton.setOnClickListener((view) -> onAcceptButton());
    }

    private void onAcceptButton() {
        parentContext.startActivity(new Intent(parentContext, TaskAssistantDetails.class).putExtra("task", thisTask));
    }

    public void bind(@NonNull ImmutableTask task) {
        thisTask = task;
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setCategory(task.getCategory());
        setSubCategory(task.getSubCategory());
        setNotes(task.getNotes());
    }

    private void setTitle(@Nullable String title) {
        titleField.setText(title);
    }

    private void setNotes(@Nullable String notes) {
        taskNotes.setText(notes);
    }

    private void setAddress(@Nullable String address) {
        taskAddress.setText(address);
    }

    private void setCategory(@Nullable String category) {
        taskCategory.setText(category);
    }

    private void setSubCategory(@Nullable String subCategory) {
        taskSubCategory.setText(subCategory);
    }


    public void bindParentIntent(Context context) {
        this.parentContext = context;
    }
}