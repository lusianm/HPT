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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlistadd);

        Button ok = (Button) findViewById(R.id.Ok);
        Button cancel =(Button) findViewById(R.id.Cancle);
        final EditText playlistname = (EditText) findViewById(R.id.addplaylist_name);

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
                            try {
                                Handler handler = new Handler(Looper.getMainLooper());
                                URL url = new URL("http://39.118.94.200:8000/AddList/a/" + playlistname.getText());
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                Log.d("확인", "http://39.118.94.200:8000/AddList/a/" + playlistname.getText());
                                if (conn != null) {
                                    StringBuilder output = new StringBuilder();
                                    Log.d("확인", "test");
                                    conn.setConnectTimeout(10000);
                                    conn.setRequestMethod("GET");
                                    conn.setDoInput(true);
                                    conn.getResponseCode();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String line = null;
                                    while (true) {
                                        line = reader.readLine();
                                        if (line == null) {
                                            break;
                                        }
                                        //output.append(line + "\n");
                                        output.append(line);
                                    }
                                    if(output.toString().equals("!"))
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "이미 추가된 재생목록입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    else
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                }
                            } catch (Exception ex) {
                                Log.d("확인", ex.toString());
                                //Toast.makeText(v.getContext(), "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    Intent result = new Intent();
                    result.putExtra("인증 결과", "success");
                    setResult(TrainingAddActivity.RESULT_OK, result);
                    finish();
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

}
