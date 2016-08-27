package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 16/08/16.
 */
public class InsertarVoc extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "InsertarVoc";
    Vocabulario voc;
    private int idUser;
    private String token;
    EditText sp, en;
    Spinner spin;
    Context actividad;

    public InsertarVoc (Vocabulario v, int id, String t, EditText spa, EditText eng, Spinner spin, Context act){
        this.voc = v;
        this.idUser = id;
        this.token = t;
        this.sp = spa;
        this.en = eng;
        this.spin = spin;
        this.actividad = act;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            String jsonObject = new Gson().toJson(this.voc);
            url = new URL (Connection.getHost()+"users/"+this.idUser+"/vocabulario?id="+this.idUser);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(jsonObject.getBytes().length); //gets system default chunk size

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonObject.getBytes());
            out.flush();
            out.close();

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(InsertarVoc.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer res) {

        if (res == 200){
            this.sp.setText("");
            this.en.setText("");
            this.spin.setSelection(0);
            Toast t1 = Toast.makeText(this.actividad,"Insertado :)",Toast.LENGTH_SHORT);
            t1.show();
        }else if (res == 304) {
            Toast t1 = Toast.makeText(this.actividad, "Ya insertado", Toast.LENGTH_SHORT);
            t1.show();
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
                    ((Activity)actividad).finish();
                    Runtime.getRuntime().exit(0);
                }
            });
            AlertDialog exitAppDialog = dialogBuilder.create();
            exitAppDialog.show();
        }else {
            Toast t1 = Toast.makeText(this.actividad,"Error insertando vocabulario... :( Codigo: "+res,Toast.LENGTH_SHORT);
            t1.show();
        }
    }

    @Override
    protected void onCancelled() {

    }

}