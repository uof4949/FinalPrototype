package kr.ac.gachon.finalprototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

/**
 * Created by kalios on 2017-06-11.
 */

public class StoreActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<LocationItem> data = null;
    ArrayList<TMapPOIItem> POIdata = null;
    TMapPoint tpoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button btnCompute = (Button) findViewById(R.id.BtnCompute);
        Button btnAdd = (Button) findViewById(R.id.BtnAdd);

        TextView textStart = (TextView) findViewById(R.id.TextStart);
        TextView textVia = (TextView) findViewById(R.id.TextVia);
        TextView textEnd = (TextView) findViewById(R.id.TextEnd);

        ListView listStartView = (ListView) findViewById(R.id.ListStartView);
        ListView listViaView = (ListView) findViewById(R.id.ListViaView);
        ListView listEndView = (ListView) findViewById(R.id.ListEndView);
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }

}
