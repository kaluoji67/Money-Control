package com.example.pluralcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class aboutUs extends AppCompatActivity {

    /////////////////////////////////////
    FirebaseUser user;
    String userID;
    //////////////////////////////////////
    TextView content;

    String projectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();

        Intent i = getIntent();
        projectName = i.getStringExtra("projectName");

        ////////////////////// Auth
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        content=(TextView)findViewById(R.id.about_us_content);

        content.setText("  At PluralCode technologies, we all come to work \n" +
                " every day to enable people make smart decisions \n" +
                " about their money every day.\n" +
                " \n" +
                " We believe that managing finance should be as \n" +
                " effortless asshopping online. It should be done \n" +
                " anytime, anywhere and in few clicks.What started \n" +
                " as a simple expense tracker for a small group \n" +
                " of people has grown into personal finance app \n" +
                " that brings beauty to finance of hundreds of \n" +
                "thousands users from almost every country in \n" +
                "the world. ");
    }

    public void backTosett(View v){
        Intent i = new Intent(aboutUs.this, settings.class);
        i.putExtra("projectName", projectName);
        startActivity(i);
        finish();
    }
}