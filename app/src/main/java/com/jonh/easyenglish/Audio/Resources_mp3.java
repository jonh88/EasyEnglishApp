package com.jonh.easyenglish.Audio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.AsynchronusTasks.GetMp3;
import com.jonh.easyenglish.AsynchronusTasks.GetResources;
import com.jonh.easyenglish.Cuestionario.Cuestionario;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Resources_mp3 extends AppCompatActivity {

    private String token;
    private ProgressBar pBar;
    private int idUser;
    private ListView lvResources;
    private ArrayAdapter<String> adapterList;
    private String selected;
    private String [] data;
    private ArrayList<String> resources = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_mp3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Token
        Bundle extras = getIntent().getExtras();
        token = (String) extras.get("token");
        idUser = (int)extras.get("idUser");
        //CONTROLES
        lvResources = (ListView)findViewById(R.id.lvResources_mp3);
        pBar = (ProgressBar)findViewById(R.id.downloadProgress);

        //OBTENGO LOS MP3 DISPONIBLES EN EL SERVER
        GetResources getMp3 = new GetResources(token, idUser, Resources_mp3.this, lvResources, pBar, lvResources);
        getMp3.execute();

        lvResources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //muestro dialogo con el resultado
                selected = (String)lvResources.getAdapter().getItem(position);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Resources_mp3.this);
                dialogBuilder.setTitle("Descarga");
                dialogBuilder.setMessage("¿Desea descargar " + selected + "?");
                dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //descargar audio
                        showProgress(true);
                        GetMp3 des = new GetMp3(token, idUser,selected, Resources_mp3.this, lvResources, pBar, lvResources);
                        des.execute();

                    }
                });
                dialogBuilder.setNegativeButton(android.R.string.cancel, null);

                AlertDialog descargaDialog = dialogBuilder.create();
                descargaDialog.show();

            }
        });

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
                i = new Intent(Resources_mp3.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (Resources_mp3.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(Resources_mp3.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(Resources_mp3.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(Resources_mp3.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(Resources_mp3.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    private void showProgress(final boolean show) {
            pBar.setVisibility(show ? View.VISIBLE : View.GONE);
            lvResources.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
