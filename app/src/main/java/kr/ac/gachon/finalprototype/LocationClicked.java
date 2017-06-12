package kr.ac.gachon.finalprototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kalios on 2017-06-11.
 */

public class LocationClicked extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = null;

    String LocName = null;
    String LocAddress = null;
    // 출발지, 경유지, 도착지 중 어느 버튼을 눌러 넘어가는지 알려주는 문자열.
    String WhichBtn = null;
    LocationItem locationItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_clicked);

        mContext = this;
        final EditText editSearch1 = (EditText) findViewById(R.id.EditSearch1);
        TextView textLocName1 = (TextView) findViewById(R.id.TextLocName1);
        TextView textLocAddress1 = (TextView) findViewById(R.id.TextLocAddress1);
        Button btnStart = (Button) findViewById(R.id.BtnStart);
        Button btnEnd = (Button) findViewById(R.id.BtnEnd);
        Button btnVia = (Button) findViewById(R.id.BtnVia);
        Button btnComplete = (Button) findViewById(R.id.BtnComplete);


        Intent intent = getIntent();

        // 데이터 받아오기
        locationItem = intent.getParcelableExtra("LocationItem");
        LocName = locationItem.getLocName();
        LocAddress = locationItem.getLocAddress();
        // POIItem은 바로 StoreActivity로 들어가 저장될것임.

        // 화면 값 바꿈
        editSearch1.setText(LocName);
        textLocName1.setText(LocName);
        textLocAddress1.setText(LocAddress);


        editSearch1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), SearchActivity.class);
                outIntent.putExtra("SearchValue", editSearch1.getText().toString());
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WhichBtn = new String("Start");
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                intent.putExtra("LocationItem", locationItem);
                // Start 버튼이 눌려서 넘어감을 알려줌. 이 지점은 출발지다.
                intent.putExtra("WhichBtn", WhichBtn);
                Toast.makeText(LocationClicked.this, "출발지 : " + LocName, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnVia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }
}
