package com.jonh.easyenglish;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jonh.easyenglish.Util.Connection;

public class Config extends AppCompatActivity {

    private EditText url;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CONTROLES
        url = (EditText)findViewById(R.id.txtIP);
        url.setText(Connection.getEndPoint().toString());

        btnSave = (Button)findViewById(R.id.btnSaveConfig);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection.setEndPoint(url.getText().toString());
                Snackbar.make(v, "Settings saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
