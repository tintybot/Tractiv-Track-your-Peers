package com.example.tractiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import org.json.JSONObject;
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

public class Tractiv_sos extends AppCompatActivity {

    public Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    public String lat;
    public String lon;
    String api_return_value;
    String username;
    private Button sos;
    private View layout;
    private ImageView anim,anim2;
    private Handler handlerAnimation;
    private Button SOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractiv_sos);
        init();
        // Glide.with(getBaseContext()).load(R.drawable.images).apply(new RequestOptions().circleCrop()).into(SOS);
        startTask();
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Value1");
        sos = findViewById(R.id.sos);
        if(!runtime_permissions())
            enable_buttons();
    }

    private void init() {
        this.handlerAnimation = new Handler();
        this.layout= findViewById(R.id.layoutPics);
        this.SOS =findViewById(R.id.sos);
        this.anim=findViewById(R.id.img_anim1);
        this.anim2=findViewById(R.id.img_anim2);
    }

    private void startTask(){
        this.runnableAnim.run();
        this.layout.setVisibility(View.VISIBLE);
    }

    private Runnable runnableAnim =new Runnable() {
        @Override
        public void run() {
            anim.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    anim.setScaleX(1f);
                    anim.setScaleY(1f);
                    anim.setAlpha(1f);
                }
            });

            anim2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    anim2.setScaleX(1f);
                    anim2.setScaleY(1f);
                    anim2.setAlpha(1f);
                }
            });
            handlerAnimation.postDelayed(runnableAnim,1500);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.after_login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sos:
                Intent i_sos = new Intent(getApplicationContext(),Tractiv_sos.class);
                i_sos.putExtra("Value1", username);
                startActivity(i_sos);
                return true;
            case R.id.logout:
                Intent i_logout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i_logout);
                Toast.makeText(Tractiv_sos.this, "Successful Logged out", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enable_buttons() {

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GPS_service.class);
                i.putExtra("username",username);
                startService(i);
            }
        });
    }
    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }
            else{
                runtime_permissions();
            }

        }
    }



}
