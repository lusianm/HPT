package com.example.hpt;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListViewBtnAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PlaylistData> sample = new ArrayList<>();
    ArrayList<PlaylistData> play = new ArrayList<>();

    public ListViewBtnAdapter(Context context, ArrayList<PlaylistData> data) {
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
    public PlaylistData getItem(int position) {
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
                v.getContext().startActivity(intent);
            }
        });

        healthname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TrainingActivity.class);
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

                            URL url = new URL("http://118.47.27.223:8000/AddListExer/a/list2/"+healthname.getText());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            Log.d("확인", "http://118.47.27.223:8000/AddListExer/a/list2/"+healthname.getText());
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
                                            Toast.makeText(v.getContext(), "이미 추가된 운동입니다.", Toast.LENGTH_SHORT).show();
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
                    }
                }).start();
            }
        });
        return view;
    }
}