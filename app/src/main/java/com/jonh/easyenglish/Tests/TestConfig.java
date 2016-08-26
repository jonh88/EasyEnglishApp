package com.jonh.easyenglish.Tests;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.AsynchronusTasks.CreateTest;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.domain.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TestConfig extends AppCompatActivity {

    private CreateTest createTestTask;

    private  static String token;
    Button create;
    View linearLayout;
    EditText numQuestions;
    View barraProgreso;
    private static int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TOKEN + IDUSER
        Bundle extras = getIntent().getExtras();
        TestConfig.token = (String)extras.get("token");
        TestConfig.idUser = (int)extras.get("idUser");

        //CONTROLES
        numQuestions = (EditText)findViewById(R.id.txtNumQuestion);
        linearLayout = (View) findViewById(R.id.LinearLayout);
        barraProgreso = (View)findViewById(R.id.progress);
        create = (Button)findViewById(R.id.btnCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = numQuestions.getText().toString();
                if (num.equals("0") || num.equals("")){
                    //no se puede crear un test con 0 preguntas
                    Toast t = Toast.makeText(getApplicationContext(),"Insertar un número válido de preguntas", Toast.LENGTH_LONG);
                    t.show();
                }else{
                    //CREAR TEST
                    createTestTask = new CreateTest(TestConfig.token, Integer.valueOf(numQuestions.getText().toString()), TestConfig.idUser, TestConfig.this);
                    createTestTask.execute();
                    showProgress(true);
//                    finish();
                }

            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void showProgress(boolean show){
        this.linearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        this.create.setVisibility(show ? View.GONE : View.VISIBLE);
        this.barraProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
    }


}
