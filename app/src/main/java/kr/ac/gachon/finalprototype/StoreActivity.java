package kr.ac.gachon.finalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

/**
 * Created by kalios on 2017-06-11.
 */

public class StoreActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<LocationItem> startData = new ArrayList<LocationItem>();
    ArrayList<TMapPOIItem> startPOIdata = new ArrayList<TMapPOIItem>();
    ArrayList<LocationItem> viaData = new ArrayList<LocationItem>();
    ArrayList<TMapPOIItem> viaPOIdata = new ArrayList<TMapPOIItem>();
    ArrayList<LocationItem> endData = new ArrayList<LocationItem>();
    ArrayList<TMapPOIItem> endPOIdata = new ArrayList<TMapPOIItem>();

    TMapPoint tpoint = null;

    // 출발지, 경유지, 도착지 중 어느 버튼을 눌러 넘어가는지 알려주는 문자열.
    String WhichBtn = null;
    LocationItem locationItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Button btnCompute = (Button) findViewById(R.id.BtnCompute);
        Button btnAdd = (Button) findViewById(R.id.BtnAdd);

        ListView listStartView = (ListView) findViewById(R.id.ListStartView);
        ListView listViaView = (ListView) findViewById(R.id.ListViaView);
        ListView listEndView = (ListView) findViewById(R.id.ListEndView);

        Intent intent = getIntent();

        // 데이터 받아오기
        locationItem = intent.getParcelableExtra("LocationItemToStore");
        WhichBtn = intent.getStringExtra("WhichBtnToStore");

        // 초기화.
        startData.clear();
        viaData.clear();
        endData.clear();
        // SearchActivity에서 LocationClicked를 거쳐 StoreActivity로 전달될 startData, viaData, endData 받아옴.
        // LocationClicked에서 넘어올 때 새로 생성되므로 매번 넣어줘야함.
        startData = intent.getParcelableArrayListExtra("StartLocationItemToStore");
        viaData = intent.getParcelableArrayListExtra("ViaLocationItemToStore");
        endData = intent.getParcelableArrayListExtra("EndLocationItemToStore");

        // 출발지인지, 경유지인지, 도착지인지 판단.
        switch(WhichBtn) {
            case "Start":
                // ArrayList에 원소 추가. LocationsAdapter adapter 생성을 위해.
                startData.add(locationItem);
                startPOIdata.add(locationItem.getPOIItem());
                break;
            case "Via":
                // ArrayList에 원소 추가. LocationsAdapter adapter 생성을 위해.
                viaData.add(locationItem);
                viaPOIdata.add(locationItem.getPOIItem());
                break;
            case "End":
                // ArrayList에 원소 추가. LocationsAdapter adapter 생성을 위해.
                endData.add(locationItem);
                endPOIdata.add(locationItem.getPOIItem());
                break;
        }

        // 출발지 출력.
        for(int i = 0; i < startData.size(); i++) {
            // 리스트 속의 아이템 연결
            LocationsAdapter startAdapter = new LocationsAdapter(StoreActivity.this, R.layout.locations_item, startPOIdata, startData);
            listStartView.setAdapter(startAdapter);
            startAdapter.notifyDataSetChanged();
        }
        // 경유지 출력.
        for(int i = 0; i < viaData.size(); i++) {
            LocationsAdapter viaAdapter = new LocationsAdapter(StoreActivity.this, R.layout.locations_item, viaPOIdata, viaData);
            listViaView.setAdapter(viaAdapter);
            viaAdapter.notifyDataSetChanged();
        }
        // 도착지 출력.
        for(int i = 0; i < endData.size(); i++) {
            LocationsAdapter endAdapter = new LocationsAdapter(StoreActivity.this, R.layout.locations_item, endPOIdata, endData);
            listEndView.setAdapter(endAdapter);
            endAdapter.notifyDataSetChanged();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), LocationClicked.class);
                // StoreActivity에서 돌아가므로 키워드는 StoreResult.
                // 더 추가하라는 메시지를 담아 LocationClicked로 돌아가고,
                // LocationClicked에서는 방금 보낸 WhcihBtn과 locationItem을
                // SearchActivity로 전송한다.
                outIntent.putExtra("StoreResult", new String("Add"));
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }

}
