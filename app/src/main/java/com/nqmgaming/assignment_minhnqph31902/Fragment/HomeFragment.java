package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.adapter.NotTodoAdapter;
import com.nqmgaming.assignment_minhnqph31902.adapter.TodoAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TodoDAO todoDAO;
    ArrayList<TodoDTO> todoDTOArrayList;
    ArrayList<TodoDTO> doneItemsList = new ArrayList<>();
    ArrayList<TodoDTO> notDoneItemsList = new ArrayList<>();
    TodoAdapter todoAdapter;
    NotTodoAdapter notTodoAdapter;
    UserPreferences userPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabAddTodo = view.findViewById(R.id.fabAddTodo);
        RecyclerView todoRecyclerView = view.findViewById(R.id.rcTodo);
        RecyclerView notTodoRecyclerView = view.findViewById(R.id.rcNotTodo);


        // Lấy danh sách TodoDTO từ cơ sở dữ liệu
        todoDAO = new TodoDAO(getContext());
        todoDTOArrayList = todoDAO.getAllTodo();
        //Create 10 data
        if (todoDTOArrayList.size() == 0) {
            todoDAO.insertTodo(new TodoDTO(1, "Todo 1", "Content 1", 0, "2021-09-01", "2021-09-01", 1));
            todoDAO.insertTodo(new TodoDTO(2, "Todo 2", "Content 2", 0, "2021-09-01", "2021-09-01", 1));
            todoDAO.insertTodo(new TodoDTO(3, "Todo 3", "Content 3", 0, "2021-09-01", "2021-09-01", 1));
            todoDAO.insertTodo(new TodoDTO(4, "Todo 4", "Content 4", 0, "2021-09-01", "2021-09-01", 1));
            Toast.makeText(getContext(), "Create 4 data", Toast.LENGTH_SHORT).show();
        }
        // Tạo các danh sách doneItemsList và notDoneItemsList
        for (TodoDTO todo : todoDTOArrayList) {
            if (todo.getStatus() == 1) {
                doneItemsList.add(todo);
            } else {
                notDoneItemsList.add(todo);
            }
        }


        // Set item cho notTodoRecyclerView
        notTodoAdapter = new NotTodoAdapter(getContext(), notDoneItemsList, doneItemsList);
        notTodoRecyclerView.setAdapter(notTodoAdapter);
        notTodoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set item cho todoRecyclerView
        todoAdapter = new TodoAdapter(getContext(), doneItemsList, notDoneItemsList);
        todoRecyclerView.setAdapter(todoAdapter);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Build a custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.add_todo, null);

                EditText edtTitle = view.findViewById(R.id.etTodo);
                EditText edtContent = view.findViewById(R.id.etDesc);
                ImageButton imgSendAdd = view.findViewById(R.id.imgSendAdd);

                imgSendAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String title = edtTitle.getText().toString();
                        String content = edtContent.getText().toString();
                        userPreferences = new UserPreferences(getContext());
                        int idUser = userPreferences.getIdUser();
                        //random id bằng cách lấy id người dùng + 2004 + id lần cuối cùng
                        int id = idUser + 2004 + todoDTOArrayList.get(todoDTOArrayList.size() - 1).getId();
                        if (title.isEmpty() || content.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            TodoDTO todoDTO = new TodoDTO();
                            todoDTO.setId(id);
                            todoDTO.setName(title);
                            todoDTO.setContent(content);
                            todoDTO.setStatus(1);
                            todoDTO.setStartDate("2021-09-01");
                            todoDTO.setEndDate("2021-09-01");
                            todoDTO.setUserId(userPreferences.getIdUser());
                            long status = todoDAO.insertTodo(todoDTO);
                            if (status > 0) {
                                Toast.makeText(getContext(), "Add todo success", Toast.LENGTH_SHORT).show();
                                todoDTOArrayList = todoDAO.getAllTodo();
                                notDoneItemsList.clear();
                                doneItemsList.clear();
                                for (TodoDTO todo : todoDTOArrayList) {
                                    if (todo.getStatus() == 1) {
                                        doneItemsList.add(todo);
                                    } else {
                                        notDoneItemsList.add(todo);
                                    }
                                }
                                notTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                // Cập nhật adapter và hiển thị animation Lottie nếu danh sách rỗng
                                notTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();

                                // Dismiss dialog
                                builder.setView(view);
                                builder.create().dismiss();

                            } else {
                                Toast.makeText(getContext(), "Add todo fail", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }
                });
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }



}
