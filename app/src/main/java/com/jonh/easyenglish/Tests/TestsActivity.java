package com.jonh.easyenglish.Tests;

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
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;

public class TestsActivity extends AppCompatActivity {

    private Button iNew;
    private Button iPerformed;
    String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");

        //CONTROLES
        iPerformed = (Button) findViewById(R.id.btnPerformedTest);
        iNew = (Button)findViewById(R.id.btnNewTest);

        //EVENTOS
        iNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestsActivity.this, TestConfig.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        iPerformed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestsActivity.this, PerformedTest.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        SharedPreferences preferences = getPreferences(0);
        boolean msg = preferences.getBoolean("expTest", true);

        if (msg){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestsActivity.this);
            dialogBuilder.setTitle("Ayuda");
            dialogBuilder.setMessage("Puedes crear nuevos tests a partir de los vocabularios que hayas" +
                    " insertado o revisar los que hayas realizado.");
            dialogBuilder.setPositiveButton(android.R.string.ok, null);
            dialogBuilder.setNeutralButton("No mostrar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = getPreferences(0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("expTest");
                    editor.putBoolean("expTest", false);
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
                i = new Intent(TestsActivity.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (TestsActivity.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            /*case R.id.mTest:
                i = new Intent(TestsActivity.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;*/
            case R.id.mVocabulario:
                i = new Intent(TestsActivity.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(TestsActivity.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(TestsActivity.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState){
       // savedInstanceState.putString("token", token.getToken());
        //savedInstanceState.putInt("id", token.getIdUsr());
        super.onSaveInstanceState(savedInstanceState);

    }
}
