package com.jonh.easyenglish.Vocab;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.AsynchronusTasks.GetTipos;
import com.jonh.easyenglish.AsynchronusTasks.InsertarVoc;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.domain.Tipo;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Insert_Vocab extends AppCompatActivity {

    private Spinner spin;
    private Button btnSave;
    private ArrayAdapter<String> adapter;
    private static String token;
    private static int idUser;
    private EditText txtEnglish;
    private EditText txtSpanish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__vocab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TOKEN
        Bundle extra = getIntent().getExtras();
        Insert_Vocab.token = (String)extra.get("token");
        Insert_Vocab.idUser = (int)extra.get("idUser");

        //CONTROLES
        txtEnglish = (EditText) findViewById(R.id.txtEnglish);
        txtSpanish = (EditText) findViewById(R.id.txtSpanish);
        spin = (Spinner) findViewById(R.id.spnVocabType);
        btnSave = (Button) findViewById(R.id.btnSave);

        //LLENAR SPINNER
        GetTipos llenarSpin = new GetTipos(spin, adapter,Insert_Vocab.idUser,Insert_Vocab.token, Insert_Vocab.this);
        llenarSpin.execute();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = checkData();
                if (res.equals("Empty")){
                    //mensaje campos vacios
                    Toast toast1 = Toast.makeText(getApplicationContext(),"Campos vacios!!", Toast.LENGTH_SHORT);
                    toast1.show();
                }else{
                    //creo nuevo objeto vocabulario
                    Vocabulario nVoc = new Vocabulario();
                    nVoc.setEnglish(txtEnglish.getText().toString());
                    nVoc.setSpanish(txtSpanish.getText().toString());
                    //id tipo:
                    Tipo tipo = new Tipo();
                    switch (res){
                        case "Verbo": tipo.setId(1);
                                      tipo.setType("Verbo");
                                      break;
                        case "Adjetivo": tipo.setId(3);
                                         tipo.setType("Adjetivo");
                                        break;
                        case "Sustantivo": tipo.setId(4);
                                           tipo.setType("Sustantivo");
                                            break;
                        case "Expresión": tipo.setId(5);
                                            tipo.setType("Expresión");
                                            break;
                    }
                    nVoc.setTipo(tipo);
                    //tarea que guarda el voc
                    InsertarVoc insert = new InsertarVoc(nVoc, Insert_Vocab.idUser, Insert_Vocab.token, txtSpanish, txtEnglish, spin, Insert_Vocab.this);
                    insert.execute();

                }

            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private String checkData (){
        String sEnglish = txtEnglish.getText().toString();
        String sSpanish = txtSpanish.getText().toString();

        if (sEnglish.isEmpty() || sSpanish.isEmpty()){
            return "Empty";
        }else{
            String selected = (String) spin.getSelectedItem();
            return selected;
        }
    }

}
