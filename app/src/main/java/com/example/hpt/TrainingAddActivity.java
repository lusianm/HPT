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
    String baseUrl = "http://118.47.27.223:8000/";
    Handler handler = new Handler();
    TextView playlistname;
    String Healthname;
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

        playlistname = findViewById(R.id.playlistname_traininglistadpater);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaylistAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        this.InitializeData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            finish();
            startActivity(new Intent(this, TrainingAddActivity.class));
        }
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
                request((baseUrl + "UserList/a"));
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
            SetListAdapter();
        }
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
            Button remove = (Button)view.findViewById(R.id.remove_traininglistadpater);

            name.setText(list.get(position).getPlaylistname());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Handler handler = new Handler(Looper.getMainLooper());
                                URL url = new URL("http://118.47.27.223:8000/AddListExer/a/" + name.getText() + "/" + Healthname);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                Log.d("확인", "http://118.47.27.223:8000/AddListExer/a/" + name.getText() + "/" + Healthname);
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
                                }
                            } catch (Exception ex) {
                                Log.d("확인", ex.toString());
                                //Toast.makeText(v.getContext(), "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                    }).start();
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
                                URL url = new URL("http://118.47.27.223:8000/DeleteList/a/" + name.getText());
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                Log.d("확인", "http://118.47.27.223:8000/DeleteList/a/" + name.getText());
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
                                    Log.d("확인", "Return Message : " + output);
                                    if(output.toString().equals("!"))
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록에 존재하지 않는 운동입니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                                    else
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(v.getContext(), "재생목록에서 제거되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                }
                            } catch (Exception ex) {
                                Log.d("확인", ex.toString());
                                //Toast.makeText(v.getContext(), "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    finish();
                }
            });
            return view;
        }

    }

}