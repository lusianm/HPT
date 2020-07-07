package com.example.hpt;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TrainingAddActivity extends AppCompatActivity {
    ArrayList<PlaylistData> playlist = new ArrayList<>();
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler();
    TextView playlistname;
    String Healthname;
    String userID;
    String[] webData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainingadd);

        Button add = (Button)findViewById(R.id.add_playlist);
        Intent getintent = getIntent();
        if(getintent.hasExtra("healthname")) {
            Healthname = (getintent.getStringExtra("healthname"));
        }
        userID = getintent.getStringExtra("ID");
        playlistname = findViewById(R.id.playlistname_traininglistadpater);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaylistAddActivity.class);
                intent.putExtra("healthname", Healthname);
                intent.putExtra("ID",userID);
                finish();
                startActivity(intent);
            }
        });

        this.InitializeData();
    }

    void SetListAdapter(){
        TraininglistAdapter traininglistAdapter = new TraininglistAdapter(this, playlist);
        ListView listView = (ListView)findViewById(R.id.listview_trainingadd);
        listView.setAdapter(traininglistAdapter);
    }

    public void InitializeData(){
        playlist = new ArrayList<PlaylistData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestServer((baseUrl + "UserList/" + userID));
            }
        }).start();
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
        String errorCheck = "!";
        Log.d("확인", "Inputdata = " + Inputdata);
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
                playlist.add(new PlaylistData(linedata));
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    SetListAdapter();
                }
            });
        }
        Thread.currentThread().interrupt();
    }

    class TraininglistAdapter extends BaseAdapter {

        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        ArrayList<PlaylistData> list = new ArrayList<>();

        public TraininglistAdapter(Context context, ArrayList<PlaylistData> data) {
            mContext = context;
            list = data;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public PlaylistData getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            if (list == null)
                return 0;
            else
                return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View converView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.listview_btn_traininglist, null);

            final TextView name = (TextView)view.findViewById(R.id.playlistname_traininglistadpater);

            name.setText(list.get(position).getPlaylistname());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Handler handler = new Handler(Looper.getMainLooper());
                                URL url = new URL(baseUrl + "AddListExer/" + userID + "/" + name.getText() + "/" + Healthname);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                                        output.append(line);
                                    }
                                    Log.d("확인", "Return Message : " + output);
                                    if(output.toString().equals("!"))
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록에 이미 존재하는 운동입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    else
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    Thread.currentThread().interrupt();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(v.getContext(), "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                    }).start();
                }
            });
            return view;
        }

    }

}
