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

public class ListViewBtnAdapter_Edit extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PlaylistData> sample = new ArrayList<>();
    ArrayList<PlaylistData> play = new ArrayList<>();

    public ListViewBtnAdapter_Edit(Context context, ArrayList<PlaylistData> data) {
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
            public void onClick(View v) {
                play.add(new PlaylistData(R.drawable.bono, sample.get(position).part, sample.get(position).healthname));
                Intent intent = new Intent(mContext.getApplicationContext(), ListActivity.class);
                intent.putParcelableArrayListExtra("playlist", play);
                mContext.startActivity(intent);

                Toast.makeText(v.getContext(), "재생목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}