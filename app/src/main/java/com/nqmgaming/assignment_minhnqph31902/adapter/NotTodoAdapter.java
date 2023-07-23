package com.nqmgaming.assignment_minhnqph31902.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.Fragment.HomeFragment;
import com.nqmgaming.assignment_minhnqph31902.UI.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class NotTodoAdapter extends RecyclerView.Adapter<NotTodoAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TodoDTO> notDoneItemsList;
    private final ArrayList<TodoDTO> doneItemsList;

    private ViewBinder viewBinderNotDone = new ViewBinder();


    public NotTodoAdapter(Context context, ArrayList<TodoDTO> notDoneItemsList, ArrayList<TodoDTO> doneItemsList) {
        this.context = context;
        this.notDoneItemsList = notDoneItemsList;
        this.doneItemsList = doneItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.not_done_item, parent, false);
        return new NotTodoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoDTO todoDTO = notDoneItemsList.get(position);
        holder.txtNameNotTodo.setText(todoDTO.getName());
        holder.txtDateEdit.setText(todoDTO.getEndDate());
        setItemAppearance(holder, todoDTO.getStatus());

        //set status for checkbox when click
        holder.uncheckBox.setOnClickListener(v -> {
            if (!holder.uncheckBox.isChecked()) {
                todoDTO.setStatus(1);
                doneItemsList.add(todoDTO);
                TodoDAO todoDAO = new TodoDAO(context);

                todoDAO.setStatusTodo(todoDTO);
                notDoneItemsList.remove(position);
                Toast.makeText(context, doneItemsList.size() + "+ " + notDoneItemsList.size() + "", Toast.LENGTH_SHORT).show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notDoneItemsList.size());

                MainActivity mainActivity = (MainActivity) context;
                mainActivity.replaceFragment(new HomeFragment());




            }
        });

        viewBinderNotDone.bind(holder.swipeLayoutNotDone, String.valueOf(todoDTO.getId()));
        holder.tvDeleteNotDone.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(context);
            todoDAO.deleteTodo(todoDTO);
            notDoneItemsList.remove(position);
            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notDoneItemsList.size());
        });
        holder.tvEditNotDone.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.edit_todo, null);
            builder.setView(view);
            EditText edtTitleEdit = view.findViewById(R.id.etTodoEdit);
            EditText edtDescriptionEdit = view.findViewById(R.id.etDescEdit);
            TextView tvDateEdit = view.findViewById(R.id.tvDateEdit);
            ImageButton btnPickDateEdit = view.findViewById(R.id.btnPickDateEdit);
            ImageButton btnSendEdit = view.findViewById(R.id.imgSenđEit);

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();

            edtTitleEdit.setText(todoDTO.getName());
            edtDescriptionEdit.setText(todoDTO.getContent());
            tvDateEdit.setText(todoDTO.getEndDate());

            btnPickDateEdit.setOnClickListener(v1 -> {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view1, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDateEdit.setText(date);
                }, year, month, day);
                datePickerDialog.show();
            });

            btnSendEdit.setOnClickListener(v12 -> {
                String title = edtTitleEdit.getText().toString().trim();
                String description = edtDescriptionEdit.getText().toString().trim();
                String date = tvDateEdit.getText().toString().trim();
                if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    todoDTO.setName(title);
                    todoDTO.setContent(description);
                    todoDTO.setEndDate(date);
                    TodoDAO todoDAO = new TodoDAO(context);
                    int status = todoDAO.updateTodo(todoDTO);
                    if (status > 0) {
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        });

        holder.constraintNotTodo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.content, null);
                TextView tvContent = view.findViewById(R.id.content);
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                tvContent.setText(todoDTO.getContent());
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notDoneItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox uncheckBox;
        TextView txtNameNotTodo, txtDateEdit;
        ConstraintLayout constraintNotTodo;
        CardView cardViewMotTodo;
        ImageButton tvEditNotDone, tvDeleteNotDone;
        SwipeLayout swipeLayoutNotDone;
        LinearLayout linearLayoutNotDone;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uncheckBox = itemView.findViewById(R.id.uncheckbox);
            txtNameNotTodo = itemView.findViewById(R.id.tvNameNotTodo);
            constraintNotTodo = itemView.findViewById(R.id.constraintNotTodo);
            cardViewMotTodo = itemView.findViewById(R.id.cardViewNotTodo);
            tvEditNotDone = itemView.findViewById(R.id.tvEditNotDone);
            tvDeleteNotDone = itemView.findViewById(R.id.tvDeleteNotDone);
            swipeLayoutNotDone = itemView.findViewById(R.id.swipeLayoutNotDone);
            linearLayoutNotDone = itemView.findViewById(R.id.layoutCutomizeNotDone);
            txtDateEdit = itemView.findViewById(R.id.txtDateNotDone);

        }
    }

    private void setItemAppearance(ViewHolder holder, int status) {
        if (status == 1) {
            setIncompleteAppearance(holder);
        } else {
            setCompletedAppearance(holder);
        }
    }

    private void setCompletedAppearance(NotTodoAdapter.ViewHolder holder) {
        holder.uncheckBox.setChecked(true);
        holder.txtNameNotTodo.setAlpha(0.5f);
        holder.constraintNotTodo.setBackgroundColor(context.getResources().getColor(R.color.gray));
        holder.cardViewMotTodo.setCardBackgroundColor(context.getResources().getColor(R.color.gray));
        holder.txtNameNotTodo.setPaintFlags(holder.txtNameNotTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.uncheckBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }

    private void setIncompleteAppearance(NotTodoAdapter.ViewHolder holder) {
        holder.uncheckBox.setChecked(false);
        holder.txtNameNotTodo.setAlpha(1f);
        holder.constraintNotTodo.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.cardViewMotTodo.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        holder.txtNameNotTodo.setPaintFlags(holder.txtNameNotTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.uncheckBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }
}
