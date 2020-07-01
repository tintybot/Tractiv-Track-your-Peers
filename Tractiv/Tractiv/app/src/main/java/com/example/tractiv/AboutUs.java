package com.example.tractiv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        ImageView rohit_git = (ImageView) findViewById(R.id.github_rohit);
        ImageView rohit_in = (ImageView) findViewById(R.id.linkedin_rohit);
        rohit_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tintybot"));
                startActivity(intent);
            }
        });
        rohit_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/rohit-halder/"));
                startActivity(intent);
            }
        });
        ImageView shrishti_git = (ImageView) findViewById(R.id.github_shrishti);
        ImageView shrishti_in = (ImageView) findViewById(R.id.linkedin_shrishti);
        shrishti_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Shrishtia02"));
                startActivity(intent);
            }
        });
        shrishti_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/shrishti-932801186/"));
                startActivity(intent);
            }
        });
        ImageView anushmita_git = (ImageView) findViewById(R.id.github_anushmita);
        ImageView anushmita_in = (ImageView) findViewById(R.id.linkedin_anushmita);
        anushmita_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AnushmitaDas"));
                startActivity(intent);
            }
        });
        anushmita_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/anushmita-das-272069183/"));
                startActivity(intent);
            }
        });
        ImageView shailesh_git = (ImageView) findViewById(R.id.github_shailesh);
        ImageView shailesh_in = (ImageView) findViewById(R.id.linkedin_shailesh);
        shailesh_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Angry-BOT"));
                startActivity(intent);
            }
        });
        shailesh_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/shailesh-das-772712171"));
                startActivity(intent);
            }
        });
    }
}
