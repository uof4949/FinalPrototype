package kr.ac.gachon.finalprototype;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback, View.OnClickListener {

    // 필요한 권한 여기서 설정 -> 여기 있는 것들 메니페스트 파일에도 등록해줘야 앱이 필요한 권한이 뭔지 OS가 인식함
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CODE = 0;

    private PermissionsChecker checker;

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private TMapData tmapdata = null;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private TMapPoint tpoint = null;
    private double Latitude = 0;
    private double Longitude = 0;
    private static String mApiKey = "eb2f6d74-4047-3adf-be50-928b2797d49e";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    private String address;
    private Double lat = null;
    private Double lon = null;

    // 버튼들
    private Button btnSearch;
    private Button btnZoomIn;
    private Button btnZoomOut;
    private Button btnCompassMode;
    private Button btnSetTrackingMode;
    private Button btnSetMapType;

    // 에디트 텍스트
    //private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checker.lacksPermissions(PERMISSIONS)) {
            // 권한 못 받았으면 권한 요청하는 액티비티 띄워줌
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }else{
            // 있으면 실행
            setupInitialMap();
        }
    }

    // PermissionActivity 완료 후 결과 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    // 실제 실행해야 하는 부분
    public void setupInitialMap() {
        mContext = this;

        // 버튼 선언
        btnSearch = (Button) findViewById(R.id.BtnSearch);
        btnZoomIn = (Button) findViewById(R.id.BtnZoomIn);
        btnZoomOut = (Button) findViewById(R.id.BtnZoomOut);
        btnCompassMode = (Button) findViewById(R.id.BtnCompassMode);
        btnSetTrackingMode = (Button) findViewById(R.id.BtnSetTrackingMode);
        btnSetMapType = (Button) findViewById(R.id.BtnSetMapType);

        // 에디트 텍스트 선언
        //editSearch = (EditText) findViewById(R.id.EditSearch);

        tmapdata = new TMapData(); // POI 검색, 경로검색 등의 지도데이터를 관리하는 클래스.
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tmapview = new TMapView(this);

        linearLayout.addView(tmapview);
        tmapview.setSKPMapApiKey(mApiKey);

        addPoint();
        showMarkerPoint();

        // 현재 보는 방향
        tmapview.setCompassMode(true);

        // 현위치 아이콘표시
        tmapview.setIconVisibility(true);

        // 줌레벨
        tmapview.setZoomLevel(15);
        // 지도 타입
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        // 언어 설정
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        // 연결된 인터넷으로 현 위치를 받는다.
        // 실내일 때 유용함.
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); // gps로 현 위치를 잡습니다.
        tmapgps.OpenGps();

        // 현재 위치의 좌표를 반환합니다.
        //tpoint = tmapgps.getLocation();

        // 현재위치로 표시되는 좌표의 위도, 경도를 반환.
        tpoint = tmapview.getLocationPoint();
        Latitude = tpoint.getLatitude();
        Longitude = tpoint.getLongitude();

        // 화면중심을 단말의 현재위치로 이동.
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        // 풍선에서 우측 버튼 클릭시 할 행동입니다.
        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                lat = markerItem.latitude;
                lon = markerItem.longitude;

                // 1. 위도, 경도로 주소 검색하기
                tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });
                Toast.makeText(MainActivity.this, "주소 : " + address, Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼 리스너 등록
        btnSearch.setOnClickListener((View.OnClickListener) this);
        btnZoomIn.setOnClickListener((View.OnClickListener) this);
        btnZoomOut.setOnClickListener((View.OnClickListener) this);
        btnSetTrackingMode.setOnClickListener((View.OnClickListener) this);
        btnCompassMode.setOnClickListener((View.OnClickListener) this);
        btnSetMapType.setOnClickListener((View.OnClickListener) this);

        // 에디트 텍스트 리스너.

    }

    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    public void addPoint() { // 여기에 핀을 꼽을 포인트들을 배열에 add해주세요!
        // 강남
        //m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
        m_mapPoint.add(new MapPoint("강남", Latitude, Longitude));
        //m_mapPoint.add(new MapPoint("강남", tpoint.getLatitude(), tpoint.getLongitude()));
    }

    public void showMarkerPoint() { // 마커 찍는거 빨간색 포인트.
        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);
            // poi_dot은 지도에 꼽을 빨간 핀 이미지입니다.

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            // 풍선뷰 안의 항목에 글을 지정합니다.
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.i_go);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d", mMarkerID++);

            tmapview.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnSearch:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.BtnZoomIn:
                Toast.makeText(MainActivity.this, "확대", Toast.LENGTH_SHORT).show();
                tmapview.MapZoomIn();
                break;
            case R.id.BtnZoomOut:
                Toast.makeText(MainActivity.this, "축소", Toast.LENGTH_SHORT).show();
                tmapview.MapZoomOut();
                break;
            case R.id.BtnCompassMode:
                if(tmapview.getIsCompass()) {
                    Toast.makeText(MainActivity.this, "나침반 모드 OFF", Toast.LENGTH_SHORT).show();
                    tmapview.setCompassMode(false);
                }
                else {
                    Toast.makeText(MainActivity.this, "나침반 모드 ON", Toast.LENGTH_SHORT).show();
                    tmapview.setCompassMode(true);
                }
                break;
            case R.id.BtnSetTrackingMode:
                Toast.makeText(MainActivity.this, "현위치로 돌아갑니다", Toast.LENGTH_SHORT).show();
                tmapview.setTrackingMode(true);
                tmapview.setSightVisible(true);
                break;
            case R.id.BtnSetMapType:
                if(tmapview.getMapType() == TMapView.MAPTYPE_STANDARD) {
                    Toast.makeText(MainActivity.this, "실시간교통지도로 변경합니다.", Toast.LENGTH_SHORT).show();
                    tmapview.setMapType(TMapView.MAPTYPE_TRAFFIC);
                }
                else {
                    Toast.makeText(MainActivity.this, "일반지도로 변경합니다.", Toast.LENGTH_SHORT).show();
                    tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
                }
                break;
        }
    }

    public void findAllPOIByName() {

    }
    //3. 주소검색으로 위도, 경도 검색하기
    // 명칭 검색을 통한 주소 변환
    public void convertToAddress() {
        // 다이얼로그 띄워서 검색창에 입력받음.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("POI 통합 검색");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();
                TMapData tmapdata = new TMapData();

                tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);

                            Log.d("주소로찾기", "POI Name : " + item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                    "Point: " + item.getPOIPoint().toString());

                        }
                    }
                });
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // 2. 주변 편의시설 검색하기
    // 화면 중심의 위도 경도를 통한, 주변 편의시설 검색
    public void getAroundBizPoi() {
        TMapData tmapdata = new TMapData();

        TMapPoint point = tmapview.getCenterPoint();

        tmapdata.findAroundNamePOI(point, "편의점;은행", 1, 99,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            Log.d("편의시설", "POI Name: " + item.getPOIName() + ", " + "Address: "
                                    + item.getPOIAddress().replace("null", ""));
                        }
                    }
                });
    }
}
