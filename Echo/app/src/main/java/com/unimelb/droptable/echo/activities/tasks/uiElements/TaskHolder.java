package com.unimelb.droptable.echo.activities.tasks.uiElements;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskHolder extends RecyclerView.ViewHolder {
    private final TextView titleField;
    private final TextView taskNotes;
    private final TextView taskAddress;
    private final TextView taskCategory;
    private final TextView taskSubCategory;

    public TaskHolder(@NonNull View itemView) {
        super(itemView);
        titleField = itemView.findViewById(R.id.taskTitle);
        taskNotes = itemView.findViewById(R.id.taskNotes);
        taskAddress = itemView.findViewById(R.id.taskAddress);
        taskCategory = itemView.findViewById(R.id.taskCategory);
        taskSubCategory = itemView.findViewById(R.id.taskSubcategory);
    }

    public void bind(@NonNull ImmutableTask task) {
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setCategory(task.getCategory());
        setSubCategory(task.getSubCategory());
        setNotes(task.getNotes());
        Log.d("Bind:", "what the heck");
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
}