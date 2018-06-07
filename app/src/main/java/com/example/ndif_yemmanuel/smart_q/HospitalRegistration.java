package com.example.ndif_yemmanuel.smart_q;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class HospitalRegistration extends AppCompatActivity {

    TextView hospital_name;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  /*to remove title bar*/
        setContentView(R.layout.activity_hospital_registration);

        //adding back-button to the toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        hospital_name = findViewById(R.id.hospital_name);
        name = getIntent().getExtras().getString("HospitalName");
        hospital_name.setText(name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
