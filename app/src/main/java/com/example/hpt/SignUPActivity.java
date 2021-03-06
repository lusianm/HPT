package com.example.hpt;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SignUPActivity  extends AppCompatActivity {
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler();
    TextView iDtxt;
    TextView pWtxt;
    TextView nametxt;
    TextView jobtxt;
    TextView heighttxt;
    TextView weighttxt;
    Spinner ageSelect;
    Spinner genderSelect;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        iDtxt = findViewById(R.id.IDtxt);
        pWtxt = findViewById(R.id.Passwordtxt);
        nametxt = findViewById(R.id.Nametxt);
        jobtxt = findViewById(R.id.Jobtxt);
        heighttxt = findViewById(R.id.Heighttxt);
        weighttxt = findViewById(R.id.Weighttxt);
        ageSelect = findViewById(R.id.AgeSelect);
        genderSelect = findViewById(R.id.GenderSelect);

        ArrayList<String> ageList = new ArrayList<>(), genderList = new ArrayList<>();
        ageList.add("10");
        ageList.add("20");
        ageList.add("30");
        ageList.add("40");
        genderList.add("남성");
        genderList.add("여성");
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ageList),
                genderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genderList);
        ageSelect.setAdapter(ageAdapter);
        genderSelect.setAdapter(genderAdapter);

        Button cancleBtn = findViewById(R.id.CancleBtn);
        Button signupBtn = findViewById(R.id.SignUpBtn);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(iDtxt.getText().length() != 0 && pWtxt.getText().length() != 0 && nametxt.getText().length() != 0 && jobtxt.getText().length() != 0 && heighttxt.getText().length() != 0 && weighttxt.getText().length() != 0)
                            requestServer((baseUrl + "AddUser/" + iDtxt.getText() + "/" + pWtxt.getText()+ "/" + nametxt.getText() + "/" + ageSelect.getSelectedItem().toString()  + "/" + genderSelect.getSelectedItem().toString() + "/"
                                + jobtxt.getText() + "/" + heighttxt.getText() + "/" + weighttxt.getText()));
                        else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "모든 항목을 기입해 주십시오.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.currentThread().interrupt();
                        }
                    }
                }).start();
            }
        });

    }

    public void requestServer(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                int resCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            DataProcessing("\n"+"!\n");
        }
        DataProcessing(output.toString());
    }

    public void DataProcessing(final String data) {
        String errorCheck = "\n" + "!\n";
        if(data.equals(errorCheck)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        Thread.currentThread().interrupt();
    }

}
