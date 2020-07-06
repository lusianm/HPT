package com.example.hpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hpt.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String[] webData;
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler();
    EditText LoginText;
    EditText PasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginText = findViewById(R.id.LoginTxt);
        PasswordText = findViewById(R.id.PasswordTxt);
        Button LoginBt = findViewById(R.id.LoginBt);
        Button SignUpBt = findViewById(R.id.SignUpBt);
        Handler handler = new Handler();


        LoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginText.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "ID를 입력해 주십시오", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(PasswordText.getText().length() <= 0){
                    Toast.makeText(getApplicationContext(), "Password를 입력해 주십시오", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestServer((baseUrl + "Login/"+LoginText.getText()));
                    }
                }).start();
            }
        });

        SignUpBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SignUPActivity.class));
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
                    if (line == null) {break;}
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
                    Toast.makeText(getApplicationContext(),"존재하지 않는 ID 입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            webData = data.split("\\|");
            if (webData[1].equals(PasswordText.getText().toString())) {
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("ID", webData[0]);
                startActivity(intent);
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
