package com.jonh.easyenglish.Tests;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.AsynchronusTasks.GetTests;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.domain.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PerformedTest extends AppCompatActivity {

    ListView lista;
    ArrayAdapter<String> adapter;
    private static String token;
    private static int idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performed_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        PerformedTest.token = (String)extras.get("token");
        PerformedTest.idUser = (int)extras.get("idUser");

        lista = (ListView) findViewById(R.id.listView);

        GetTests userTests = new GetTests(PerformedTest.token, PerformedTest.idUser, PerformedTest.this, lista);
        userTests.execute();

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
