package com.hch.qewqs.project_army;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by qewqs on 2017-04-07.
 */

public class CategoryFourFragement extends Fragment {
    View v;

    String mUser;
    public static final String ANONYMOUS = "anonymous";

    String username="";

    private Firebase_info finfo;

    Activity root = getActivity();

    TextView textView1;
    android.app.FragmentManager manager = getFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_four, container, false);

        finfo = (Firebase_info) getArguments().get("acc");
        textView1 = (TextView) v.findViewById(R.id.content1);

        username = finfo.getmUsername();


        textView1.setText(username);


        return v;


    }

    public void onAttach(Context context){
        super.onAttach(context);
    }
}
