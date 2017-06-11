package kr.ac.gachon.finalprototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kalios on 2017-06-11.
 */

public class LocationClicked extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = null;

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

        editSearch1.setText(intent.getStringExtra("LocName"));
        textLocName1.setText(intent.getStringExtra("LocName"));
        textLocAddress1.setText(intent.getStringExtra("LocAddress"));

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
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
            }
        });
    }

    // 오버라이딩 안하면 오류남. 반드시 해줘야 함.
    @Override
    public void onClick(View v) {
    }
}
