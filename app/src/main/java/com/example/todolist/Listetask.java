package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.apdater.TaskAdapter;
import com.example.todolist.database.TaskDatabase;
import com.example.todolist.structure.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listetask extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listetask);


        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);


        database = TaskDatabase.getInstance(this);


        executorService = Executors.newSingleThreadExecutor();


        loadTasks();


        taskAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEditClick(Task task) {
                Intent intent = new Intent(Listetask.this, NewTask.class);
                intent.putExtra("task_id", task.getId());
                intent.putExtra("task_title", task.getTitle());
                intent.putExtra("task_date", task.getDate());
                intent.putExtra("task_time", task.getTime());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Task task) {

                executorService.execute(() -> {
                    database.TaskDao().delete(task);
                    runOnUiThread(() -> {
                        loadTasks();
                        Toast.makeText(Listetask.this, "Task deleted", Toast.LENGTH_SHORT).show();
                    });
                });
            }

            @Override
            public void onCheckBoxClick(Task task, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(Listetask.this, "Task completed: " + task.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button plus = findViewById(R.id.plus);
        plus.setOnClickListener(v -> {
            Intent intent = new Intent(Listetask.this, NewTask.class);
            startActivity(intent);
        });
    }

    private void loadTasks() {

        executorService.execute(() -> {
            List<Task> tasks = database.TaskDao().getAllTasks();

            runOnUiThread(() -> {
                taskAdapter.setTasks(tasks);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}