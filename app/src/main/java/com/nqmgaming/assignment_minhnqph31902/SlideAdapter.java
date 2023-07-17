package com.nqmgaming.assignment_minhnqph31902;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nqmgaming.assignment_minhnqph31902.R;

public class SlideAdapter extends PagerAdapter {
    Context context;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.welcomeone,
            R.drawable.welcometwo,
            R.drawable.welcomethree
    };

    //Create 3 string short title for todo app
    public String[] slide_headings = {
            "Welcome to Todo App",
            "Add new task",
            "Delete task"
    };

    //Create 3 string short des for todo app
    public String[] slide_descs = {
            "This is a simple todo app",
            "You can add new task by click on the add button",
            "You can delete task by click on the delete button"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.imgSlide);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        slideImageView.setImageResource(slide_images[position]);
        txtTitle.setText(slide_headings[position]);
        txtDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
