package com.jonh.easyenglish.Cuestionario;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jonh.easyenglish.AsynchronusTasks.CreateCuestionario;
import com.jonh.easyenglish.AsynchronusTasks.CreateTest;
import com.jonh.easyenglish.Audio.Audios;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;

public class CuestionarioConfig extends AppCompatActivity {

    private CreateCuestionario createCuestionarioTask;
    private  static String token;
    Button create;
    View linearLayout;
    EditText numQuestions;
    View barraProgreso;
    private static int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //TOKEN + IDUSER
        Bundle extras = getIntent().getExtras();
        CuestionarioConfig.token = (String)extras.get("token");
        CuestionarioConfig.idUser = (int)extras.get("idUser");

        //CONTROLES
        numQuestions = (EditText)findViewById(R.id.txtNumQuestionCuestionario);
        linearLayout = (View) findViewById(R.id.LinearLayoutCuestionario);
        barraProgreso = (View)findViewById(R.id.progressCuestionario);
        create = (Button)findViewById(R.id.btnCreateCuestionario);
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
                    createCuestionarioTask = new CreateCuestionario(CuestionarioConfig.token,
                            Integer.valueOf(numQuestions.getText().toString()), CuestionarioConfig.idUser,
                            CuestionarioConfig.this, barraProgreso, linearLayout);
                    createCuestionarioTask.execute();
                    showProgress(true);
//                    finish();
                }

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
            case R.id.mMain:
                i = new Intent(CuestionarioConfig.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (CuestionarioConfig.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(CuestionarioConfig.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(CuestionarioConfig.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(CuestionarioConfig.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(CuestionarioConfig.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    public void showProgress(boolean show){
        this.linearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        this.create.setVisibility(show ? View.GONE : View.VISIBLE);
        this.barraProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
