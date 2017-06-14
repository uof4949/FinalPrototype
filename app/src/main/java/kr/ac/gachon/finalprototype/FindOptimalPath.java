package kr.ac.gachon.finalprototype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by kalios on 2017-06-12.
 */

public class FindOptimalPath extends AppCompatActivity implements View.OnClickListener {

    private TMapData tmapdata = null;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private TMapPoint tpoint = null;
    private Context mContext = null;
    private double Latitude = 0;
    private double Longitude = 0;
    private static String mApiKey = "eb2f6d74-4047-3adf-be50-928b2797d49e";
    private static int mMarkerID;


    // 버튼들
    private Button btnZoomIn2;
    private Button btnZoomOut2;
    private Button btnCompassMode2;
    private Button btnSetTrackingMode2;
    private Button btnSetMapType2;
    private Button btnReturnToMain;
    private Button btnHide2;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    // 받아온 좌표들.
    ArrayList<LocationItem> startData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> viaData = new ArrayList<LocationItem>();
    ArrayList<LocationItem> endData = new ArrayList<LocationItem>();

    // 경유지는 TMapPoint ArrayList이므로 따로 지정해주고 viaData에서 받아온다.
    ArrayList<TMapPoint> passList = new ArrayList<TMapPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findoptimalpath);
        setupInitialMap();
    }

    // 실제 실행해야 하는 부분
    public void setupInitialMap() {
        mContext = this;

        // 버튼 선언
        btnZoomIn2 = (Button) findViewById(R.id.BtnZoomIn2);
        btnZoomOut2 = (Button) findViewById(R.id.BtnZoomOut2);
        btnCompassMode2 = (Button) findViewById(R.id.BtnCompassMode2);
        btnSetTrackingMode2 = (Button) findViewById(R.id.BtnSetTrackingMode2);
        btnSetMapType2 = (Button) findViewById(R.id.BtnSetMapType2);
        btnReturnToMain = (Button) findViewById(R.id.BtnReturnToMain);
        btnHide2 = (Button) findViewById(R.id.BtnHide2);

        Intent intent = getIntent();

        // 초기화.
        startData.clear();
        viaData.clear();
        endData.clear();
        passList.clear();

        // SearchActivity에서 LocationClicked를 거쳐 StoreActivity로 전달될 startData, viaData, endData 받아옴.
        // LocationClicked에서 넘어올 때 새로 생성되므로 매번 넣어줘야함.
        startData = intent.getParcelableArrayListExtra("StartLocationItemToFind");
        viaData = intent.getParcelableArrayListExtra("ViaLocationItemToFind");
        endData = intent.getParcelableArrayListExtra("EndLocationItemToFind");

        // passList에 viaData의 TMapPoint 값을 뽑아서 넣어준다.
        for(int i = 0; i < viaData.size(); i++) {
            TMapPoint tMapPoint = viaData.get(i).getPOIItem().getPOIPoint();
            passList.add(tMapPoint);
        }


        tmapdata = new TMapData(); // POI 검색, 경로검색 등의 지도데이터를 관리하는 클래스.
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.optimalmapview);
        tmapview = new TMapView(this);

        linearLayout.addView(tmapview);
        tmapview.setSKPMapApiKey(mApiKey);

        //addPoint();
        //howMarkerPoint();

        // 현재 보는 방향
        tmapview.setCompassMode(false);

        // 현위치 아이콘표시
        //tmapview.setIconVisibility(true);

        // 줌레벨
        // 왼쪽 상단 좌표구함
        TMapPoint leftTop = getLeftTopPoint(startData, viaData, endData);
        // 오른쪽 하단 좌표구함
        TMapPoint rightBottom = getRightBottomPoint(startData, viaData, endData);
        // 주어진 좌표에 맞게 줌레벨을 조정.
        tmapview.zoomToTMapPoint(leftTop, rightBottom);
        //tmapview.MapZoomOut();
        //tmapview.MapZoomOut();

        // 왼쪽 상단 좌표와 오른쪽 상단 좌표의 평균 좌표를 구한다.
        double avgLongitude = (leftTop.getLongitude() + rightBottom.getLongitude()) / 2;
        double avgLatitude = (leftTop.getLatitude() + rightBottom.getLatitude()) / 2;
        // 그 좌표로 지도의 중심을 이동한다. 세 번째 인자는 애니메이션 적용 여부.
        tmapview.setCenterPoint(avgLongitude, avgLatitude, true);

        // 지도 타입
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        // 언어 설정
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(FindOptimalPath.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        // 연결된 인터넷으로 현 위치를 받는다.
        // 실내일 때 유용함.
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); // gps로 현 위치를 잡습니다.
        //tmapgps.OpenGps();

        // 현재 위치의 좌표를 반환합니다.
        //tpoint = tmapgps.getLocation();

        // 현재위치로 표시되는 좌표의 위도, 경도를 반환.
        tpoint = tmapview.getLocationPoint();
        Latitude = tpoint.getLatitude();
        Longitude = tpoint.getLongitude();

        // 화면중심을 단말의 현재위치로 이동.
        //tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);


        // 경로 요청.
        // 첫 번째 인자는 자동차 경로 type.
        // 두 번째 인자는 출발지 위치 좌표.
        // 세 번째 인자는 도착지 위치 좌표.
        // 네 번째 인자는 경유지에 대한 좌표.
        // 다섯 번째 인자는 경로 탐색 옵션. 0이 교통최적 + 추천(기본값).
        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,
                startData.get(0).getPOIItem().getPOIPoint(), endData.get(0).getPOIItem().getPOIPoint(),
                passList, 0,
                new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        tmapview.addTMapPath(polyLine);
                    }
                });

        // 버튼 리스너 등록
        btnZoomIn2.setOnClickListener((View.OnClickListener) this);
        btnZoomOut2.setOnClickListener((View.OnClickListener) this);
        btnSetTrackingMode2.setOnClickListener((View.OnClickListener) this);
        btnCompassMode2.setOnClickListener((View.OnClickListener) this);
        btnSetMapType2.setOnClickListener((View.OnClickListener) this);
        btnReturnToMain.setOnClickListener((View.OnClickListener) this);
        btnHide2.setOnClickListener((View.OnClickListener) this);
    }

    // 왼쪽 상단 좌표를 구하기 위한 메소드.
    // 가장 왼쪽에 있는건 경도가 제일 낮은것을 구하면 된다.
    public TMapPoint getLeftTopPoint(ArrayList<LocationItem> start, ArrayList<LocationItem> via, ArrayList<LocationItem> end) {
        // 모든 지점을 저장할 ArrayList.
        ArrayList<LocationItem> allLocation = new ArrayList<LocationItem>();
        // 반환할 왼쪽 상단 좌표.
        TMapPoint leftTop = null;

        // 출발지 가져옴.
        allLocation.add(start.get(0));
        // 경유지 가져옴.
        for(int i = 0; i < via.size() ; i++)
            allLocation.add(via.get(i));
        // 도착지 가져옴.
        allLocation.add(end.get(0));

        leftTop = allLocation.get(0).getPOIItem().getPOIPoint();

        for(int i = 0; i < allLocation.size(); i++) {
            if(leftTop.getLongitude() > allLocation.get(i).getPOIItem().getPOIPoint().getLongitude()) {
                leftTop = allLocation.get(i).getPOIItem().getPOIPoint();
            }
        }

        return leftTop;
    }

    // 오른쪽 하단 좌표를 구하기 위한 메소드.
    // 가장 오른쪽에 있는건 경도가 제일 큰 것을 구하면 된다.
    public TMapPoint getRightBottomPoint(ArrayList<LocationItem> start, ArrayList<LocationItem> via, ArrayList<LocationItem> end) {
        // 모든 지점을 저장할 ArrayList.
        ArrayList<LocationItem> allLocation = new ArrayList<LocationItem>();
        // 반환할 오른쪽 하단 좌표.
        TMapPoint rightBottom = null;

        // 출발지 가져옴.
        allLocation.add(start.get(0));
        // 경유지 가져옴.
        for(int i = 0; i < via.size() ; i++)
            allLocation.add(via.get(i));
        // 도착지 가져옴.
        allLocation.add(end.get(0));

        rightBottom = allLocation.get(0).getPOIItem().getPOIPoint();

        for(int i = 0; i < allLocation.size(); i++) {
            if(rightBottom.getLongitude() < allLocation.get(i).getPOIItem().getPOIPoint().getLongitude()) {
                rightBottom = allLocation.get(i).getPOIItem().getPOIPoint();
            }
        }

        return rightBottom;
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
            case R.id.BtnZoomIn2:
                Toast.makeText(FindOptimalPath.this, "확대", Toast.LENGTH_SHORT).show();
                tmapview.MapZoomIn();
                break;
            case R.id.BtnZoomOut2:
                Toast.makeText(FindOptimalPath.this, "축소", Toast.LENGTH_SHORT).show();
                tmapview.MapZoomOut();
                break;
            case R.id.BtnCompassMode2:
                if(tmapview.getIsCompass()) {
                    Toast.makeText(FindOptimalPath.this, "나침반 모드 OFF", Toast.LENGTH_SHORT).show();
                    tmapview.setCompassMode(false);
                }
                else {
                    Toast.makeText(FindOptimalPath.this, "나침반 모드 ON", Toast.LENGTH_SHORT).show();
                    tmapview.setCompassMode(true);
                }
                break;
            case R.id.BtnSetTrackingMode2:
                Toast.makeText(FindOptimalPath.this, "현위치로 돌아갑니다", Toast.LENGTH_SHORT).show();
                tmapview.setTrackingMode(true);
                tmapview.setSightVisible(true);
                break;
            case R.id.BtnSetMapType2:
                if(tmapview.getMapType() == TMapView.MAPTYPE_STANDARD) {
                    Toast.makeText(FindOptimalPath.this, "실시간교통지도로 변경합니다.", Toast.LENGTH_SHORT).show();
                    tmapview.setMapType(TMapView.MAPTYPE_TRAFFIC);
                }
                else {
                    Toast.makeText(FindOptimalPath.this, "일반지도로 변경합니다.", Toast.LENGTH_SHORT).show();
                    tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
                }
                break;
            case R.id.BtnReturnToMain:
                Toast.makeText(FindOptimalPath.this, "메인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.BtnHide2:
                // 보이도록 설정되어 있을경우.
                if(btnZoomIn2.getVisibility() == View.VISIBLE) {
                    btnZoomIn2.setVisibility(View.INVISIBLE);
                    btnZoomOut2.setVisibility(View.INVISIBLE);
                    btnCompassMode2.setVisibility(View.INVISIBLE);
                    btnSetTrackingMode2.setVisibility(View.INVISIBLE);
                    btnSetMapType2.setVisibility(View.INVISIBLE);
                    btnReturnToMain.setVisibility(View.INVISIBLE);
                }
                else {
                    btnZoomIn2.setVisibility(View.VISIBLE);
                    btnZoomOut2.setVisibility(View.VISIBLE);
                    btnCompassMode2.setVisibility(View.VISIBLE);
                    btnSetTrackingMode2.setVisibility(View.VISIBLE);
                    btnSetMapType2.setVisibility(View.VISIBLE);
                    btnReturnToMain.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
