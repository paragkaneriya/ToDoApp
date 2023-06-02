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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    String[] amPm = new String[]{"AM", "PM"};
    String strAmPm;
    ArrayList<Task> tasks;

    public static boolean isValidTime(final String time) {

        Pattern pattern;
        Matcher matcher;
        final String TIME_PATTERN = "[0-1][0-9]:[0-5][0-9]";
        pattern = Pattern.compile(TIME_PATTERN);
        matcher = pattern.matcher(time);

        return matcher.matches();

    }

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
                } else if (binding.edtTime.getText().toString().length() < 5 || !isValidTime(binding.edtTime.getText().toString())) {
                    Toast.makeText(AddTaskActivity.this, "Please Enter Valid Time Format like 00:00", Toast.LENGTH_SHORT).show();

                } else {

                    Task task = new Task(binding.edtTaskTitle.getText().toString(), binding.edtTime.getText().toString(), strAmPm);
                    tasks.add(task);

                    Collections.sort(tasks, new Comparator<Task>() {
                        @Override
                        public int compare(Task o1, Task o2) {


                            try {

                                String current_time_a_o1 = o1.getTime() + " " + o1.getAm_pm().toLowerCase();

                                String current_time_o1 = String.valueOf(new SimpleDateFormat("hh:mm a").parse(current_time_a_o1));

                                String current_time_a_o2 = o2.getTime() + " " + o2.getAm_pm().toLowerCase();

                                String current_time_o2 = String.valueOf(new SimpleDateFormat("hh:mm a").parse(current_time_a_o2));

                                return current_time_o1.compareTo(current_time_o2);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

//                            return o1.getTime().compareTo(o2.getTime());
                        }
                    });

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