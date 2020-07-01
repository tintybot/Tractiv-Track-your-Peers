package com.example.tractiv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact_Us extends AppCompatActivity {

    EditText message,name,email;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        message=findViewById(R.id.message);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("");
                name.setText("");
                email.setText("");
                Toast.makeText(Contact_Us.this, "Your Response is recorded !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
