package com.jonh.easyenglish.Audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jonh.easyenglish.Cuestionario.Cuestionario;
import com.jonh.easyenglish.GrammarView;
import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.R;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Vocab.Vocabulary;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    private Button play;
    private Button pause;
    private SeekBar seek;
    private double inicio = 0;
    private double total;
    private String fichero;
    private String ruta;
    private MediaPlayer mp;
    private TextView txtInicio;
    private TextView txtDuration;
    private TextView txtTitle;
    private Handler myHandler = new Handler();
    private String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        fichero =(String)extras.get("fichero");
        token = (String) extras.get("token");
        idUser = (int) extras.get("idUser");

        ruta = getFilesDir().getAbsolutePath()+"/"+fichero;

        //CONTROLES
        play = (Button)findViewById(R.id.btnPlay);
        pause = (Button)findViewById(R.id.btnPause);
        seek = (SeekBar)findViewById(R.id.seekBar);
        txtInicio = (TextView)findViewById(R.id.txtInicio);
        txtDuration = (TextView)findViewById(R.id.txtTotal);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtTitle.setText(fichero);
        try {
            mp = new MediaPlayer();
            mp.setDataSource(ruta);
            mp.prepare();
        }catch (Exception e){
            Toast t = Toast.makeText(Player.this,"Error",Toast.LENGTH_SHORT);
            t.show();
        }

        //tiempos de la cancion y play
        inicio = mp.getCurrentPosition();
        total = mp.getDuration();

        txtInicio.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) inicio),
                TimeUnit.MILLISECONDS.toSeconds((long) inicio) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) inicio))));

        txtDuration.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) total),
                TimeUnit.MILLISECONDS.toSeconds((long) total) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) total)))
        );

        //barra de progreso de la reproduccion
        seek.setMax((int) total);
        seek.setProgress((int)inicio);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()){
                    mp.start();
                    myHandler.postDelayed(UpdateSongTime,100);
                    //Toast t = Toast.makeText(Player.this,"Reproduciendo...",Toast.LENGTH_SHORT);
                    //t.show();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()){
                    mp.pause();
                    //Toast t = Toast.makeText(Player.this,"Pausado",Toast.LENGTH_SHORT);
                    //t.show();
                }
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int current = seek.getProgress();
                mp.seekTo(current);
            }
        });

        //para que no salga la flecha en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
                i = new Intent(Player.this, MainActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mGramatica:
                i = new Intent (Player.this, GrammarView.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mTest:
                i = new Intent(Player.this, TestsActivity.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mVocabulario:
                i = new Intent(Player.this, Vocabulary.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mCuestionario:
                i = new Intent(Player.this, Cuestionario.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            case R.id.mAudio:
                i = new Intent(Player.this,Audios.class);
                i.putExtra("token",(Serializable)token);
                i.putExtra("idUser",(Serializable)idUser);
                startActivity(i);
                return true;
            default: return true;
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            inicio = mp.getCurrentPosition();
            txtInicio.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) inicio),
                            TimeUnit.MILLISECONDS.toSeconds((long) inicio) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) inicio)))
            );
            seek.setProgress((int)inicio);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed(){
        try {
            this.mp.stop();
        }catch (Exception e){
            Toast.makeText(Player.this, "Error parando la reproduccion... :(", Toast.LENGTH_LONG).show();
        }
        super.onBackPressed();
    }

}
