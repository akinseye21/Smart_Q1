package com.example.ndif_yemmanuel.smart_q;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {



    public static final String KEY_FIRSTNAME = "fname";
    public static final String KEY_LASTNAME = "lname";
    public static final String KEY_PHONE = "phone";
    public  static final String KEY_ID = "_id";
    public static final String KEY_IMG = "user.profile_pic";


    private static final int RESULT_LOAD_IMAGE = 1;
    Button uploadimage, editprofile;
    ImageView userimageupload;

    EditText txtfirstname, txtlastname, txtphone;



    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //adding back-button to the toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        //get passed string using shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
        final String _id = sharedPreferences.getString("_id", "_id");



        System.out.println(_id);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        txtfirstname = findViewById(R.id.txtfirstname);
        txtlastname = findViewById(R.id.txtlastname);
        txtphone = findViewById(R.id.txtphone);


        editprofile = findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String fname = txtfirstname.getText().toString().trim();
                final String lname = txtlastname.getText().toString().trim();
                final String phone = txtphone.getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString("token", "token");

                //String REGISTER_URL = "https://dd0598df.ngrok.io/user/updateuser?token="+token;
                String REGISTER_URL = "https://mobile-companion.herokuapp.com/user/updateuser?token="+token;

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(edit_profile.this, "Profile Update Successful", Toast.LENGTH_LONG).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(edit_profile.this, "Error... Profile not updated", Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_ID, _id);
                        params.put(KEY_FIRSTNAME, fname);
                        params.put(KEY_LASTNAME, lname);
                        params.put(KEY_PHONE, phone);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
                requestQueue.add(stringRequest);

            }
        });

        userimageupload = findViewById(R.id.userimageupload);

        uploadimage = findViewById(R.id.uploadimage);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                Bitmap image = ((BitmapDrawable) userimageupload.getDrawable()).getBitmap();
                new UploadImage(image).execute();

               //uploadimage();
            }
        });

    }

    /***
    private String imagetobitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        String temp=Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return temp;
    }
     ***/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            userimageupload.setImageURI(selectedImage);
        }
    }

    private class UploadImage extends AsyncTask<Void, Void, Void>{

        Bitmap image;

        public UploadImage(Bitmap image){
            this.image = image;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
            final String token = sharedPreferences.getString("token", "token");
            String UPLOAD_IMAGE = "http://mobile-companion.herokuapp.com/upload/upload?token="+token;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("files", encodedImage));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client =  new DefaultHttpClient(httpRequestParams);
            HttpPost post =  new HttpPost(UPLOAD_IMAGE);

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }



    /***
    private void uploadimage(){
        SharedPreferences sharedPreferences = getSharedPreferences("pass", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "token");
        String uploadimage = "http://mobile-companion.herokuapp.com/upload/upload?token="+token;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadimage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(edit_profile.this, response, Toast.LENGTH_LONG).show();
                        Log.d("Image response", response);
                        System.out.print("\n Image upload Respose"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(edit_profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error", error.getMessage());
                System.out.print("\n Image upload error"+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("files", imagetobitmap(bitmap));

                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        requestQueue.add(stringRequest);

    }
     ***/


}
