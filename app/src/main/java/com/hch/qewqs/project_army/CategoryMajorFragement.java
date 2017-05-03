package com.hch.qewqs.project_army;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.hch.qewqs.project_army.MainActivity.specialist;

/**
 * Created by food8 on 2017-05-03.
 */

public class CategoryMajorFragement extends Fragment {
    ViewGroup v;
    Button btnSearch;
    String base_URL;
    TextView resultText;
    String result;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    String jaguk1;
    String jaguk2;
    String jaguk3;
    String gubun;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_category_one_3, container, false);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        resultText = (TextView) v.findViewById(R.id.resultText);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner3 = (Spinner) v.findViewById(R.id.spinner3);
        spinner4 = (Spinner) v.findViewById(R.id.spinner4);
        localSpinner2(R.array.spinnerArray2);
        localSpinner3(R.array.spinnerArray2);
        localSpinner4(R.array.spinnerArray2);

        spinner2.setOnItemSelectedListener(setSpinner2);
        spinner3.setOnItemSelectedListener(setSpinner3);

        specialist.setLicense(spinner2.getSelectedItem().toString());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이알로그
                final MyDialogFragment frag = MyDialogFragment.newInstance();
                frag.show(getFragmentManager(), "TAG");

                //작업스레드
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            base_URL = "http://apis.data.go.kr/1300000/mjbJiWon/list?serviceKey=6tLZdNCgFtUUkC1aMEPPSDH5EqZB09HbJ9vEwO1DeRGItkpZQzyAxdTw2npenOfhIQdsklstTNt9qrj2RODhkQ%3D%3D&numOfRows=100&pageSize=10&pageNo=1&startPage=1";
                            getXmlData(base_URL);
                            resultText.setText(result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        frag.dismiss();
                    }
                });
                t.start();
            }
        });
        return v;
    }

    public static class MyDialogFragment extends DialogFragment {
        public static MyDialogFragment newInstance() {
            return new MyDialogFragment();
        }

        public Dialog onCreateDialog(Bundle savedInstancestate) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("네트워크");
            dialog.setMessage("요청중...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            return dialog;
        }
    }

    /////////////////////////////////////

    void getXmlData(String url_base) {

        try {
            URL url = new URL(url_base); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        //테그 이름 얻어오기
                        if (tag.equals("gubun")) {
                            xpp.next();
                            gubun = xpp.getText().toString();
                        }
                        else if(tag.equals("gtcdNm2")){
                            xpp.next();
                            if(gubun.equals("전공")){
                                result = xpp.getText();
                            }
                            else if(gubun.equals("자격")){
                                if(xpp.getText().toString().equals(specialist.getLicense())){
                                    result += "자격증 : " + specialist.getLicense() + "\n";
                                }
                            }
                        }
                        else if(tag.equals("gtcdNm1")){
                            xpp.next();
                            result += "군명 : " + xpp.getText() + "\n";
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG: // </> End Tag
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item")){

                        }
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void localSpinner2(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(fAdapter);
    }
    private void localSpinner3(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(fAdapter);
    }
    private void localSpinner4(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(fAdapter);
    }

    public AdapterView.OnItemSelectedListener setSpinner2 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                spinner3.setVisibility(View.INVISIBLE);
            }
            else {
                spinner3.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    public AdapterView.OnItemSelectedListener setSpinner3 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                spinner4.setVisibility(View.INVISIBLE);
            }
            else {
                spinner4.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
