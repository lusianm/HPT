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

    //List View에 Adapter 할당
    public void SetListAdapter(){
        ListView listView = (ListView)findViewById(R.id.listview_list);
        final ListViewBtnAdapter myAdapter = new ListViewBtnAdapter(this,data);
        listView.setAdapter(myAdapter);
    }


    //Data 저장을 위한 ArrayList 생성 후, 서버로 요청 시작
    public void InitializeData()        {
        data = new ArrayList<TrainingData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestServer((baseUrl + "List/" + userID));
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

                    if(ListData[1].equals("니 푸쉬업")) {
                        data.add(new TrainingData(R.drawable.pt1, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("인클라인 푸시업")) {
                        data.add(new TrainingData(R.drawable.pt2, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("디클라인 푸시업")) {
                        data.add(new TrainingData(R.drawable.pt3, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("와이드 푸시업")) {
                        data.add(new TrainingData(R.drawable.pt4, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("티 푸시업")) {
                        data.add(new TrainingData(R.drawable.pt5, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("런지")) {
                        data.add(new TrainingData(R.drawable.pt6, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("제자리달리기")) {
                        data.add(new TrainingData(R.drawable.pt7, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("내로우스쿼트")) {
                        data.add(new TrainingData(R.drawable.pt8, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("와이드 스쿼트")) {
                        data.add(new TrainingData(R.drawable.pt9, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("점프 스쿼트")) {
                        data.add(new TrainingData(R.drawable.pt10, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("팔 벌려 뛰기")) {
                        data.add(new TrainingData(R.drawable.pt11, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("버피테스트")) {
                        data.add(new TrainingData(R.drawable.pt12, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("플랭크")) {
                        data.add(new TrainingData(R.drawable.pt13, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("크런치")) {
                        data.add(new TrainingData(R.drawable.pt14, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("레그 레이즈")) {
                        data.add(new TrainingData(R.drawable.pt15, ListData[0],ListData[1]));
                    }
                    else if(ListData[1].equals("싯 업")) {
                        data.add(new TrainingData(R.drawable.pt16, ListData[0],ListData[1]));
                    }
                    else{}
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