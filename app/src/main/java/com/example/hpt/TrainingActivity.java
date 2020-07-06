package com.example.hpt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import kr.co.prnd.YouTubePlayerView;

public class TrainingActivity extends AppCompatActivity {
    TextView part;
    TextView healthnameView;
    String baseUrl = "http://39.118.94.200:8000/";
    String userID;
    String healthName;
    String VIDEO_ID = "ByOn66R9-pE";
    Handler handler = new Handler();
    String[] webData;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);
        Intent intent = getIntent();
        //이전 Activity로 부터 데이터를 가져옴.
        userID = intent.getStringExtra("ID");
        healthName = intent.getStringExtra("healthname");

        healthnameView=(TextView) findViewById(R.id.healthname);
        healthnameView.setText(intent.getStringExtra("healthname"));
        TextView healthExplain = findViewById(R.id.explain);
        healthExplain.setText(userID);

        //서버 요청을 위한 Thread 시작
        new Thread(new Runnable() {
            @Override
            public void run() {
                //서버에 URL을 보내어 요청 시작
                requestServer((baseUrl + "Exercise/" + healthName + "/" + userID));
            }
        }).start();

        youTubePlayerView = findViewById(R.id.you_tube_player_view);


//        if(healthname.getText().equals("크런치")) {
//            healthExplain.setText("복직근 중 상부를 강화하는 운동이다.");
//        }
//        else if(healthname.getText().equals("니 푸쉬업")){
//            healthExplain.setText("무릎을 바닥에 대고 엎드려뻗친 상태에서 짚은 팔을 굽혔다 폈다 한다.");
//        }
//        else if(healthname.getText().equals("런지")){
//            healthExplain.setText("한쪽 발을 뒤쪽으로 뻗은 상태에서, 다른 쪽 발을 앞으로 내밀고 무릎을 굽혀 몸을 앞쪽으로 움직인다.");
//        }
//        else if(healthname.getText().equals("버피테스트")){
//            healthExplain.setText("짧은 시간 안에 운동 효과를 극대화할 수 있는 유산소성 근력 운동이다.");
//        }
//        else if(healthname.getText().equals("티 푸시업")){
//            healthExplain.setText("양손으로 육각 덤벨을 잡고 엎드려뻗친 상태에서, 짚은 팔을 굽혔다 펴면서 몸통을 돌려 한쪽 덤벨을 위로 올린다.");
//        }
//        else if(healthname.getText().equals("인클라인 푸시업")){
//            healthExplain.setText("손바닥을 벤치나 상자에 올려 엎드려뻗친 상태에서 짚은 팔을 굽혔다 폈다 한다.");
//        }
//        else if(healthname.getText().equals("디클라인 푸시업")){
//            healthExplain.setText("발을 벤치나 상자에 올리고 엎드려뻗친 상태에서 짚은 팔을 굽혔다 폈다 한다.");
//        }
//        else if(healthname.getText().equals("와이드 푸시업")){
//            healthExplain.setText("손을 어깨너비의 두 배로 벌려 엎드려뻗친 상태에서 짚은 팔을 굽혔다 폈다 한다.");
//        }
//        else if(healthname.getText().equals("제자리달리기")){
//            healthExplain.setText("제자리에서 구보 동작을 취한다.");
//        }
//        else if(healthname.getText().equals("내로우스쿼트")){
//            healthExplain.setText("양 다리를 좁게 하고 스쿼트 자세를 취한다.");
//        }
//        else if(healthname.getText().equals("와이드 스쿼트")){
//            healthExplain.setText("양 다리를 넓게 하고 스쿼트 자세를 취한다.");
//        }
//        else if(healthname.getText().equals("점프 스쿼트")){
//            healthExplain.setText("스쿼트 자세에서 뒤꿈치에 힘을주어 점프했다가 다시 스쿼트자세로 돌아간다.");
//        }
//        else if(healthname.getText().equals("팔 벌려 뛰기")){
//            healthExplain.setText("차렷 자세에서 두 팔을 양옆으로 올리면서 두 발을 점프하며 벌린다. ");
//        }
//        else if(healthname.getText().equals("플랭크")){
//            healthExplain.setText("코어 운동 가운데 가장 기본적인 운동. 엎드린 상태에서 몸을 어깨부터 발목까지 일직선이 되게 한다");
//        }
//        else if(healthname.getText().equals("레그 레이즈")){
//            healthExplain.setText("하복부를 단련하는 대표적인 운동이다. 다리를 들어올리는 근육의 힘을 이용하여 동작하는 운동이다.");
//        }
//        else if(healthname.getText().equals("싯 업")){
//            healthExplain.setText("가장 일반적으로 할 수 있는 복근 운동이다. 앉았다 누웠다를 반복하는 동작이므로 복근과 다리를 들어올리는 근육을 발달시킬 수 있다.");
//        }
//        else{
//            healthExplain.setText("존재하지 않습니다.");
//        }
    }

    //입력받은 URL을 이용하여 서버에 데이터 요청
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

    //Input 받은 Data를 파싱하여 뷰에 정보 제공
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), Inputdata, Toast.LENGTH_SHORT).show();
                    TextView healthExplain = findViewById(R.id.explain);
                    healthExplain.setText(Inputdata);
                }
            });
            webData = Inputdata.split("\\|");

            youTubePlayerView.play(webData[3], null);

        }
    }

    public void onButtonClicked(View view){
        part=(TextView) findViewById(R.id.part);
        healthnameView=(TextView) findViewById(R.id.healthname);
        TextView healthExplain = findViewById(R.id.explain);

        if(healthnameView.getText().equals("인클라인 푸시업")) {
            healthExplain.setText("손바닥을 벤치나 상자에 올려 엎드려뻗친 상태에서 짚은 팔을 굽혔다 폈다 한다.");
            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"));
            startActivity(intent2);
        }
        else if(healthnameView.getText().equals("런지")) {
            healthExplain.setText("한쪽 발을 뒤쪽으로 뻗은 상태에서, 다른 쪽 발을 앞으로 내밀고 무릎을 굽혀 몸을 앞쪽으로 움직인다.");
            Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
            startActivity(intent3);
        }
        else{}
    }
}

