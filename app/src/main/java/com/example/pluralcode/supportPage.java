package com.example.pluralcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class supportPage extends AppCompatActivity {

    /////////////////////////////////////
    FirebaseUser user;
    String userID;
    //////////////////////////////////////
    EditText subject, message;
    Button send, back, clear, back2;

    String projectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_page);
        getSupportActionBar().hide();

        Intent i = getIntent();
        projectName = i.getStringExtra("projectName");

        ////////////////////// Auth
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        subject=findViewById(R.id.subject_editText);
        message=findViewById(R.id.message_editText);

        send=findViewById(R.id.send_to_supp_button);
        back=findViewById(R.id.back_sett_supp_button);
        back2=findViewById(R.id.back_to_sett_frSupp);
        clear=findViewById(R.id.clear_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(supportPage.this, settings.class);
                i.putExtra("projectName", projectName);
                startActivity(i);
                finish();
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(supportPage.this, settings.class);
                i.putExtra("projectName", projectName);
                startActivity(i);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subject.getText().toString().equals("") || subject.getText().toString().equals(null) ){
                    subject.setError("input valid subject");
                    return;
                }

                if(message.getText().toString().equals("") || message.getText().toString().equals(null) ){
                    message.setError("input valid subject");
                    return;
                }

                String email = "misterobinna@gmail.com";
                String sub = subject.getText().toString().trim();
                String msg = message.getText().toString().trim();

                /*
                  Uri uri = Uri.parse("mailto:" + email)
                        .buildUpon()
                        .appendQueryParameter("subject", sub)
                        .appendQueryParameter("body", msg)
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, "Send mail"));
                */

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
                //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                startActivity(Intent.createChooser(emailIntent, "Send email"));

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setText("");
            }
        });
    }
}