package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;

import com.nqmgaming.assignment_minhnqph31902.UI.Account.SettingActivity;
import com.nqmgaming.assignment_minhnqph31902.UI.Intro.GetStartActivity;


import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;


public class UserFragment extends Fragment {
    int id;
    UserPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        if (args != null) {
            id = args.getInt("id");
        }
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgUser = view.findViewById(R.id.imgUser);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvDone = view.findViewById(R.id.tvDone);
        TextView tvNotDone = view.findViewById(R.id.tvNotDone);
        TextView tvSignOut = view.findViewById(R.id.tvSignout);
        ImageButton btnSetting = view.findViewById(R.id.btnSetting);

        Glide.with(this)
                .load(R.drawable.minh)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .transform(new CircleCrop())
                .into(imgUser);
        UserDAO userDAO = new UserDAO(getContext());
        UserDTO userDTO = userDAO.getUserById(id);

        if (userDTO != null) {
            tvName.setText(userDTO.getFirstname() + " " + userDTO.getLastname());
        } else {
            // Handle the case when userDTO is null
            tvName.setText("Unknown User");
        }
        if (userDTO != null) {
            tvEmail.setText(userDTO.getEmail());
        } else {
            // Handle the case when userDTO is null
            tvEmail.setText("Unknown Email");
        }
        TodoDAO todoDAO = new TodoDAO(getContext());
        ArrayList<TodoDTO> userDTOArrayList = todoDAO.getAllTodoByUserId(id);
        ArrayList<TodoDTO> doneItemsList = new ArrayList<>();
        ArrayList<TodoDTO> notDoneItemsList = new ArrayList<>();
        for (TodoDTO todoDTO : userDTOArrayList) {
            if (todoDTO.getStatus() == 1) {
                doneItemsList.add(todoDTO);
            } else {
                notDoneItemsList.add(todoDTO);
            }
        }
        if (doneItemsList.size() == 0) {
            tvDone.setText("Done: 0");
        } else {
            tvDone.setText("Done: " + doneItemsList.size() + "");
        }

        if (notDoneItemsList.size() == 0) {
            tvNotDone.setText("Not Done: 0");
        } else {
            tvNotDone.setText("Not Done: " + notDoneItemsList.size() + "");
        }
        tvSignOut.setOnClickListener(v -> new CuteDialog.withAnimation(getContext())
                .setTitle("Sign out")
                .setAnimation(R.raw.logout)
                .hideCloseIcon(true)
                .setDescription("Are you sure you want to sign out?")
                .setNegativeButtonText("No", v1 -> {

                }).setPositiveButtonText("Yes", v12 -> {
                    preferences = new UserPreferences(requireContext());
                    preferences.logout();
                    Intent intent = new Intent(getContext(), GetStartActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .show());

        btnSetting.setOnClickListener(v -> {
            //Intent to Setting Activity
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });
    }
}