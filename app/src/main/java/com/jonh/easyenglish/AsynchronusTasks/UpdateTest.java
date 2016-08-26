package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Tests.TestsActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.Util.FechaManager;
import com.jonh.easyenglish.domain.Test;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 17/08/16.
 */
public class UpdateTest extends AsyncTask<Void, Void, Integer> {

    private String token;
    private int idUser, idTest, numFallos, numPreguntas;
    private Activity actividad;


    public UpdateTest (String t, int test, int user, int fallos, int preg, Activity act){
        this.token = t;
        this.idTest = test;
        this.idUser = user;
        this.numFallos = fallos;
        this.numPreguntas = preg;
        this.actividad = act;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            FechaManager fm = new FechaManager();
            Test nTest = new Test();
            nTest.setId(this.idTest);
            nTest.setFecha(fm.getNow());
            nTest.setNumPreguntas(this.numPreguntas);
            nTest.setNumFallos(this.numFallos);

            String jsonObject = new Gson().toJson(nTest);

            url = new URL (Connection.getHost()+"test/"+this.idTest+"?id="+this.idUser);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setFixedLengthStreamingMode(jsonObject.getBytes().length); //gets system default chunk size

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonObject.getBytes());
            out.flush();
            out.close();

            return urlConnection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer result) {

        if ((result == -1)||(result == 500)){
            Toast t = Toast.makeText(actividad,"Ha habido un error... :(", Toast.LENGTH_LONG);
            t.show();

            Intent i = new Intent(actividad, TestsActivity.class);
            i.putExtra("token", (Serializable) this.token);
            i.putExtra("idUser", (Serializable) this.idUser);
            actividad.startActivity(i);
            actividad.finish();
        }else if (result == 406){
            Toast t = Toast.makeText(actividad,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(actividad, LoginActivity.class);
            actividad.startActivity(i);
            actividad.finish();
        }else if (result == 200){
            Intent i = new Intent(actividad, TestsActivity.class);
            i.putExtra("token", (Serializable) this.token);
            i.putExtra("idUser", (Serializable) this.idUser);
            actividad.startActivity(i);
            actividad.finish();
        }else{
            Toast t = Toast.makeText(actividad,"Error actualizando Test. Codigo: "+result, Toast.LENGTH_LONG);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}