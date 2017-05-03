package com.hch.qewqs.project_army;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qewqs on 2017-04-08.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class QA extends Fragment {
    View v;

    public static final String ANONYMOUS = "anonymous";
    String username="";
    private Firebase_info finfo;

    Button qa_send_btn;
    Button qa_cancel_btn;
    EditText qa_title;
    EditText qa_contents;

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD_1 = "QA";
    public String MESSAGES_CHILD_2 = "Date";
    DatabaseReference mFirebaseDatabaseReference;


    android.app.FragmentManager manager = getFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.qa_layout, container, false);



        finfo = (Firebase_info) getArguments().get("acc");

        qa_send_btn = (Button)v.findViewById(R.id.qa_send_btn);
        qa_cancel_btn = (Button)v.findViewById(R.id.qa_cancel_btn);
        qa_title = (EditText)v.findViewById(R.id.qa_title);
        qa_contents = (EditText)v.findViewById(R.id.qa_contents);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(1000);
        qa_contents.setFilters(FilterArray);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        qa_send_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                if(finfo.getmUsername().equals(ANONYMOUS)){
                    Toast.makeText(getActivity(), "로그인 후 가능한 서비스입니다.", Toast.LENGTH_LONG).show();
                }else {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    String stNow = sdfnow.format(date);

                    MESSAGES_CHILD_2 = stNow;

                    qa_send_format QAMessage = new qa_send_format(finfo.getmUsername().toString(), stNow,
                            qa_title.getText().toString(), qa_contents.getText().toString());
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD_1).child(MESSAGES_CHILD_2).push().setValue(QAMessage);
                    Toast.makeText(getActivity(), finfo.getmUsername().toString()+ stNow+
                            qa_title.getText().toString()+ qa_contents.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        qa_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_LONG).show();
                qa_title.setText("");
                qa_contents.setText("");
            }
        });


        return v;


    }
}
