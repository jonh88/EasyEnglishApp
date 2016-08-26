package com.jonh.easyenglish.Cuestionario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.jonh.easyenglish.R;
import java.io.Serializable;

public class Cuestionario extends AppCompatActivity {

    private Button iNuevo;
    private Button iRealizado;
    String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");

        //CONTROLES
        iNuevo = (Button) findViewById(R.id.btnNewCuestionario);
        iRealizado = (Button)findViewById(R.id.btnPerformedCuestionarios);

        //EVENTOS
        iNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Cuestionario.this, CuestionarioConfig.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

        iRealizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cuestionario.this, PerformedCuestionario.class);
                i.putExtra("token", (Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                finish();
            }
        });

    }

}
