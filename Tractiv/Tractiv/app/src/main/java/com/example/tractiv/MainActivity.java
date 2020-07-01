package com.example.tractiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pusher.pushnotifications.PushNotifications;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getStarted= findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), register_user.class);
                startActivity(i);
            }
        });
        PushNotifications.start(getApplicationContext(), "fbdbd35d-e9d1-4141-819b-d145c92b33cf");
        PushNotifications.addDeviceInterest("hello");
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
}
