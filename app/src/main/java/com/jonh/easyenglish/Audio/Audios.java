package com.jonh.easyenglish.Audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jonh.easyenglish.R;

import java.io.Serializable;

public class Audios extends AppCompatActivity {

    private String token;
    private int idUser;
    private Button descargar;
    private Button ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TOKEN
        Bundle extras = getIntent().getExtras();
        token = (String) extras.get("token");
        idUser = (int) extras.get("idUser");

        //CONTROLES
        descargar = (Button)findViewById(R.id.btn_descargar);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Audios.this,Resources_mp3.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        ver = (Button)findViewById(R.id.btn_ver);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Audios.this,Resources_Local.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
