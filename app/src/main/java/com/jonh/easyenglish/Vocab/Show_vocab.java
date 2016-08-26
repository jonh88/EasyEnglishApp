package com.jonh.easyenglish.Vocab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.jonh.easyenglish.AsynchronusTasks.BorrarVocabulario;
import com.jonh.easyenglish.AsynchronusTasks.GetTipos;
import com.jonh.easyenglish.AsynchronusTasks.GetVocabulariesTipo;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.domain.Vocabulario;

public class Show_vocab extends AppCompatActivity {

    private static String token;
    private static int idUser;
    private Spinner spin;
    private ArrayAdapter<String> adapterSpin;
    private ListView vocs;
    private Vocabulario selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vocab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TOKEN
        Bundle extra = getIntent().getExtras();
        Show_vocab.token = (String) extra.get("token");
        Show_vocab.idUser = (int) extra.get("idUser");

        //CONTROLES
        spin = (Spinner)findViewById(R.id.spnVocabTypeShow);
        vocs = (ListView)findViewById(R.id.lvVocabularies);

        //LLENAR SPIN
        GetTipos fill = new GetTipos(spin, adapterSpin,Show_vocab.idUser,Show_vocab.token, Show_vocab.this);
        fill.execute();

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: GetVocabulariesTipo tipoVerbo = new GetVocabulariesTipo(Show_vocab.idUser,Show_vocab.token,1, vocs,Show_vocab.this);
                        tipoVerbo.execute();
                        break;
                    case 1: GetVocabulariesTipo tipoAdjetivo = new GetVocabulariesTipo(Show_vocab.idUser,Show_vocab.token,3,vocs,Show_vocab.this);
                        tipoAdjetivo.execute();
                        break;
                    case 2: GetVocabulariesTipo tipoSustantivo = new GetVocabulariesTipo(Show_vocab.idUser,Show_vocab.token,4,vocs,Show_vocab.this);
                        tipoSustantivo.execute();
                        break;
                    case 3: GetVocabulariesTipo tipoExpresion = new GetVocabulariesTipo(Show_vocab.idUser,Show_vocab.token,5,vocs,Show_vocab.this);
                        tipoExpresion.execute();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vocs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                selected = (Vocabulario)vocs.getAdapter().getItem(position);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Show_vocab.this);
                dialogBuilder.setTitle("Borrar");
                dialogBuilder.setMessage("¿Desea borrar " + selected.getEnglish() + "?");
                dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //borrar voc
                        BorrarVocabulario delete = new BorrarVocabulario(Show_vocab.token, Show_vocab.idUser,
                                selected.getId(), vocs, position, Show_vocab.this);
                        delete.execute();

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

}
