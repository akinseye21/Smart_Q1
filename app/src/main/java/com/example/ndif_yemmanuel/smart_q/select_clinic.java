package com.example.ndif_yemmanuel.smart_q;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class select_clinic extends AppCompatActivity {

    ListView simpleList;
    public static final String KEY_DATA = "data";
    String DATA_VALUE = "";

    Button maitama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clinic);

        //adding back-button to the toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        simpleList = findViewById(R.id.simpleListView);

        loadhospital();





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void loadhospital(){
        SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "token");

        String VIEW_HOSPITALS = "https://mobile-companion.herokuapp.com/user/view-hospitals?limit=4&page=0&token="+token;
        RequestQueue requestQueue = Volley.newRequestQueue(this);




        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_HOSPITALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            final JSONArray array = jsonObject.getJSONArray("result");
                            final int[] index = {0};
                            final ArrayList<String> hospital_list = new ArrayList<String>();

                            while(index[0] <array.length())
                            {
                                String result = array.getString(index[0]);

                                JSONObject jsonObject1 = new JSONObject(result);
                                String hospital = jsonObject1.getString("hospital");

                                JSONObject jsonObject2 = new JSONObject(hospital);
                                final String name = jsonObject2.getString("name");

                                System.out.print("\n Hospital name "+name+"\n");



                                //String hospital_list[] = {name};
                                hospital_list.add(name);

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(select_clinic.this, R.layout.hospital_listview, R.id.list, hospital_list);
                                simpleList.setAdapter(arrayAdapter);

                                index[0]++;
                            }
                            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        Intent myIntent = new Intent(select_clinic.this, HospitalRegistration.class);
                                        myIntent.putExtra("HospitalName", hospital_list.get(position));
                                        startActivity(myIntent);
                                }

                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.print("Error 1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print("Error 2");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                DATA_VALUE = "{\"location\":" +'"'+"Abuja"+ "\"}";
                map.put(KEY_DATA, DATA_VALUE);
                map.put("page", "0");
                map.put("limit", "5");
                map.put("order", "-updated_at");
                return map;
            }

        };
        requestQueue.add(stringRequest);


    }

    public void click(){


    }

}
