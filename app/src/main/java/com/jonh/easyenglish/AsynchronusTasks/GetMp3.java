package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

            if (urlConnection.getResponseCode() == 200){
                FileOutputStream fos = this.actividad.openFileOutput(this.name, Context.MODE_PRIVATE);

                byte [] buffer = new byte[totalSize];
                int byteRead = -1;
                while ((byteRead = is.read(buffer)) != -1){
                    fos.write(buffer,0,byteRead);
                }

                fos.close();
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetMp3.TAG, e.getMessage(), e);
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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(actividad);
            dialogBuilder.setTitle("Token expirado");
            dialogBuilder.setMessage("Ha expirado el token. Debe volver a iniciar sesión.");
            dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(actividad, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    actividad.startActivity(i);
                    actividad.finish();
                    Runtime.getRuntime().exit(0);
                }
            });
            AlertDialog exitAppDialog = dialogBuilder.create();
            exitAppDialog.show();
        }else{
            Toast t = Toast.makeText(actividad,"Descarga no realizada... Codigo: "+res,Toast.LENGTH_SHORT);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
