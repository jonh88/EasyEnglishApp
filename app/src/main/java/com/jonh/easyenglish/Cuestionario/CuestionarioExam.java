package com.jonh.easyenglish.Cuestionario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jonh.easyenglish.AsynchronusTasks.UpdateCuestionario;
import com.jonh.easyenglish.Audio.Audios;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;
import com.jonh.easyenglish.domain.Pregunta;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuestionarioExam extends AppCompatActivity {

    private static String token;
    private static int idUser;
    private static Integer idCuest;
    private static int fallos;
    private static List<Pregunta> testQ;
    private ArrayList<String> respuestas;
    private Button next;
    private EditText txtPregunta;
    private RadioGroup rButtons;
    private RadioButton rbA;
    private RadioButton rbB;
    private RadioButton rbC;
    private RadioButton rbD;
    private int contador;
    private View progress, container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //TOKEN + IDTEST + idUser + preguntas
        Bundle extras = getIntent().getExtras();
        CuestionarioExam.token = (String)extras.get("token");
        CuestionarioExam.idCuest = (Integer)extras.get("idCuest");
        CuestionarioExam.idUser = (int)extras.get("idUser");
        CuestionarioExam.testQ = (List<Pregunta>)extras.get("preguntas");

        //CONTROLES
        progress = findViewById(R.id.cuestionarioExamProgress);
        container = findViewById(R.id.LinearLayout_textExam);
        next = (Button) findViewById(R.id.btnNextCuestionario);
        txtPregunta = (EditText) findViewById(R.id.txtPregunta);
        rButtons = (RadioGroup)findViewById(R.id.rGroup);
        rbA = (RadioButton) findViewById(R.id.rbA);
        rbB = (RadioButton) findViewById(R.id.rbB);
        rbC = (RadioButton) findViewById(R.id.rbC);
        rbD = (RadioButton) findViewById(R.id.rbD);

        //comienza test
        if (CuestionarioExam.testQ.size() == 1)
            next.setText("CORREGIR");

        contador = 0;
        respuestas = new ArrayList<String>();
        txtPregunta.setText(testQ.get(contador).getPregunta());
        rbA.setText(testQ.get(contador).getRespA());
        rbB.setText(testQ.get(contador).getRespB());
        rbC.setText(testQ.get(contador).getRespC());
        rbD.setText(testQ.get(contador).getRespD());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rButtons.getCheckedRadioButtonId() == -1){
                    Toast t = Toast.makeText(CuestionarioExam.this,"Seleccione una respuesta", Toast.LENGTH_SHORT);
                    t.show();
                }else{
                    if (contador == testQ.size()-1){
                        guardarRespuesta(rButtons.getCheckedRadioButtonId(), respuestas);
                        //corregir
                        corregir(CuestionarioExam.testQ, respuestas, CuestionarioExam.this);
                    }else if (contador < testQ.size()-1){
                        guardarRespuesta(rButtons.getCheckedRadioButtonId(), respuestas);
                        contador++;
                        txtPregunta.setText(testQ.get(contador).getPregunta());
                        rbA.setText(testQ.get(contador).getRespA());
                        rbB.setText(testQ.get(contador).getRespB());
                        rbC.setText(testQ.get(contador).getRespC());
                        rbD.setText(testQ.get(contador).getRespD());
                        if (contador == testQ.size()-1)
                            next.setText("CORREGIR");
                    }
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
                i = new Intent(CuestionarioExam.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (CuestionarioExam.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(CuestionarioExam.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(CuestionarioExam.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(CuestionarioExam.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(CuestionarioExam.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    private void guardarRespuesta(int idSelected, final ArrayList<String> respuestas){
        View rButton = rButtons.findViewById(idSelected);
        int idx = rButtons.indexOfChild(rButton);

        switch (idx){
            case 0: respuestas.add("A");
                    break;
            case 1: respuestas.add("B");
                    break;
            case 2: respuestas.add("C");
                    break;
            case 3: respuestas.add("D");
                    break;
        }
    }

    public void corregir(List<Pregunta> preguntas, ArrayList<String> resp, final Context act){
        int erroneas = 0;
        for(int i =0; i < preguntas.size();i++){
            if (!preguntas.get(i).getRespOK().equals(resp.get(i))){
                erroneas++;
            }
        }
        CuestionarioExam.fallos = erroneas;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(act);
        dialogBuilder.setTitle("Finalizado!!");
        dialogBuilder.setMessage("Has acertado: " + (testQ.size() - erroneas) + " de " + testQ.size());
        dialogBuilder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //update test
                Toast t = Toast.makeText(act,"Guardando resultado...", Toast.LENGTH_SHORT);
                t.show();
                UpdateCuestionario uTest = new UpdateCuestionario(CuestionarioExam.token, CuestionarioExam.idCuest, CuestionarioExam.idUser,
                        CuestionarioExam.fallos, CuestionarioExam.testQ.size(), CuestionarioExam.this,
                        progress, container);
                uTest.execute();
                //finish();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
