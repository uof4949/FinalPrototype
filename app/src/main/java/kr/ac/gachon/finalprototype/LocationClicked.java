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

import java.util.ArrayList;

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

    // StoreActivity로 LocationItem을 넘겨주기 위한 ArrayList들.
    ArrayList<LocationItem> startData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> viaData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> endData = new ArrayList<LocationItem>();

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

        Intent intent = getIntent();

        // 데이터 받아오기
        locationItem = intent.getParcelableExtra("LocationItemToLoc");
        LocName = locationItem.getLocName();
        LocAddress = locationItem.getLocAddress();
        // POIItem은 바로 StoreActivity로 들어가 저장될것임.

        // 초기화.
        startData.clear();
        viaData.clear();
        endData.clear();
        // SearchActivity에서 LocationClicked를 거쳐 StoreActivity로 전달될 startData, viaData, endData 받아옴.
        // SearchActivity에서 넘어올 때 새로 생성되므로 매번 넣어줘야함.
        startData = intent.getParcelableArrayListExtra("StartLocationItemToLoc");
        viaData = intent.getParcelableArrayListExtra("ViaLocationItemToLoc");
        endData = intent.getParcelableArrayListExtra("EndLocationItemToLoc");

        // 화면 값 바꿈
        editSearch1.setText(LocName);
        textLocName1.setText(LocName);
        textLocAddress1.setText(LocAddress);


        editSearch1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), SearchActivity.class);
                // 이 경우 LocResult 키워드로 반환하되 EditText에 있던 문자열을 전송.
                outIntent.putExtra("LocResult", editSearch1.getText().toString());
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WhichBtn = new String("Start");
                // 출발지에 지점을 추가하지 않았을 때만 추가.
                if(startData.size() == 0) {
                    Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                    // 해당 지점 좌표를 담은 locationItem 전송.
                    intent.putExtra("LocationItemToStore", locationItem);
                    // Start 버튼이 눌려서 넘어감을 알려줌. 이 지점은 출발지다.
                    intent.putExtra("WhichBtnToStore", WhichBtn);

                    // LocationClicked를 거쳐 StoreActivity로 넘겨줄 startData, viaData, endData 전송.
                    intent.putExtra("StartLocationItemToStore", startData);
                    intent.putExtra("ViaLocationItemToStore", viaData);
                    intent.putExtra("EndLocationItemToStore", endData);

                    Toast.makeText(LocationClicked.this, "출발지 : " + LocName, Toast.LENGTH_SHORT).show();
                    // 값을 다시 돌려받기 위해 사용. Add가 들어오면 SearchActivity로 locationItem과 WhichBtn을 전송.
                    // 두번째 인자 0은 돌려 받을 값이 있을 경우 0 이상을 쓴다.
                    // Compute가 들어오면 SearchActivity로 그냥 돌아감.
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(LocationClicked.this, "출발지를 이미 선택하셨습니다. 출발지 : " + startData.get(0).getLocName(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnVia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WhichBtn = new String("Via");
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                intent.putExtra("LocationItemToStore", locationItem);
                // Via 버튼이 눌려서 넘어감을 알려줌. 이 지점은 경유지다.
                intent.putExtra("WhichBtnToStore", WhichBtn);

                // LocationClicked를 거쳐 StoreActivity로 넘겨줄 startData, viaData, endData 전송.
                intent.putExtra("StartLocationItemToStore", startData);
                intent.putExtra("ViaLocationItemToStore", viaData);
                intent.putExtra("EndLocationItemToStore", endData);

                Toast.makeText(LocationClicked.this, "경유지 : " + LocName, Toast.LENGTH_SHORT).show();
                // 값을 다시 돌려받기 위해 사용. Add가 들어오면 SearchActivity로 locationItem과 WhichBtn을 전송.
                startActivityForResult(intent, 0);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WhichBtn = new String("End");
                // 도착지에 지점을 추가하지 않았을때만 추가.
                if(endData.size() == 0) {
                    Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                    intent.putExtra("LocationItemToStore", locationItem);
                    // End 버튼이 눌려서 넘어감을 알려줌. 이 지점은 도착지다.
                    intent.putExtra("WhichBtnToStore", WhichBtn);

                    // LocationClicked를 거쳐 StoreActivity로 넘겨줄 startData, viaData, endData 전송.
                    intent.putExtra("StartLocationItemToStore", startData);
                    intent.putExtra("ViaLocationItemToStore", viaData);
                    intent.putExtra("EndLocationItemToStore", endData);

                    Toast.makeText(LocationClicked.this, "도착지 : " + LocName, Toast.LENGTH_SHORT).show();
                    // 값을 다시 돌려받기 위해 사용. Add가 들어오면 SearchActivity로 locationItem과 WhichBtn을 전송.
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(LocationClicked.this, "도착지를 이미 선택하셨습니다. 도착지 : " + endData.get(0).getLocName(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    // StoreActivity로부터 Add를 전달받을 경우, SearchActivity로 locationItem과 WhichBtn을 전송.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            String resultStr = data.getStringExtra("StoreResult");
            if(resultStr.equals("Add")) {
                // Add를 넘겨받았으면. SearchActivity로 locationItem과 WhichBtn을 전송.
                Intent outIntent = new Intent(getApplicationContext(), SearchActivity.class);

                //LocationClicked에서 돌아가므로 키워드는 LocResult, 추가 명령이므로 Add 전송.
                outIntent.putExtra("LocResult", "Add");
                // locationItem과 WhichBtn 전송.
                outIntent.putExtra("LocationItemBack", locationItem);
                outIntent.putExtra("WhichBtnBack", WhichBtn);
                setResult(RESULT_OK, outIntent);
                finish();
            }
        }
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }
}
