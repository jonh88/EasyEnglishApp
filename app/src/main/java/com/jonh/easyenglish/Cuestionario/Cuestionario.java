package com.jonh.easyenglish.Cuestionario;

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
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;

public class Cuestionario extends AppCompatActivity {

    private Button iNuevo;
    private Button iRealizado;
    String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");

        //CONTROLES
        iNuevo = (Button) findViewById(R.id.btnNewCuestionario);
        iRealizado = (Button)findViewById(R.id.btnPerformedCuestionarios);

        //EVENTOS
        iNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Cuestionario.this, CuestionarioConfig.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        iRealizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cuestionario.this, PerformedCuestionario.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        SharedPreferences preferences = getPreferences(0);
        boolean msg = preferences.getBoolean("expCuestionario", true);

        if (msg){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Cuestionario.this);
            dialogBuilder.setTitle("Ayuda");
            dialogBuilder.setMessage("Puedes realizar cuestionarios o consultar los resultados de" +
                    " los ya realizados.");
            dialogBuilder.setPositiveButton(android.R.string.ok, null);
            dialogBuilder.setNeutralButton("No mostrar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = getPreferences(0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("expCuestionario");
                    editor.putBoolean("expCuestionario", false);
                    editor.commit();
                }
            });

            AlertDialog descargaDialog = dialogBuilder.create();
            descargaDialog.show();
        }
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
                i = new Intent(Cuestionario.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (Cuestionario.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(Cuestionario.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(Cuestionario.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
           /* case R.id.mCuestionario:
                i = new Intent(Cuestionario.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;*/
            case R.id.mAudio:
                i = new Intent(Cuestionario.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }
}
