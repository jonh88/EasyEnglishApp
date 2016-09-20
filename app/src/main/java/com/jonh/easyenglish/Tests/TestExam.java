package com.jonh.easyenglish.Tests;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.AsynchronusTasks.UpdateTest;
import com.jonh.easyenglish.Audio.Audios;
import com.jonh.easyenglish.Cuestionario.Cuestionario;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Util.FechaManager;
import com.jonh.easyenglish.Vocab.Vocabulary;
import com.jonh.easyenglish.domain.Vocabulario;
import com.jonh.easyenglish.domain.Test;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestExam extends AppCompatActivity {

    private static String token;
    private static int idUser;
    private static Integer idTest;
    private static int fallos;
    private static List<Vocabulario> testQ;
    private ArrayList<String> respuestas;
    private Button next;
    private int contador;
    private EditText txtIdioma, txtTraducir;
    private View progress, container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //CONTROLES
        next = (Button) findViewById(R.id.btnNext);
        txtIdioma = (EditText)findViewById(R.id.txtIdioma);
        txtTraducir = (EditText)findViewById(R.id.txtTraducir);
        progress = findViewById(R.id.progressTestExam);
        container= findViewById(R.id.LinearLayout_textExam);

        setSupportActionBar(toolbar);

        //TOKEN + IDTEST + idUser + preguntas
        Bundle extras = getIntent().getExtras();
        TestExam.token = (String)extras.get("token");
        TestExam.idTest = (Integer)extras.get("idTest");
        TestExam.idUser = (int)extras.get("idUser");
        TestExam.testQ = (List<Vocabulario>)extras.get("preguntas");

        //comienza test
        if (TestExam.testQ.size() == 1)
            next.setText("CORREGIR");

        contador = 0;
        respuestas = new ArrayList<String>();
        txtIdioma.setText(testQ.get(contador).getEnglish());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    1-guardo la respuesta, vacio el txtTraducir, relleno el txtIdioma
                    2-aumento la progres bar
                 */
                if (contador == testQ.size()-1){
                    respuestas.add(txtTraducir.getText().toString());
                    txtTraducir.setText("");
                    //corregir
                    corregir(TestExam.testQ, respuestas, TestExam.this);
                }else if (contador < testQ.size()-1){
                    respuestas.add(txtTraducir.getText().toString());
                    txtTraducir.setText("");
                    contador++;
                    txtIdioma.setText(testQ.get(contador).getEnglish());
                    if (contador == testQ.size()-1)
                        next.setText("CORREGIR");
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                i = new Intent(TestExam.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (TestExam.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(TestExam.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(TestExam.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(TestExam.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(TestExam.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    public void corregir(List<Vocabulario> preguntas, ArrayList<String> resp, final Context act){
        int erroneas = 0;
        for(int i =0; i < preguntas.size();i++){
            if (!preguntas.get(i).getSpanish().equals(resp.get(i))){
                erroneas++;
            }
        }
        TestExam.fallos = erroneas;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(act);
        dialogBuilder.setTitle("Finalizado!!");
        dialogBuilder.setMessage("Has acertado: " + (testQ.size() - erroneas) + " de " + testQ.size());
        dialogBuilder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //update test
                Toast t = Toast.makeText(act,"Guardando resultado...", Toast.LENGTH_SHORT);
                t.show();
                UpdateTest uTest = new UpdateTest(TestExam.token, TestExam.idTest, TestExam.idUser, TestExam.fallos, TestExam.testQ.size(), TestExam.this, progress, container);
                uTest.execute();
                //finish();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}


