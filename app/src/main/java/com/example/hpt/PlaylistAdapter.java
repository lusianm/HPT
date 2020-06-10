package com.example.hpt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PlaylistData> list = new ArrayList<>();

    public PlaylistAdapter(Context context, ArrayList<PlaylistData> data) {
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
        View view = mLayoutInflater.inflate(R.layout.listview_btn_item, null);

        ImageView image = (ImageView)view.findViewById(R.id.image);
        TextView part = (TextView)view.findViewById(R.id.part);
        TextView healthname = (TextView)view.findViewById(R.id.healthname);
        Button button = (Button)view.findViewById(R.id.button);


        image.setImageResource(list.get(position).getImage());
        part.setText(list.get(position).getPart());
        healthname.setText(list.get(position).getHealthname());

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
            public void onClick(View v) {
                list.remove(position);
                Intent intent = new Intent(mContext.getApplicationContext(), PlaylistActivity.class);
                intent.putParcelableArrayListExtra("playlist", list);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "재생목록에서 제거되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
