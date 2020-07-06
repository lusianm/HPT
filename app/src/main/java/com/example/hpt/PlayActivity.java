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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    ArrayList<TrainingData> data;
    String baseUrl = "http://39.118.94.200:8000/";
    Handler handler = new Handler();
    String userID;
    String playlistname;
    String[] webData;
    String[] ListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent getintent = getIntent();
        if(getintent.hasExtra("playlistname")) {
            playlistname = (getintent.getStringExtra("playlistname"));
        }
        userID = (getintent.getStringExtra("ID"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traininglist);
        this.InitializeData();
    }

    public void SetListAdapter(){
        ListView listView = (ListView)findViewById(R.id.listview_traininglist);
        final PlayAdapter myAdapter = new PlayAdapter(this,data);
        listView.setAdapter(myAdapter);
    }


    public void InitializeData()        {
        data = new ArrayList<TrainingData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestServer((baseUrl + "UserListElement/" + userID + "/" + playlistname));
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
                data.add(new TrainingData(R.drawable.bono, ListData[0],ListData[1]));
            }
            SetListAdapter();
        }
    }

    class PlayAdapter extends BaseAdapter {
        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        ArrayList<TrainingData> sample = new ArrayList<>();
        public PlayAdapter(Context context, ArrayList<TrainingData> data) {
            mContext = context;
            sample = data;
            mLayoutInflater = LayoutInflater.from(mContext);

        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public int getCount() {
            if (sample == null)
                return 0;
            else
                return sample.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public TrainingData getItem(int position) {
            return sample.get(position);
        }

        @Override
        public View getView(final int position, View converView, ViewGroup parent) {
            final View view = mLayoutInflater.inflate(R.layout.listview_btn_item, null);

            ImageView image = (ImageView)view.findViewById(R.id.image);
            final TextView part = (TextView)view.findViewById(R.id.part);
            final TextView healthname = (TextView)view.findViewById(R.id.healthname);
            Button button = (Button)view.findViewById(R.id.button);


            image.setImageResource(sample.get(position).getImage());
            part.setText(sample.get(position).getPart());
            healthname.setText(sample.get(position).getHealthname());

            part.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TrainingActivity.class);
                    intent.putExtra("healthname", healthname.getText());
                    intent.putExtra("ID",userID);
                    v.getContext().startActivity(intent);
                }
            });

            healthname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TrainingActivity.class);
                    intent.putExtra("healthname", healthname.getText());
                    intent.putExtra("ID",userID);
                    v.getContext().startActivity(intent);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Handler handler = new Handler(Looper.getMainLooper());
                                URL url = new URL(baseUrl + "DeleteListExer/a/" + playlistname + "/" + healthname.getText());
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                if (conn != null) {
                                    StringBuilder output = new StringBuilder();
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
