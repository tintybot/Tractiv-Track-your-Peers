package com.example.tractiv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class login extends AppCompatActivity {
    private ProgressDialog progressdialog;
    EditText email_txt;
    EditText password_txt;
    String email;
    String password;
    public String api_return_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressdialog = new ProgressDialog(this);
    }

    public void yes_login(View view) {
        progressdialog.setMessage("Please Wait..");
        progressdialog.show();
        email_txt=(EditText)findViewById(R.id.login_email);
        password_txt=(EditText)findViewById(R.id.login_password);
        email=email_txt.getText().toString();
        password=password_txt.getText().toString();
        final JSONObject json=convertDatatoJson();
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return getserverresponse(json);
            }

            @Override
            protected void onPostExecute(String s) {
                //do something that we wanna do

                if (api_return_value.equals("login-success")==true){
                    Intent i_login=new Intent(getApplicationContext(),AfterLogin.class);
                    i_login.putExtra("Value1", email);
                    progressdialog.hide();
                    startActivity(i_login);
                    Toast.makeText(login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressdialog.hide();
                    Toast.makeText(login.this, "Invalid username/ password", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }.execute();
    }
    private JSONObject convertDatatoJson(){
        JSONObject jsonobject=new JSONObject();
        try {
            jsonobject.put("username",email);
            jsonobject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonobject;
    }
    private String getserverresponse(JSONObject json) {
        String url="https://sos-rest-api.herokuapp.com/api/login";
        HttpPost post=new HttpPost(url);
        try {
        StringEntity entity;
        entity = new StringEntity(json.toString(),"UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-Type","application/json");
        DefaultHttpClient client=new DefaultHttpClient();
        BasicResponseHandler handler=new BasicResponseHandler();
        api_return_value=client.execute(post,handler);

        api_return_value= api_return_value.substring(11,api_return_value.length()-3);
        Log.d("Tag", api_return_value);
        //Toast.makeText(this,api_return_value, Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        } catch (ClientProtocolException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        return "Unable to contact server";
    }

}

