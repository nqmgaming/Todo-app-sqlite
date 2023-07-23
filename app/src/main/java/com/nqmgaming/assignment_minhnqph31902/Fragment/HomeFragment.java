package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.adapter.NotTodoAdapter;
import com.nqmgaming.assignment_minhnqph31902.adapter.TodoAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

import io.github.cutelibs.cutedialog.CuteDialog;

public class HomeFragment extends Fragment {
    TodoDAO todoDAO;
    ArrayList<TodoDTO> todoDTOArrayList;
    ArrayList<TodoDTO> doneItemsList = new ArrayList<>();
    ArrayList<TodoDTO> notDoneItemsList = new ArrayList<>();
    TodoAdapter todoAdapter;
    NotTodoAdapter notTodoAdapter;
    UserPreferences userPreferences;
    FloatingActionButton fabAddTodo;
    FloatingActionButton fabDeleteAll;
    FloatingActionButton fabDoneAll;
    private boolean isSubMenuOpen = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            fabAddTodo = view.findViewById(R.id.fabAddTodo);
            fabDeleteAll = view.findViewById(R.id.fabDeleteAll);
            fabDoneAll = view.findViewById(R.id.doneAll);
            RecyclerView todoRecyclerView = view.findViewById(R.id.rcTodo);
            RecyclerView notTodoRecyclerView = view.findViewById(R.id.rcNotTodo);

            //try catch
            try {

                //Get id user from shared preferences
                userPreferences = new UserPreferences(getContext());
                int idUser = userPreferences.getIdUser();

                todoDAO = new TodoDAO(getContext());
                todoDTOArrayList = todoDAO.getAllTodoByUserId(idUser);

                if (todoDTOArrayList.size() == 0) {
                    // Create 4 data entries
                    for (int i = 0; i < 3; i++) {
                        int id = idUser + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + i;
                        TodoDTO todoDTO = new TodoDTO();
                        todoDTO.setId(id);
                        todoDTO.setName("Todo " + (i + 1));
                        todoDTO.setContent("Content " + (i + 1));
                        if (id % 2 == 0) {
                            todoDTO.setStatus(0);
                        } else {
                            todoDTO.setStatus(1);
                        }
                        todoDTO.setStartDate("01/01/2021");
                        todoDTO.setEndDate("01/01/2021");
                        todoDTO.setUserId(idUser);
                        long status = todoDAO.insertTodo(todoDTO);
                        if (status > 0) {
                            Toast.makeText(getContext(), "Add todo success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Add todo fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    todoDTOArrayList = todoDAO.getAllTodoByUserId(idUser);
                    doneItemsList.clear();
                    notDoneItemsList.clear();
                    for (TodoDTO todo : todoDTOArrayList) {
                        if (todo.getStatus() == 1) {
                            doneItemsList.add(todo);
                        } else {
                            notDoneItemsList.add(todo);
                        }
                    }
                } else {
                    // Tạo các danh sách doneItemsList và notDoneItemsList
                    for (TodoDTO todo : todoDTOArrayList) {
                        if (todo.getStatus() == 1) {
                            doneItemsList.add(todo);
                        } else {
                            notDoneItemsList.add(todo);
                        }
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //try catch
            try {
                // Set item cho notTodoRecyclerView
                notTodoAdapter = new NotTodoAdapter(getContext(), notDoneItemsList, doneItemsList);
                notTodoRecyclerView.setAdapter(notTodoAdapter);
                notTodoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // Set item cho todoRecyclerView
                todoAdapter = new TodoAdapter(getContext(), doneItemsList, notDoneItemsList);
                todoRecyclerView.setAdapter(todoAdapter);
                todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            fabAddTodo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    toggleSubMenu();
                    return true;
                }
            });
            fabAddTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Build a custom dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View view = getLayoutInflater().inflate(R.layout.add_todo, null);

                    EditText edtTitle = view.findViewById(R.id.etTodo);
                    EditText edtContent = view.findViewById(R.id.etDesc);
                    TextView tvDate = view.findViewById(R.id.txtDate);
                    ImageButton imgSendAdd = view.findViewById(R.id.imgSendAdd);
                    ImageButton imgCalendar = view.findViewById(R.id.calendarAdd);

                    ViewGroup parent = (ViewGroup) view.getParent();
                    if (parent != null) {
                        parent.removeView(view);
                    }
                    builder.setView(view);
                    final AlertDialog alertDialog = builder.create();
                    //get date from system
                    String startDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

                    imgCalendar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                // Date picker
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth1) -> {
                                    String endDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                                    tvDate.setText(endDate);
                                    //get date from system
                                }, year, month, dayOfMonth);
                                datePickerDialog.show();

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    imgSendAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String title = edtTitle.getText().toString().trim();
                            String content = edtContent.getText().toString().trim();
                            String endDate = tvDate.getText().toString().trim();
                            userPreferences = new UserPreferences(getContext());
                            int idUser = userPreferences.getIdUser();
                            //random id bằng cách lấy id người dùng + 2004 + id lần cuối cùng
                            int id;
                            if (todoDTOArrayList.isEmpty()) {
                                //random id bằng cách lấy giây hiện tại
                                id = idUser + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                            } else {
                                // Access the last element of todoDTOArrayList
                                id = idUser + 2004 + todoDTOArrayList.get(todoDTOArrayList.size() - 1).getId();
                            }

                            if (title.isEmpty() || content.isEmpty()) {
                                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    TodoDTO todoDTO = new TodoDTO();
                                    todoDTO.setId(id);
                                    todoDTO.setName(title);
                                    todoDTO.setContent(content);
                                    todoDTO.setStatus(1);
                                    todoDTO.setStartDate(startDate);
                                    todoDTO.setEndDate(endDate);
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
                                        alertDialog.dismiss();

                                    } else {
                                        Toast.makeText(getContext(), "Add todo fail", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });
                    alertDialog.show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        fabDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoDAO todoDAO = new TodoDAO(getContext());
                new CuteDialog.withAnimation(getContext())
                        .setTitle("Delete all?")
                        .setPositiveButtonText("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    todoDAO.deleteAllTodo();
                                    Toast.makeText(getContext(), "Delete all success", Toast.LENGTH_SHORT).show();
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
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButtonText("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        fabDoneAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoDAO todoDAO = new TodoDAO(getContext());
                new CuteDialog.withAnimation(getContext())
                        .setTitle("Done all?")
                        .setPositiveButtonText("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    todoDAO.doneAllTodo();
                                    Toast.makeText(getContext(), "Done all success", Toast.LENGTH_SHORT).show();
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
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButtonText("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }

    private void toggleSubMenu() {
        if (isSubMenuOpen) {
            hideSubMenu();
        } else {
            showSubMenu();
        }
    }

    private void showSubMenu() {
        fabDoneAll.setVisibility(View.VISIBLE);
        fabDeleteAll.setVisibility(View.VISIBLE);
        fabAddTodo.setImageDrawable(getResources().getDrawable(R.drawable.close, null));
        isSubMenuOpen = true;
    }

    private void hideSubMenu() {
        fabDoneAll.setVisibility(View.GONE);
        fabDeleteAll.setVisibility(View.GONE);
        fabAddTodo.setImageDrawable(getResources().getDrawable(R.drawable.add, null));
        isSubMenuOpen = false;
    }


}
