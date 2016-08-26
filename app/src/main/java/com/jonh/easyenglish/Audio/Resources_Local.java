package com.jonh.easyenglish.Audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jonh.easyenglish.R;

import java.util.ArrayList;

public class Resources_Local extends AppCompatActivity {

    private String token;
    private int idUser;
    private ListView lvResources;
    private ArrayAdapter<String> adapterList;
    private String itemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources__local);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TOKEN
        Bundle extras = getIntent().getExtras();
        token = (String)extras.get("token");
        idUser = (int)extras.get("idUser");
        //CONTROLES
        lvResources = (ListView)findViewById(R.id.lvResources_local);

        //ELIMINAR INSTANT-RUN...
        ArrayList<String> ficheros = new ArrayList<>();
        for (int i=0; i< fileList().length; i++){
            ficheros.add(fileList()[i]);
        }
        ficheros.remove(0);

        adapterList =  new ArrayAdapter<String>(Resources_Local.this,android.R.layout.simple_list_item_1,ficheros);
                //new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fileList());

        lvResources.setAdapter(adapterList);

        lvResources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = adapterList.getItem(position);
                Intent i = new Intent(Resources_Local.this,Player.class);
                i.putExtra("fichero",itemSelected);
                startActivity(i);
            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
