package com.unimelb.droptable.echo.activities.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskHolder;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskList extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mRecyclerView = findViewById(R.id.task_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @NonNull
    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<ImmutableTask> options =
                new FirebaseRecyclerOptions.Builder<ImmutableTask>()
                        .setQuery(FirebaseAdapter.mostRecentTasks, new SnapshotParser<ImmutableTask>() {
                            @NonNull
                            @Override
                            public ImmutableTask parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return ImmutableTask.builder()
                                        .title(snapshot.child("title").getValue(String.class))
                                            .address(snapshot.child("address").getValue(String.class))
                                            .category(snapshot.child("category").getValue(String.class))
                                            .subCategory(snapshot.child("subCategory").getValue(String.class))
                                            .notes(snapshot.child("notes").getValue(String.class)).build();
                            }
                        })
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<ImmutableTask, TaskHolder>(options) {
            @Override
            public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new TaskHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull ImmutableTask model) {
                holder.bind(model);
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
