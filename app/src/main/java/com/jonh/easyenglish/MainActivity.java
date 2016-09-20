package com.jonh.easyenglish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jonh.easyenglish.Audio.Audios;
import com.jonh.easyenglish.Cuestionario.Cuestionario;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private Button btnTest;
    private Button btnVoc;
    private Button btnAudio;
    private Button btnCuest;
    private Button btnGrammar;
    private String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);

            }
        });
        btnVoc = (Button) findViewById(R.id.btnVoc);
        btnVoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        btnCuest = (Button) findViewById(R.id.btnCuest);
        btnCuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        btnAudio = (Button)findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });
        btnGrammar = (Button)findViewById(R.id.btnGrammar);
        btnGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent i;
        switch (item.getItemId()){
            case R.id.mGramatica:
                i = new Intent (MainActivity.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(MainActivity.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(MainActivity.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(MainActivity.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(MainActivity.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }
}
