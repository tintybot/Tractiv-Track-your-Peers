package com.example.tractiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class helpothers extends AppCompatActivity {
    private ProgressDialog progressdialog;
    public String res;
    public static String lat;
    public static String lon;
    public static String username;
    public String Pr_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpothers);
        progressdialog = new ProgressDialog(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.example_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.register:
                Intent i_reg = new Intent(getApplicationContext(),register_user.class);
                startActivity(i_reg);
                return true;
            case R.id.login:
                Intent i_log = new Intent(getApplicationContext(),login.class);
                startActivity(i_log);
                return true;
            case R.id.helpothers:
                Intent i_help = new Intent(getApplicationContext(),helpothers.class);
                startActivity(i_help);
                return true;
            case R.id.aboutus:
                Intent i_trac = new Intent(getApplicationContext(),AboutUs.class);
                startActivity(i_trac);
                return true;
            case R.id.contactus:
                // contact us page
                Intent i_contact = new Intent(getApplicationContext(),Contact_Us.class);
                startActivity(i_contact);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private JSONObject convertDatatoJson()
    {
        JSONObject jsonobject=new JSONObject();
        try {
            jsonobject.put("Status","Help Others");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonobject;
    };

    private String getserverresponse(JSONObject json) {
        String url="https://sos-rest-api.herokuapp.com/api/helpothers";
        HttpPost post=new HttpPost(url);
        try {
            StringEntity entity;
            entity = new StringEntity(json.toString(),"UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type","application/json");
            DefaultHttpClient client=new DefaultHttpClient();
            BasicResponseHandler handler=new BasicResponseHandler();
            res = client.execute(post,handler);
            Pr_string = res.substring(11,res.length()-3);
            /*res=res.substring(12,res.length()-4);
            Log.d("Tag", res);
            String[] parts = res.split(",");
            lat= parts[0].substring(11,parts[0].length()-1);
            lon=parts[1].substring(13,parts[1].length()-1);
            username=parts[3].substring(12,parts[3].length()-0);
            Log.d("Tag", username);*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to contact server";
    }
    public void help_others(View view) {
        progressdialog.setMessage("Redirecting to map ..");
        progressdialog.show();
        final JSONObject json=convertDatatoJson();
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return getserverresponse(json);
            }

            @Override
            protected void onPostExecute(String s) {

                //new addition
                if (Pr_string.equals("Error")==true){
                    Toast.makeText(helpothers.this, "Failed", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else if (Pr_string.equals("No Active Cases")==true){
                    Toast.makeText(helpothers.this, "No Active case", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else{
                    res=res.substring(12,res.length()-4);
                    String[] parts = res.split(",");
                    lat= parts[0].substring(11,parts[0].length()-1);
                    lon=parts[1].substring(13,parts[1].length()-1);
                    username=parts[3].substring(12,parts[3].length()-0);

                    Intent i = new Intent(getApplicationContext(), map.class);
                    i.putExtra("Value1", lat);
                    i.putExtra("Value2", lon);
                    i.putExtra("Value3", username);
                    progressdialog.hide();
                    startActivity(i);
                }

                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
              /*  Log.d("Tag", lat);
                Log.d("Tag", lon);
                Intent i = new Intent(getApplicationContext(), map.class);
                i.putExtra("Value1", lat);
                i.putExtra("Value2", lon);
                i.putExtra("Value3", username);
                progressdialog.hide();  //new addition
                startActivity(i);*/
                super.onPostExecute(s);
            }
        }.execute();
        //Toast.makeText(MainActivity.this, "Lat : "+lat+"lon : "+lon, Toast.LENGTH_SHORT).show();


    }
}