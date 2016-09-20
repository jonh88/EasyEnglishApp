package com.jonh.easyenglish.Cuestionario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jonh.easyenglish.AsynchronusTasks.GetCuestionarios;
import com.jonh.easyenglish.Audio.Audios;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;

public class PerformedCuestionario extends AppCompatActivity {

    ListView lista;
    ArrayAdapter<String> adapter;
    private static String token;
    private static int idUser;
    private View progress, container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performed_cuestionario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //TOKEN + IDUSER
        Bundle extras = getIntent().getExtras();
        PerformedCuestionario.token = (String)extras.get("token");
        PerformedCuestionario.idUser = (int)extras.get("idUser");

        //CONTROLES
        progress = findViewById(R.id.performedCuestionarioProgress);
        container = findViewById(R.id.listViewCuestionario);
        lista = (ListView) findViewById(R.id.listViewCuestionario);

        GetCuestionarios userTests = new GetCuestionarios(PerformedCuestionario.token, PerformedCuestionario.idUser, PerformedCuestionario.this, lista, progress, container);
        userTests.execute();

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
                i = new Intent(PerformedCuestionario.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (PerformedCuestionario.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(PerformedCuestionario.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(PerformedCuestionario.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(PerformedCuestionario.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(PerformedCuestionario.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

}
