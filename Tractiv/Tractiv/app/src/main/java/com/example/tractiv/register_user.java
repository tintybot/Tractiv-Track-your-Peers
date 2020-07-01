package com.example.tractiv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class register_user extends AppCompatActivity {
    EditText username_registered;
    EditText password_register;
    EditText email_register;
    EditText phone_register;
    String username;
    public String str;
    String email;
    String password;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    public void reg_user(View view) {
        username_registered=(EditText)findViewById(R.id.register_first_name);
        email_register=(EditText)findViewById(R.id.register_email);
        phone_register=(EditText)findViewById(R.id.register_phone_number);
        password_register=(EditText)findViewById(R.id.register_password);
        username=username_registered.getText().toString();
        email=email_register.getText().toString();
        password=password_register.getText().toString();
        phone=phone_register.getText().toString();
        final JSONObject json=convertDatatoJson();
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return getserverresponse(json);
            }

            @Override
            protected void onPostExecute(String s) {
                //do something that we wanna do

                if (str.equals("Done")==true){
                    Intent i_login=new Intent(getApplicationContext(),AfterLogin.class);
                    i_login.putExtra("Value1", username);
                    //progressdialog.hide();
                    startActivity(i_login);
                    Toast.makeText(register_user.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(register_user.this, "email already registered", Toast.LENGTH_SHORT).show();
                }

                super.onPostExecute(s);
            }
        }.execute();
    }
    private JSONObject convertDatatoJson(){
        JSONObject jsonobject=new JSONObject();
        try {
            jsonobject.put("username",username);
            jsonobject.put("email",email);
            jsonobject.put("phone",phone);
            jsonobject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonobject;
    }
    private String getserverresponse(JSONObject json) {
        String url="https://sos-rest-api.herokuapp.com/api/signup";
        HttpPost post=new HttpPost(url);
        try {
            StringEntity entity;
            entity = new StringEntity(json.toString(),"UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type","application/json");
            DefaultHttpClient client=new DefaultHttpClient();
            BasicResponseHandler handler=new BasicResponseHandler();
            str=client.execute(post,handler);

            str= str.substring(11,str.length()-3);


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

