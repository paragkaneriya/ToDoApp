package com.example.todoapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TaskAdapter;
import com.example.todoapp.BeenClass.Task;
import com.example.todoapp.R;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListner {

    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<Task> tasks;
    TaskAdapter adapter;
    Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);

            }
        });
        loadTaskData();

        if (tasks.size() > 0) {
            adapter = new TaskAdapter(tasks, MainActivity.this, MainActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            binding.rclrToDoList.setLayoutManager(layoutManager);
            binding.rclrToDoList.setAdapter(adapter);
        } else {
            binding.rclrToDoList.setVisibility(View.GONE);
        }

        timer.scheduleAtFixedRate(new myTimerTask(), 0, 30000);


    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


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

    @Override
    public void onCloseButtonClick(int postion) {

        Task task = tasks.get(postion);


        final AlertDialog dialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.raw_dialog, null);
        dialogBuilder.setCancelable(false);

        final TextView txt_dialog_msg = dialogView.findViewById(R.id.txt_dialog_msg);
        final TextView txt_dialog_ok = dialogView.findViewById(R.id.txt_dialog_ok);
        final TextView txt_dialog_cancel = dialogView.findViewById(R.id.txt_dialog_cancel);


        String msg = "Do you want to delete “" + task.getTask_title() + "”, this action can’t be undone.";

        txt_dialog_msg.setText(msg);

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(task);
                saveTaskData();

                adapter.filterList(tasks);

                dialogBuilder.dismiss();
            }
        });

        txt_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBuilder.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public class myTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < tasks.size(); i++) {

                            Task task = tasks.get(i);

                            String date_a = new SimpleDateFormat("hh:mm a").format(new Date());


                            String current_time_a = task.getTime() + " " + task.getAm_pm().toLowerCase();
                            try {
                                String date = String.valueOf(new SimpleDateFormat("hh:mm a").parse(date_a));
                                String current_time = String.valueOf(new SimpleDateFormat("hh:mm a").parse(current_time_a));
                                Log.e("time", date + " / " + current_time);
                                task.setExpired(current_time.compareTo(date) < 0);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapter.filterList(tasks);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}