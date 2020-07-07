package com.example.hpt;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    ArrayList<PlaylistData> playlist = new ArrayList<>();
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler();
    String userID;
    String[] webData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Button back = (Button) findViewById(R.id.back);
        userID = (getIntent().getStringExtra("ID"));
        TextView nameText = findViewById(R.id.User_id);
        nameText.setText(userID);
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
                requestServer((baseUrl + "UserList/" + userID));
            }
        }).start();
    }

    void SetListAdapter(){
        ListView listView = (ListView)findViewById(R.id.listview_playlist);
        final PlaylistAdapter myAdapter = new PlaylistAdapter(this, playlist);
        listView.setAdapter(myAdapter);
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
            DataProcessing("\n"+"!\n");
        }

        DataProcessing(output.toString());
    }

    public void DataProcessing(final String Inputdata) {
        String errorCheck = "\n" + "!\n";
        if(Inputdata.equals(errorCheck)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                    finish();
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
    }

    class PlaylistAdapter extends BaseAdapter {
        Context mContext = null;
        LayoutInflater mLayoutInflater = null;

        public PlaylistAdapter(Context context, ArrayList<PlaylistData> data) {
            mContext = context;
            playlist = data;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public PlaylistData getItem(int position) {
            return playlist.get(position);
        }

        @Override
        public int getCount() {
            if (playlist == null)
                return 0;
            else
                return playlist.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View converView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.listview_playlist, null);

            final TextView name = (TextView)view.findViewById(R.id.playlistname_playlistadpater);
            Button remove = (Button)view.findViewById(R.id.remove_playlistadapter);

            name.setText(playlist.get(position).getPlaylistname());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent intent = new Intent(v.getContext(), PlayActivity.class);
                    intent.putExtra("playlistname", name.getText());
                    intent.putExtra("ID", userID);
                    mContext.startActivity(intent);
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Handler handler = new Handler(Looper.getMainLooper());
                                URL url = new URL(baseUrl + "DeleteList/" + userID + "/" + name.getText());
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
                                                Toast.makeText(v.getContext(), "재생목록이 존재하지않습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    else
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = getIntent();
                                                intent.putExtra("ID", userID);
                                                finish();
                                                startActivity(intent);
                                            }
                                        });
                                }
                            } catch (Exception ex) {
                                Toast.makeText(v.getContext(), "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                }
            });
            return view;
        }

    }
}

