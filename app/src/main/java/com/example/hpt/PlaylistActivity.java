package com.example.hpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    ArrayList<PlaylistData> play = new ArrayList<>();
    String baseUrl = "http://118.47.27.223:8000/";
    Handler handler = new Handler();
    String userID;
    String[] webData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Button logout = (Button) findViewById(R.id.logout);
        Button back = (Button) findViewById(R.id.back);
        Intent intent = getIntent();

        TextView userIDText = findViewById(R.id.User_id);
        if(intent.hasExtra("ID")) {
            userID = (intent.getStringExtra("ID"));
            userIDText.setText(userID);
        }

        //play = intent.getParcelableArrayListExtra("playlist");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.InitializeData();
    }

    public void InitializeData()        {
        new Thread(new Runnable() {
            @Override
            public void run() {
                request((baseUrl + "UserList/"+userID));
            }
        }).start();
    }

    void SetListAdapter(){
        ListView listView = (ListView)findViewById(R.id.listview);
        final PlaylistAdapter myAdapter = new PlaylistAdapter(this, play);
        listView.setAdapter(myAdapter);
    }
    public void request(String urlStr) {
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

                    //output.append(line + "\n");
                    output.append(line);
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            println("\n"+"!\n");
        }

        println(output.toString());
    }

    public void println(final String Inputdata) {

        String errorCheck = "\n" + "!\n";
        if(Inputdata.equals(errorCheck)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            webData = Inputdata.split("<br>");
            for (final String linedata : webData){
                play.add(new PlaylistData(R.drawable.bono, linedata,linedata));
            }
            SetListAdapter();
        }
    }
}
