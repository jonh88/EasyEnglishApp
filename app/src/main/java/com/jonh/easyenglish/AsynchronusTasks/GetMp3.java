package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jonh on 18/08/16.
 */
public class GetMp3 extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "GetMP3";
    private String token;
    private int idUser;
    private String name;
    private ProgressBar progressBar;
    private ListView listView;
    Activity actividad;

    public GetMp3 (String t, int idUser, String archivo, Activity act, ProgressBar pbar, ListView lv){
        this.actividad = act;
        this.token = t;
        this.name = archivo;
        this.idUser = idUser;
        this.progressBar = pbar;
        this.listView = lv;
    }

    @Override
    protected  Integer doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;
        int totalSize = 0;

        try {
            url = new URL (Connection.getHost()+"media?id="+this.idUser+"&name="+this.name);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);
            InputStream is = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            totalSize = urlConnection.getContentLength();

            FileOutputStream fos = this.actividad.openFileOutput(this.name, Context.MODE_PRIVATE);

            byte [] buffer = new byte[totalSize];
            int byteRead = -1;
            while ((byteRead = is.read(buffer)) != -1){
                fos.write(buffer,0,byteRead);
            }

            fos.close();

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetMp3.TAG, e.getMessage());
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer res) {

        if (res == 200){
            this.progressBar.setVisibility(View.GONE);
            this.listView.setVisibility(View.VISIBLE);
            Toast t = Toast.makeText(actividad,"Descarga realizada",Toast.LENGTH_SHORT);
            t.show();
        }else if (res == 406){
            Toast t = Toast.makeText(actividad,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(actividad, LoginActivity.class);
            actividad.startActivity(i);
            actividad.finish();
        }else{
            Toast t = Toast.makeText(actividad,"Descarga no realizada... Codigo: "+res,Toast.LENGTH_SHORT);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
