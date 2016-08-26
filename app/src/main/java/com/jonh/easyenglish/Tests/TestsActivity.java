package com.jonh.easyenglish.Tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jonh.easyenglish.R;

import java.io.Serializable;

public class TestsActivity extends AppCompatActivity {

    private Button iNew;
    private Button iPerformed;
    String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");

        //CONTROLES
        iPerformed = (Button) findViewById(R.id.btnPerformedTest);
        iNew = (Button)findViewById(R.id.btnNewTest);

        //EVENTOS
        iNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestsActivity.this, TestConfig.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        iPerformed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestsActivity.this, PerformedTest.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState){
       // savedInstanceState.putString("token", token.getToken());
        //savedInstanceState.putInt("id", token.getIdUsr());
        super.onSaveInstanceState(savedInstanceState);

    }
}
