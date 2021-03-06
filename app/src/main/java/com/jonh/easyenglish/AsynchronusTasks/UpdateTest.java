package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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
public class UpdateTest extends APICalls {

    private static final String TAG = "UpdateTest";
    private int idTest, numFallos, numPreguntas;

    public UpdateTest (String token, int test, int idUser, int fallos, int preg, Activity act, View progress, View container){
        super(idUser, token, act, progress, container);
        this.idTest = test;
        this.numFallos = fallos;
        this.numPreguntas = preg;

    }

    @Override
    protected Integer doInBackground(Void... params) {
        super.showProgress(true);
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

            url = new URL (Connection.getHost()+"test/"+this.idTest+"?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
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
            Log.e(UpdateTest.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer result) {
        super.showProgress(false);
        if ((result == -1)||(result == 500)){
            Toast t = Toast.makeText(super.getActivity(),"Ha habido un error... :(", Toast.LENGTH_LONG);
            t.show();

            Intent i = new Intent(super.getActivity(), TestsActivity.class);
            i.putExtra("token", (Serializable) super.getToken());
            i.putExtra("idUser", (Serializable) super.getIdUser());
            super.getActivity().startActivity(i);
            super.getActivity().finish();
        }else if (result == 406){
            super.tokenExpired();
        }else if (result == 200){
            Intent i = new Intent(super.getActivity(), TestsActivity.class);
            i.putExtra("token", (Serializable) super.getToken());
            i.putExtra("idUser", (Serializable) super.getIdUser());
            super.getActivity().startActivity(i);
            super.getActivity().finish();
        }else{
            Toast.makeText(super.getActivity(),"Error actualizando Test. Codigo: "+result, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
