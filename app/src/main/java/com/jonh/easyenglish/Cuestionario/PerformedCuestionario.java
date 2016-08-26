package com.jonh.easyenglish.Cuestionario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jonh.easyenglish.AsynchronusTasks.GetCuestionarios;
import com.jonh.easyenglish.R;

public class PerformedCuestionario extends AppCompatActivity {

    ListView lista;
    ArrayAdapter<String> adapter;
    private static String token;
    private static int idUser;

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
        lista = (ListView) findViewById(R.id.listViewCuestionario);

        GetCuestionarios userTests = new GetCuestionarios(PerformedCuestionario.token, PerformedCuestionario.idUser, PerformedCuestionario.this, lista);
        userTests.execute();

    }

}
