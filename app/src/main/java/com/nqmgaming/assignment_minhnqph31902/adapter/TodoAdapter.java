package com.nqmgaming.assignment_minhnqph31902.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<TodoDTO> todoDTOArrayList;

    public TodoAdapter(Context context, ArrayList<TodoDTO> todoDTOArrayList) {
        this.context = context;
        this.todoDTOArrayList = todoDTOArrayList;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;
        TodoDTO todoDTO = todoDTOArrayList.get(pos);
        holder.txtNameTodo.setText(todoDTO.getName());
        AtomicBoolean isCheck = new AtomicBoolean(false);
        //Create anmtion for checkbox if check setbackground is green and set text is done
        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                isCheck.set(true);
                holder.txtNameTodo.setText("Done");
            } else {
                isCheck.set(false);

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView txtNameTodo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxAnimation);
            txtNameTodo = itemView.findViewById(R.id.tvNameTodo);
        }
    }

    @Override
    public int getItemCount() {
        return todoDTOArrayList.size();
    }
}
