package com.example.ndif_yemmanuel.smart_q;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private static final String REGISTER_URL = "https://dd0598df.ngrok.io/user/register";
            //"https://mobile-companion.herokuapp.com/user/register";

    public static final String KEY_FIRSTNAME = "user.fname";
    public static final String KEY_LASTNAME = "user.lname";
    public static final String KEY_EMAIL = "contact.email";
    public static final String KEY_PHONE = "contact.phone";
    public static final String KEY_PASSWORD = "security.password";
    public static final String KEY_DATA = "data";
    public static final String KEY_FIND = "find";
    String DATA_VALUE = "";
    String FIND_VALUE = "";

    private EditText editTextFirstname;
    private EditText editTextLastname;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;

    private Button btnlogin, btnsignup2;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //adding back-button to the toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editTextFirstname = findViewById(R.id.edt_first_name);
        editTextLastname = findViewById(R.id.edt_last_name);
        editTextUsername = findViewById(R.id.edt_user_name);
        editTextEmail = findViewById(R.id.edtemail);
        editTextPhone = findViewById(R.id.edtphone);
        editTextPassword = findViewById(R.id.edtpassword);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnsignup2 = findViewById(R.id.btnsignup2);
        btnsignup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                final String fname = editTextFirstname.getText().toString().trim();
                final String lname = editTextLastname.getText().toString().trim();
                final String username = editTextUsername.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String phone = editTextPhone.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(signup.this, response, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                System.out.println(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(signup.this, "Error... Please try again", Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        DATA_VALUE = "{\"user.fname\":" +'"'+fname+ '"'+", " +
                                "\"user.lname\":" +'"'+lname+ '"'+", " +
                                "\"contact.email\":" +'"' +email+ '"'+", " +
                                "\"contact.phone\":" +'"' +phone+ '"'+", " + "\"security.password\":" +'"' +password+ "\"}";

                        FIND_VALUE = "{\"contact.email\":" +'"'+email+ "\"}";
                        params.put(KEY_FIRSTNAME, fname);
                        params.put(KEY_LASTNAME, lname);
                        params.put(KEY_EMAIL, email);
                        params.put(KEY_PHONE, phone);
                        params.put(KEY_PASSWORD, password);
                        params.put(KEY_DATA, DATA_VALUE);
                        params.put(KEY_FIND, FIND_VALUE);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(signup.this);
                requestQueue.add(stringRequest);
            }
        });

        btnlogin = findViewById(R.id.btnlogin2);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signup.this, MainActivity2.class);
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
}
