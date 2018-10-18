package com.unimelb.droptable.echo.activities.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskHolder;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskAssistantList extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mRecyclerView = findViewById(R.id.taskList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();

    }

    @NonNull
    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<ImmutableTask> options =
                new FirebaseRecyclerOptions.Builder<ImmutableTask>()
                        .setQuery(FirebaseAdapter.mostRecentTasks, snapshot -> ImmutableTask.builder()
                                .title(snapshot.child("title").getValue(String.class))
                                    .address(snapshot.child("address").getValue(String.class))
                                    .category(snapshot.child("category").getValue(String.class))
                                    .subCategory(snapshot.child("subCategory").getValue(String.class))
                                    .notes(snapshot.child("notes").getValue(String.class))
                                    .paymentAmount(snapshot.child("paymentAmount").getValue(String.class))
                                    .status(snapshot.child("status").getValue(String.class))
                                    .ap(snapshot.child("ap").getValue(String.class))
                                    .lastPhase(snapshot.child("lastPhase").getValue(String.class))
                                    .id(snapshot.child("id").getValue(String.class)).build())
                        .setLifecycleOwner(this)
                        .build();

        Context self = this;

        return new FirebaseRecyclerAdapter<ImmutableTask, TaskHolder>(options) {
            @Override
            public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new TaskHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull ImmutableTask model) {
                holder.bind(model);
                holder.bindParentIntent(self);
            }
        };
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


}
