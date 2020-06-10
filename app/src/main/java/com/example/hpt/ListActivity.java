package com.example.hpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    ArrayList<PlaylistData> data;
    ArrayList<PlaylistData> play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        this.InitializeData();

        Button logout = (Button) findViewById(R.id.logout);
        Button playlist = (Button) findViewById(R.id.playlist);
        Intent getintent = getIntent();
        play = getintent.getParcelableArrayListExtra("playlist");
        final ListView PlaylistView = (ListView)findViewById(R.id.listview);
        final ListViewBtnAdapter PlaylistAdapter = new ListViewBtnAdapter(this, play);
        PlaylistView.setAdapter(PlaylistAdapter);

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistAdapter.getContext().getApplicationContext(), PlaylistActivity.class);
                intent.putParcelableArrayListExtra("playlist", play);
                //startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        ListView listView = (ListView)findViewById(R.id.listview);
        final ListViewBtnAdapter myAdapter = new ListViewBtnAdapter(this,data);
        listView.setAdapter(myAdapter);


    }

    public void InitializeData()        {
        data = new ArrayList<PlaylistData>();
        data.add(new PlaylistData(R.drawable.bono, "가슴","데드리프트"));
        data.add(new PlaylistData(R.drawable.bono, "팔","팔굽혀펴기"));
        data.add(new PlaylistData(R.drawable.bono, "다리","스쿼트"));
    }
}