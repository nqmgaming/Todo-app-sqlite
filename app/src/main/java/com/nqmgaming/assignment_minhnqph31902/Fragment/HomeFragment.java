package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.assignment_minhnqph31902.DataBase.DataBaseTodoAppHelper;
import com.nqmgaming.assignment_minhnqph31902.adapter.TodoAdapter;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    TodoDAO todoDAO;
    ArrayList<TodoDTO> todoDTOArrayList;
    TodoAdapter todoAdapter;
    UserPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView todoRecyclerView = view.findViewById(R.id.rcTodo);
        FloatingActionButton fabAddTodo = view.findViewById(R.id.fabAddTodo);

        todoDAO = new TodoDAO(getContext());
        todoDTOArrayList = todoDAO.getAllTodo();
        if (todoDTOArrayList.size() == 0) {
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }else {
            todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            todoRecyclerView.hasFixedSize();

            todoAdapter = new TodoAdapter(getContext(), todoDTOArrayList);
            todoRecyclerView.setAdapter(todoAdapter);
            todoAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Data", Toast.LENGTH_SHORT).show();
        }
        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new TodoDTO


            }
        });

    }
}