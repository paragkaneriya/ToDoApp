package com.example.todoapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.todoapp.BeenClass.Task;
import com.example.todoapp.R;
import com.example.todoapp.databinding.ActivityAddTaskBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    String[] amPm = new String[]{"AM", "PM"};
    String strAmPm;
    ArrayList<Task> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task);

        loadTaskData();

        binding.toolbar.txtToolbarTitle.setText("Add a task");
        binding.toolbar.menu.setVisibility(View.GONE);


        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, amPm);
            binding.spnrAddTask.setAdapter(adapter);

            binding.spnrAddTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strAmPm = amPm[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.acbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtTaskTitle.getText().toString().equals("")) {
                    Toast.makeText(AddTaskActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                } else if (binding.edtTime.getText().toString().equals("")) {
                    Toast.makeText(AddTaskActivity.this, "Please Enter Time", Toast.LENGTH_SHORT).show();
                } else {

                    Task task = new Task(binding.edtTaskTitle.getText().toString(), binding.edtTime.getText().toString(), strAmPm);
                    tasks.add(task);

                    saveTaskData();

                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        binding.acbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private void loadTaskData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("task", null);
        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        tasks = gson.fromJson(json, type);

        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    }

    private void saveTaskData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("task", json);
        editor.apply();
    }

}