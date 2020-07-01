package com.example.tractiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AfterLogin extends AppCompatActivity {
    String username;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Value1");
        //Toast.makeText(AfterLogin.this, "Welcome"+username, Toast.LENGTH_SHORT).show();
        user = findViewById(R.id.user);//new adittion
        user.setText("Welcome "+username);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(AfterLogin.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.after_login,menu);

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
                Intent i_logout = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i_logout);
                Toast.makeText(AfterLogin.this, "Successful Logged out", Toast.LENGTH_SHORT).show();
                Toast.makeText(AfterLogin.this, "Thank You For Using Our Services"+username, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

