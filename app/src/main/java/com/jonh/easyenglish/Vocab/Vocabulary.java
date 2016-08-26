package com.jonh.easyenglish.Vocab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jonh.easyenglish.R;

import java.io.Serializable;

public class Vocabulary extends AppCompatActivity {

    String token;
    private Button btnInsertar;
    private Button btnMostrar;
    int idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TOKEN
        Bundle extra = getIntent().getExtras();
        token = (String)extra.get("token");
        idUser = (int)extra.get("idUser");
        //CONTROLES
        btnInsertar = (Button)findViewById(R.id.btn_insertar_vocab);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vocabulary.this,Insert_Vocab.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        btnMostrar = (Button) findViewById(R.id.btn_show);
        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vocabulary.this,Show_vocab.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
