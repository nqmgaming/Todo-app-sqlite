package com.nqmgaming.assignment_minhnqph31902.Adapter;

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

    //create context
    Context context;

    //create constructor
    public SlideAdapter(Context context) {
        this.context = context;
    }

    //Create 3 int images for todo app
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

    //get count of slide_headings
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

        //create layout inflater
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        //mapping variables with view
        ImageView slideImageView = view.findViewById(R.id.imgSlide);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        //set image, title, description for each slide
        slideImageView.setImageResource(slide_images[position]);
        txtTitle.setText(slide_headings[position]);
        txtDescription.setText(slide_descs[position]);

        //add view to container
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //remove view from container
        container.removeView((LinearLayout) object);

    }
}
