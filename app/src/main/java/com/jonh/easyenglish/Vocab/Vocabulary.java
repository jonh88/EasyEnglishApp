package com.jonh.easyenglish.Vocab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;

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

        SharedPreferences preferences = getPreferences(0);
        boolean msg = preferences.getBoolean("expVoc", true);

        if (msg){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Vocabulary.this);
            dialogBuilder.setTitle("Ayuda");
            dialogBuilder.setMessage("Puedes insertar nuevas palabras que vayas aprendiendo" +
                    " o visualizar las que ya hayas insertado.");
            dialogBuilder.setPositiveButton(android.R.string.ok, null);
            dialogBuilder.setNeutralButton("No mostrar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = getPreferences(0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("expVoc");
                    editor.putBoolean("expVoc", false);
                    editor.commit();
                }
            });

            AlertDialog descargaDialog = dialogBuilder.create();
            descargaDialog.show();
        }

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
            case R.id.mMain:
                i = new Intent(Vocabulary.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (Vocabulary.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(Vocabulary.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            /*case R.id.mVocabulario:
                i = new Intent(Vocabulary.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;*/
            case R.id.mCuestionario:
                i = new Intent(Vocabulary.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(Vocabulary.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

}
