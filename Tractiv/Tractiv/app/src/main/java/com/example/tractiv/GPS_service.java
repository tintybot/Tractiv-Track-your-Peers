package com.example.tractiv;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class GPS_service extends Service {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context context;
    private Location location;
    private LocationCallback locationCallback;
    private final long INTERVAL = 60000; // getting location for 1 min here
    Timer myTimer = new Timer();

    public String lat;
    public String lon;
    String api_return_value;
    String username;

    public GPS_service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myTimer.schedule(myTask, INTERVAL);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;
                context = getApplicationContext();

                location = locationResult.getLastLocation();

              //  Toast.makeText(getApplicationContext(), location.getLatitude() + " \n" +
                //        location.getLongitude(), Toast.LENGTH_SHORT).show();

                Log.d("Main_GPS_services", "onLocationResult: " + location.getLatitude() + " \n" +
                        location.getLongitude());

                need_help(location.getLatitude(),location.getLongitude());
            }
        };
    }

    TimerTask myTask = new TimerTask()
    {
        public void run()
        {

            Intent i=new Intent(getApplicationContext(),GPS_service.class);
            stopService(i);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username= intent.getStringExtra("username");
        //Toast.makeText(getApplicationContext(),username,Toast.LENGTH_LONG).show();
        Log.d("Services","Started Service");

        getLocationUpdates();

        return START_STICKY;
    }

    private void getLocationUpdates() {

        LocationRequest locationRequest= new LocationRequest();
        locationRequest.setInterval(5000); // location updated every 5 seconds
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            stopSelf();
            return;
        }


        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("Services","Stopped service");
        Intent i = new Intent(getApplicationContext(), AfterLogin.class);
        startActivity(i);
    }

        private void need_help(double latitude, double longitude) {
        lat=Double.toString(latitude);
        lon=Double.toString(longitude);
        final JSONObject json=convertDatatoJson();
            new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                return getserverresponse(json);
            }

            @Override
            protected void onPostExecute(String s) {
                //do something that we wanna do
                //new addition
                if (api_return_value.equals("Error")==true){
                    Toast.makeText(GPS_service.this, "Sorry ! Could not notify peers", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),GPS_service.class);
                    stopService(i);
                }
                else {
                    Toast.makeText(GPS_service.this, "Peers successfully notified", Toast.LENGTH_SHORT).show();

                }
                    //Toast.makeText(GPS_service.this, api_return_value, Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }.execute();
    }
    private JSONObject convertDatatoJson(){
        JSONObject jsonobject=new JSONObject();
        try {
            jsonobject.put("username",username);
            jsonobject.put("latitude",lat);
            jsonobject.put("longitude",lon);
        } catch (JSONException e) {
        e.printStackTrace();
        }
        return jsonobject;
    }
private String getserverresponse(JSONObject json) {
        String url="https://sos-rest-api.herokuapp.com/api/xnotifyothers";
        HttpPost post=new HttpPost(url);
        try {
        StringEntity entity;
        entity = new StringEntity(json.toString(),"UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-Type","application/json");
        DefaultHttpClient client=new DefaultHttpClient();
        BasicResponseHandler handler=new BasicResponseHandler();
        api_return_value=client.execute(post,handler);
        Log.d("Tag", api_return_value);
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
