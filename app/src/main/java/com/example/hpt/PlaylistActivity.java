package com.example.hpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    ArrayList<PlaylistData> play = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Button logout = (Button) findViewById(R.id.logout);
        Button back = (Button) findViewById(R.id.back);
        Intent intent = getIntent();

        play = intent.getParcelableArrayListExtra("playlist");

        ListView listView = (ListView)findViewById(R.id.listview);
        final PlaylistAdapter myAdapter = new PlaylistAdapter(this, play);
        listView.setAdapter(myAdapter);

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
    }
}
