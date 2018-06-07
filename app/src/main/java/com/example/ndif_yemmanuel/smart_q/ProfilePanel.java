package com.example.ndif_yemmanuel.smart_q;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePanel extends AppCompatActivity {

    Button editprofile;
    TextView txtfirstname, txtlastname, txtemail, txtphone, username;
    ImageView pics;
    String DATA_VALUE = "";
    public static final String KEY_DATA = "data";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  /*to remove title bar*/
        setContentView(R.layout.activity_profile_panel);

        //adding back-button to the toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //get passed string using shared preference
        //SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
        //final String fname = sharedPreferences.getString("fname", "fname");
        //final String lname = sharedPreferences.getString("lname", "lame");
        //final String email = sharedPreferences.getString("email2", "email2");
        //final String phone = sharedPreferences.getString("phone", "phone");

         //pics = findViewById(R.id.userimage);


         getprofile();











        editprofile = findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePanel.this, edit_profile.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void getprofile(){
        SharedPreferences sharedPreferences = getSharedPreferences("FnameLnameEmailPhone", Context.MODE_PRIVATE);
        final String emailr = sharedPreferences.getString("email", "email_pass");

        SharedPreferences sharedPreferences1 = getSharedPreferences("pass", Context.MODE_PRIVATE);
        final String token = sharedPreferences1.getString("token", "token");

        String VIEW_PROFILE = "https://mobile-companion.herokuapp.com/user/view-profile?token="+token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject res = jsonObject.getJSONObject("result");
                            JSONObject res2 = res.getJSONObject("user");
                            JSONObject res3 = res.getJSONObject("contact");

                            String first_name = res2.getString("fname");
                            String last_name = res2.getString("lname");
                            String email_pass = res3.getString("email");
                            String phone = res3.getString("phone");

                            txtfirstname = findViewById(R.id.txtfirstname);
                            txtfirstname.setText(first_name);

                            txtlastname = findViewById(R.id.txtlastname);
                            txtlastname.setText(last_name);

                            txtemail = findViewById(R.id.txtemail);
                            txtemail.setText(email_pass);

                            txtphone = findViewById(R.id.txtphone);
                            txtphone.setText(phone);

                            username = findViewById(R.id.username);
                            username.setText(first_name+" "+last_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(ProfilePanel.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);


                        //Toast.makeText(HomePage.this, first_name+" "+last_name, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                DATA_VALUE = "{\"contact.email\":" +'"'+emailr+ "\"}";
                map.put(KEY_DATA, DATA_VALUE);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
