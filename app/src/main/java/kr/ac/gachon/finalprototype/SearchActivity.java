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

    //TMapPoint tpoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button btnSearch1 = (Button) findViewById(R.id.BtnSearch1);

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
                //adapter.notifyDataSetChanged();

                // 아이템 클릭시 작동
                listLocView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), LocationClicked.class);
                        // putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값.
                        // 이름, 주소, POIItem 세개 넘기지 말고 그냥 LocationItem 넘기면 해결되는거 아님?
                        intent.putExtra("LocationItem", data.get(position));
                        //intent.putExtra("LocName", data.get(position).getLocName());
                        //intent.putExtra("LocAddress", data.get(position).getLocAddress());
                        //intent.putExtra("POIItem", data.get(position).getPOIItem());
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
            EditText editSearch = (EditText) findViewById(R.id.EditSearch);
            String searchValue = data.getStringExtra("SearchValue").toString();
            // 검색창 값을 가져옴. 가천대역[분당선] 검색시 가천대역[분당선] 그대로 가져옴.
            editSearch.setText(searchValue);
            // 커서를 맨 끝으로 보냄.
            editSearch.setSelection(searchValue.length());
        }
    }
}