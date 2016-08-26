package com.jonh.easyenglish.Audio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        GetResources getMp3 = new GetResources(token, idUser, Resources_mp3.this, lvResources);
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
                        GetMp3 des = new GetMp3(token, idUser,selected, Resources_mp3.this, pBar, lvResources);
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

    private void showProgress(final boolean show) {
            pBar.setVisibility(show ? View.VISIBLE : View.GONE);
            lvResources.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
