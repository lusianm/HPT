package com.example.hpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    ArrayList<TrainingData> data;
    //String baseUrl = "http://118.47.27.223:8000/";
    String baseUrl = "http://39.118.94.200:8000/";
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
                intent.putExtra("ID", userID);
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
        ListView listView = (ListView)findViewById(R.id.listview_list);
        final ListViewBtnAdapter myAdapter = new ListViewBtnAdapter(this,data);
        listView.setAdapter(myAdapter);
    }


    public void InitializeData()        {
        data = new ArrayList<TrainingData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                request((baseUrl + "List/" + userID));
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

    class ListViewBtnAdapter extends BaseAdapter {
        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        ArrayList<TrainingData> sample = new ArrayList<>();
        public ListViewBtnAdapter(Context context, ArrayList<TrainingData> data) {
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
                    Intent intent = new Intent(v.getContext(), TrainingAddActivity.class);
                    intent.putExtra("healthname", healthname.getText());
                    intent.putExtra("ID",userID);
                    mContext.startActivity(intent);
                }
            });

            return view;
        }

    }
}