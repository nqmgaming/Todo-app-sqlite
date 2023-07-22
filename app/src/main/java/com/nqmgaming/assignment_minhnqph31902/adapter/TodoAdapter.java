package com.nqmgaming.assignment_minhnqph31902.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TodoDTO> doneItemsList;
    private final ArrayList<TodoDTO> notDoneItemsList;
    private  ViewBinder viewBinderDone = new ViewBinder();

    public TodoAdapter(Context context, ArrayList<TodoDTO> doneItemsList, ArrayList<TodoDTO> notDoneItemsList) {
        this.context = context;
        this.doneItemsList = doneItemsList;
        this.notDoneItemsList = notDoneItemsList;
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
        TodoDTO todoDTO = doneItemsList.get(position);
        holder.txtNameTodo.setText(todoDTO.getName());
        setItemAppearance(holder, todoDTO.getStatus());

        // Set status for checkbox when clicked
        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                todoDTO.setStatus(0);
                notDoneItemsList.add(todoDTO);

                TodoDAO todoDAO = new TodoDAO(context);
                todoDAO.setStatusTodo(todoDTO);
                doneItemsList.remove(todoDTO);
                Toast.makeText(context, doneItemsList.size() + "+ " + notDoneItemsList.size() + "", Toast.LENGTH_SHORT).show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, doneItemsList.size());
            }
        });
        viewBinderDone.bind(holder.swipeLayout, String.valueOf(todoDTO.getId()));
        holder.tvDelete.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(context);
            todoDAO.deleteTodo(todoDTO);
            doneItemsList.remove(todoDTO);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, doneItemsList.size());
        });
        holder.tvEdit.setOnClickListener(v -> {
            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return doneItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView txtNameTodo;
        ImageButton tvEdit, tvDelete;
        ConstraintLayout constraintTodo;
        CardView cardViewTodo;
        SwipeLayout swipeLayout;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxAnimation);
            txtNameTodo = itemView.findViewById(R.id.tvNameTodo);
            constraintTodo = itemView.findViewById(R.id.constraintTodo);
            cardViewTodo = itemView.findViewById(R.id.cardViewTodo);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            linearLayout = itemView.findViewById(R.id.layoutCutomize);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
        }
    }

    private void setItemAppearance(ViewHolder holder, int status) {
        if (status == 0) {
            setCompletedAppearance(holder);
        } else {
            setIncompleteAppearance(holder);
        }
    }

    private void setCompletedAppearance(ViewHolder holder) {
        holder.checkBox.setChecked(true);
        holder.txtNameTodo.setAlpha(0.5f);
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.grayCon));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.grayCon));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.gray, null));
    }

    private void setIncompleteAppearance(ViewHolder holder) {
        holder.checkBox.setChecked(false);
        holder.txtNameTodo.setAlpha(1f);
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }

}
