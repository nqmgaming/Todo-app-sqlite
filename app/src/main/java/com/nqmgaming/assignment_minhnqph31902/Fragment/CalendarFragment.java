package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.adapter.TodoAdapter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CalendarFragment extends Fragment {

    private TextView selectedDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        selectedDateTextView = rootView.findViewById(R.id.selectedDateTextView);
        DatePicker datePicker = rootView.findViewById(R.id.datePicker);
        // Set the initial date to the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the date on the TextView when the DatePicker is initialized
        selectedDateTextView.setText(getFormattedDate(year, month, dayOfMonth));

        // Set an OnDateChangedListener to handle date selection
        datePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Display the selected date in the TextView
                selectedDateTextView.setText(getFormattedDate(year, monthOfYear, dayOfMonth));

                // Show an alert with the selected date
                showDateAlert(year, monthOfYear, dayOfMonth);
            }
        });

        return rootView;
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        // Format the date in the desired format (e.g., dd/MM/yyyy)
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(calendar.getTime());
    }

    private void showDateAlert(int year, int month, int dayOfMonth) {
        // Create and show the DatePickerDialog with the selected date
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_todo, null);
        EditText edtTitle = dialogView.findViewById(R.id.etTodo);
        EditText edtContent = dialogView.findViewById(R.id.etDesc);
        TextView tvDate = dialogView.findViewById(R.id.txtDate);
        ImageButton imgSendAdd = dialogView.findViewById(R.id.imgSendAdd);
        ImageButton imgCalendar = dialogView.findViewById(R.id.calendarAdd);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeView(dialogView);
        }
        final AlertDialog alertDialog = builder.create();
        tvDate.setText(getFormattedDate(year, month, dayOfMonth));
        alertDialog.setView(dialogView);

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

        //get id from shared preferences
        imgSendAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPreferences userPreferences = new UserPreferences(getContext());
                int id = userPreferences.getIdUser();
                //random id todo bằng id user + 2004 + số ngẫu nhiên
                int idTodo = id + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) % 1000000;
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();

                //validate
                if (TextUtils.isEmpty(title)) {
                    edtTitle.setError("Please enter title");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    edtContent.setError("Please enter content");
                    return;
                }

                TodoDTO todoDTO = new TodoDTO();
                todoDTO.setUserId(id);
                todoDTO.setName(title);
                todoDTO.setContent(content);
                todoDTO.setEndDate(tvDate.getText().toString());
                todoDTO.setStartDate(tvDate.getText().toString());
                todoDTO.setId(idTodo);
                todoDTO.setStatus(1);
                TodoDAO todoDAO = new TodoDAO(getContext());
                long result = todoDAO.insertTodo(todoDTO);
                if (result > 0) {
                    Toast.makeText(getContext(), "Add success", Toast.LENGTH_SHORT).show();
                    //Intent to home
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Add failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialog.show();
    }
}
