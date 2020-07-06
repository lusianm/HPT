package com.example.hpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaylistAddActivity extends AppCompatActivity {
    String healthname;
    String userID;
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler(Looper.getMainLooper());
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlistadd);

        Button ok = (Button) findViewById(R.id.Ok);
        Button cancel =(Button) findViewById(R.id.Cancle);
        final EditText playlistname = (EditText) findViewById(R.id.addplaylist_name);

        Intent getintent = getIntent();
        healthname = (getintent.getStringExtra("healthname"));
        userID = getintent.getStringExtra("ID");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(playlistname.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "재생목록 이름을 입력해 주십시오", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            requestServer(baseUrl + "AddList/" + userID + "/" + playlistname.getText());
                        }
                    }).start();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    output.append(line);
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            DataProcessing("!");
        }
        DataProcessing(output.toString());
    }

    public void DataProcessing(final String Inputdata) {
        if(Inputdata.equals("!")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"이미 추가된 재생목록입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"재생목록이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, TrainingAddActivity.class);
            intent.putExtra("healthname", healthname);
            intent.putExtra("ID",userID);
            finish();
            startActivity(intent);
        }
    }

}
