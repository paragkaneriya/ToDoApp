package com.example.todoapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.BeenClass.Task;
import com.example.todoapp.R;
import com.example.todoapp.databinding.RawToDoListBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Viewholder> {

    ArrayList<Task> taskList;
    Context mContext;
    OnItemClickListner closeButtonClick;
    String time;
    OnCheckboxClickListner checkboxClickListner;

    public TaskAdapter(ArrayList<Task> taskList, Context mContext,OnItemClickListner closeButtonClick,OnCheckboxClickListner checkboxClickListner) {
        this.taskList = taskList;
        this.mContext = mContext;
        this.closeButtonClick=closeButtonClick;
        this.checkboxClickListner = checkboxClickListner;
    }

    @NonNull
    @Override
    public TaskAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawToDoListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_to_do_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {

        final int pos = position;

        Task task = taskList.get(position);

        holder.binding.txtEventName.setText(task.getTask_title());

        String first = String.valueOf(task.getTime().charAt(0));

        if(first.equals("0")){

            time = task.getTime().substring(1);

        }else {
            time = task.getTime();
        }

        holder.binding.txtEventTime.setText(time + " " + task.getAm_pm());

        if (task.isDone()){
            holder.binding.cbEvent.setBackground(mContext.getResources().getDrawable(R.drawable.ic_checked));
            holder.binding.txtEventName.setPaintFlags(holder.binding.txtEventName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.binding.cbEvent.setBackground(mContext.getResources().getDrawable(R.drawable.ic_empty_checkbox));
            holder.binding.txtEventName.setPaintFlags(holder.binding.txtEventName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            if (task.isExpired()){

                holder.binding.txtEventName.setTextColor(mContext.getColor(R.color.red));
                holder.binding.txtEventStatus.setVisibility(View.VISIBLE);

            }else {

                holder.binding.txtEventName.setTextColor(mContext.getColor(R.color.txt_event_color));
                holder.binding.txtEventStatus.setVisibility(View.GONE);

            }
        }



        holder.binding.cbEvent.setChecked(task.isIsselected());

        holder.binding.cbEvent.setTag(task);

        holder.binding.cbEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Task task = (Task) cb.getTag();

                task.setIsselected(cb.isChecked());
                taskList.get(pos).setIsselected(cb.isChecked());

                if (cb.isChecked()){
                    holder.binding.cbEvent.setBackground(mContext.getResources().getDrawable(R.drawable.ic_checked));
                    holder.binding.txtEventName.setPaintFlags(holder.binding.txtEventName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    holder.binding.txtEventName.setTextColor(mContext.getColor(R.color.txt_event_color));
                    holder.binding.txtEventStatus.setVisibility(View.GONE);

                }else {
                    holder.binding.cbEvent.setBackground(mContext.getResources().getDrawable(R.drawable.ic_empty_checkbox));
                    holder.binding.txtEventName.setPaintFlags(holder.binding.txtEventName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);


                    if (task.isExpired()){

                        holder.binding.txtEventName.setTextColor(mContext.getColor(R.color.red));
                        holder.binding.txtEventStatus.setVisibility(View.VISIBLE);

                    }
                }

                checkboxClickListner.onCheckboxClick(position);

            }
        });

        holder.binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeButtonClick.onCloseButtonClick(position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RawToDoListBinding binding;

        public Viewholder(@NonNull RawToDoListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public void filterList(ArrayList<Task> taskListNew) {
        taskList = taskListNew;
        notifyDataSetChanged();
    }

    public interface OnItemClickListner{
        public void onCloseButtonClick(int postion);

    }

    public interface OnCheckboxClickListner{
        public void onCheckboxClick(int postion);

    }
}
