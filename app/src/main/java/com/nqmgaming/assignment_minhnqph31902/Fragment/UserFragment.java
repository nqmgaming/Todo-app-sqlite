package com.nqmgaming.assignment_minhnqph31902.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;

import com.nqmgaming.assignment_minhnqph31902.UI.Intro.GetStartActivity;

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
        TextView tvSignOut = view.findViewById(R.id.tvSignout);

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
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CuteDialog.withAnimation(getContext())
                        .setTitle("Sign out")
                        .setDescription("Are you sure you want to sign out?")
                        .setNegativeButtonText("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButtonText("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                preferences = new UserPreferences(getContext());
                                preferences.logout();
                                Intent intent = new Intent(getContext(), GetStartActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });

    }
}