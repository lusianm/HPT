package com.example.hpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, LoginActivity.class));

        Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra("ID", "a");
        startActivity(intent);
    }
}
