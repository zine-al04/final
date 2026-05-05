package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.database.TaskDatabase;
import com.example.todolist.structure.Task;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewTask extends AppCompatActivity {

    private EditText titleText, btndate, btntime;
    private Button addtaskbtn;
    private ImageView btnback;
    private TaskDatabase database;
    private ExecutorService executorService;


    private int taskId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_task);


        titleText = findViewById(R.id.titletext);
        btndate = findViewById(R.id.btndate);
        btntime = findViewById(R.id.btntime);
        addtaskbtn = findViewById(R.id.addtaskbtn);
        btnback = findViewById(R.id.btnback);


        database = TaskDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();


        checkForEditData();


        btndate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        btndate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


        btntime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        btntime.setText(time);
                    }, hour, minute, true);
            timePickerDialog.show();
        });


        btnback.setOnClickListener(v -> finish());


        addtaskbtn.setOnClickListener(v -> {
            if (isEditMode) {
                updateTask();
            } else {
                saveNewTask();
            }
        });
    }


    private void checkForEditData() {
        taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId != -1) {
            isEditMode = true;
            String title = getIntent().getStringExtra("task_title");
            String date = getIntent().getStringExtra("task_date");
            String time = getIntent().getStringExtra("task_time");


            if (title != null) titleText.setText(title);
            if (date != null) btndate.setText(date);
            if (time != null) btntime.setText(time);


            addtaskbtn.setText("Update Task");
        } else {
            isEditMode = false;
            addtaskbtn.setText("Add Task");
        }
    }


    private void saveNewTask() {
        String title = titleText.getText().toString().trim();
        String date = btndate.getText().toString().trim();
        String time = btntime.getText().toString().trim();

        if (!validateInputs(title, date, time)) return;

        Task newTask = new Task(time, date, title);

        executorService.execute(() -> {
            database.TaskDao().insert(newTask);
            runOnUiThread(() -> {
                Toast.makeText(NewTask.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }


    private void updateTask() {
        String title = titleText.getText().toString().trim();
        String date = btndate.getText().toString().trim();
        String time = btntime.getText().toString().trim();

        if (!validateInputs(title, date, time)) return;


        Task updatedTask = new Task(time, date, title, taskId);

        executorService.execute(() -> {
            database.TaskDao().update(updatedTask);
            runOnUiThread(() -> {
                Toast.makeText(NewTask.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }


    private boolean validateInputs(String title, String date, String time) {
        if (title.isEmpty()) {
            titleText.setError("Title is required");
            titleText.requestFocus();
            return false;
        }

        if (date.isEmpty()) {
            btndate.setError("Date is required");
            btndate.requestFocus();
            return false;
        }

        if (time.isEmpty()) {
            btntime.setError("Time is required");
            btntime.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}