package kr.ac.gachon.finalprototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;

/**
 * Created by kalios on 2017-06-10.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<LocationItem> data = null;
    private ArrayList<TMapPOIItem> POIdata = null;

    // StoreActivity로 LocationItem을 넘겨주기 위한 ArrayList들.
    ArrayList<LocationItem> startData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> viaData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> endData = new ArrayList<LocationItem>();

    //TMapPoint tpoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button btnSearch1 = (Button) findViewById(R.id.BtnSearch1);

        // 처음 SearchActivity를 실행했을때 StoreActivity로 넘겨줄 ArrayList들 초기화.
        //startData.clear();
        //viaData.clear();
        //endData.clear();

        btnSearch1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchText;
                EditText editSearch = (EditText) findViewById(R.id.EditSearch);
                ListView listLocView = (ListView) findViewById(R.id.ListLocView);

                searchText = editSearch.getText().toString();

                //Toast.makeText(SearchActivity.this, "검색 : " +searchText, Toast.LENGTH_SHORT).show();

                // 키보드 내리기.
                hideSoftKeyboard(v);

                POIdata = new ArrayList<TMapPOIItem>();
                data = new ArrayList<LocationItem>();
                POIdata.clear();
                data.clear();

                TMapData tmapdata = new TMapData();
                tmapdata.findAllPOI(searchText, 20, new FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            //Toast.makeText(SearchActivity.this, "TMapPOIItem item = poiItem.get(i)", Toast.LENGTH_SHORT).show();
                            // i번째 아이템 이름, 주소 받아옴.
                            ParcelableTMapPOIItem pitem = null;
                            pitem = ParcelableTMapPOIItem.convertFromTMapPOIItem(item);
                            //ParcelableTMapPOIItem pitem = new ParcelableTMapPOIItem();
                            //pitem = item;
                            LocationItem locationItem = new LocationItem(item.getPOIName().toString(), item.getPOIAddress().replace("null", ""), pitem);
                            // 리스트에 추가
                            POIdata.add(item);
                            data.add(locationItem);
                            // 검색된 주소 좌표 위치 받아옴.
                            //tpoint = item.getPOIPoint();
                        }
                    }
                });
                // 리스트 속의 아이템 연결
                LocationsAdapter adapter = new LocationsAdapter(SearchActivity.this, R.layout.locations_item, POIdata, data);
                listLocView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                // 아이템 클릭시 작동
                listLocView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), LocationClicked.class);
                        // 해당 위치 정보를 담은 locationItem 넘겨줌.
                        intent.putExtra("LocationItemToLoc", data.get(position));

                        // LocationClicked를 거쳐 StoreActivity로 넘겨줄 startData, viaData, endData 전송.
                        intent.putExtra("StartLocationItemToLoc", startData);
                        intent.putExtra("ViaLocationItemToLoc", viaData);
                        intent.putExtra("EndLocationItemToLoc", endData);

                        startActivityForResult(intent, 0);
                    }
                });

            }
        });
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }

    // 키보드 숨겨주는 메서드.
    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            // LocationClicked로부터 문자열을 받아옴.
            String resultStr = data.getStringExtra("LocResult");

            //문자열이 Add일 경우. 받아온 locationItem을 WhichBtn에 따라 추가해야됨.
            if(resultStr.equals("Add") || resultStr.equals("ReturnToSearch")) {
                // 어떤 버튼을 눌러서 돌아왔는지(출발지, 도착지, 경유지 여부) 판단.
                String WhichBtn = data.getStringExtra("WhichBtnBack");

                // locationItem 받아옴.
                LocationItem locationItem = data.getParcelableExtra("LocationItemBack");

                // 어떤 버튼이냐에 따라 어떤 ArrayList에 넣을지 결정함.
                switch (WhichBtn) {
                    case "Start":
                        startData.add(locationItem);
                        break;
                    case "Via":
                        viaData.add(locationItem);
                        break;
                    case "End":
                        endData.add(locationItem);
                        break;
                }
            }

            // 문자열이 Add가 아닐 경우. LocationClicked에서 검색창을 클릭한 경우이다.
            else {
                EditText editSearch = (EditText) findViewById(R.id.EditSearch);
                String searchValue = data.getStringExtra("SearchValue").toString();
                // 검색창 값을 가져옴. 가천대역[분당선] 검색시 가천대역[분당선] 그대로 가져옴.
                editSearch.setText(searchValue);
                // 커서를 맨 끝으로 보냄.
                editSearch.setSelection(searchValue.length());
            }
        }
    }
}