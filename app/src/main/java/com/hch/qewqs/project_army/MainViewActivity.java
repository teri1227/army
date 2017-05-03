package com.hch.qewqs.project_army;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainViewActivity  extends Fragment {
    View v;

    Button category1;
    Button category2;
    Button category3;
    Button category4;

    MainActivity mCallback;

    android.app.FragmentManager manager = getFragmentManager();

    private Firebase_info finfo;
    Fragment fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_main_view, container, false);

        category1 = (Button) v.findViewById(R.id.category_one);
        category2 = (Button) v.findViewById(R.id.category_two);
        category3 = (Button) v.findViewById(R.id.category_three);
        category4 = (Button) v.findViewById(R.id.category_four);


        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_One();
            }
        });
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_Two();
            }
        });
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_Three();
            }
        });
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_Four();
            }
        });

        return v;


    }

    @Override

    public void onAttach(Activity activity) {

        super.onAttach(activity);


        // This makes sure that the container activity has implemented

        // the callback interface. If not, it throws an exception

        try {

            mCallback = (MainActivity) activity;

        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()

                    + " must implement OnHeadlineSelectedListener");

        }

    }
}
