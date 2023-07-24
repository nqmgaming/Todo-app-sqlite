package com.nqmgaming.assignment_minhnqph31902.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Calendar;

import io.github.cutelibs.cutedialog.CuteDialog;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TodoDTO> doneItemsList;
    private final ArrayList<TodoDTO> notDoneItemsList;
    private final ViewBinder viewBinderDone = new ViewBinder();

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
        holder.tvDate.setText(todoDTO.getEndDate());
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
            int result = todoDAO.deleteTodo(todoDTO);
            if (result > 0) {
                new CuteDialog.withIcon(context)
                        .setIcon(R.drawable.done)
                        .setTitle("Delete success")
                        .setPositiveButtonText("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();
                doneItemsList.remove(todoDTO);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, doneItemsList.size());
            } else {
                new CuteDialog.withIcon(context)
                        .setIcon(R.drawable.close)
                        .setTitle("Delete fail")
                        .setPositiveButtonText("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();
            }


        });
        holder.tvEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.edit_todo, null);

            EditText edtTitleEdit = view.findViewById(R.id.etTodoEdit);
            EditText edtDescriptionEdit = view.findViewById(R.id.etDescEdit);
            TextView tvDateEdit = view.findViewById(R.id.tvDateEdit);
            ImageButton btnPickDateEdit = view.findViewById(R.id.btnPickDateEdit);
            ImageButton btnSendEdit = view.findViewById(R.id.imgSendEit);

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
                        new CuteDialog.withIcon(context)
                                .setIcon(R.drawable.done)
                                .setTitle("Update success")
                                .setPositiveButtonText("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .hideNegativeButton(true)
                                .hideCloseIcon(true)
                                .show();
                        notifyItemRangeChanged(position, doneItemsList.size());
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        });

        holder.constraintTodo.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.content, null);
            TextView tvContent = view.findViewById(R.id.content);
            Button btnOK = view.findViewById(R.id.btnOK);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }

            tvContent.setText(todoDTO.getContent());
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            btnOK.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return doneItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView txtNameTodo, tvDate;
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
            tvDate = itemView.findViewById(R.id.tvDateTodo);
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
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.grayCon, null));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.grayCon, null));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.gray, null));
    }

    private void setIncompleteAppearance(ViewHolder holder) {
        holder.checkBox.setChecked(false);
        holder.txtNameTodo.setAlpha(1f);
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }

}
