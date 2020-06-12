package com.example.hpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    ArrayList<PlaylistData> data;
    String baseUrl = "http://118.47.27.223:8000/";
    Handler handler = new Handler();
    String userID;
    TextView testView;
    String[] webData;
    String[] ListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Button logout = (Button) findViewById(R.id.logout);
        Button playlist = (Button) findViewById(R.id.playlist);
        Intent getintent = getIntent();
        TextView userIDText = findViewById(R.id.User_id);
        if(getintent.hasExtra("ID")) {
            userID = (getintent.getStringExtra("ID"));
            userIDText.setText(userID);
        }
        testView = findViewById(R.id.testText);

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, PlaylistActivity.class);
                intent.putExtra("ID", "a");
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        this.InitializeData();
    }

    public void SetListAdapter(){
        ListView listView = (ListView)findViewById(R.id.listview);
        final ListViewBtnAdapter myAdapter = new ListViewBtnAdapter(this,data);
        listView.setAdapter(myAdapter);
    }


    public void InitializeData()        {
        data = new ArrayList<PlaylistData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                request((baseUrl + "List/"+userID));
            }
        }).start();
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
            println("!");
        }

        println(output.toString());
    }

    public void println(final String Inputdata) {

        String errorCheck = "\n" + "!\n";
        if(Inputdata.equals("!")) {
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
                ListData = linedata.split("\\|");
                data.add(new PlaylistData(R.drawable.bono, ListData[0],ListData[1]));
            }
            SetListAdapter();
        }
    }
}