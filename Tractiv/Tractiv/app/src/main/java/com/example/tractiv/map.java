package com.example.tractiv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    public Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    public double lat;
    public double lon;
    public String U;
    private ProgressDialog progressdialog;
    public String res;
    public static String lat1;
    public static String lon1;
    public static String username;
    public String Pr_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        progressdialog = new ProgressDialog(this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fetchlastlocation();
        Bundle extras = getIntent().getExtras();
        String la = extras.getString("Value1");
        String lo = extras.getString("Value2");
        U = extras.getString("Value3");
        lat=Double.parseDouble(la);
        lon=Double.parseDouble(lo);

    }

    private void fetchlastlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    currentlocation=location;
                    Toast.makeText(map.this, "latitude: "+currentlocation.getLatitude()+"Longitude :"+currentlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(map.this);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        LatLng latLng2=new LatLng(lat,lon);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
        MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title(U+" needs help");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2,5));
        googleMap.addMarker(markerOptions);
        googleMap.addMarker(markerOptions2);

    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    fetchlastlocation();
                }
                break;

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
    public void refresh(View view) {
        progressdialog.setMessage("Refreshing ..");
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
                    Toast.makeText(map.this, "Failed", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else if (Pr_string.equals("No Active Cases")==true){
                    Toast.makeText(map.this, "No Active case", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else{
                    res=res.substring(12,res.length()-4);
                    String[] parts = res.split(",");
                    lat1= parts[0].substring(11,parts[0].length()-1);
                    lon1=parts[1].substring(13,parts[1].length()-1);
                    username=parts[3].substring(12,parts[3].length()-0);

                    Intent i = new Intent(getApplicationContext(), map.class);
                    i.putExtra("Value1", lat1);
                    i.putExtra("Value2", lon1);
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