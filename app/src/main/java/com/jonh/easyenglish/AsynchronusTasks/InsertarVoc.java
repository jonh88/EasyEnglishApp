package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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
public class InsertarVoc extends APICalls {

    private static final String TAG = "InsertarVoc";
    Vocabulario voc;
    EditText sp, en;
    Spinner spin;


    public InsertarVoc (Vocabulario v, int idUser, String token, EditText spa, EditText eng, Spinner spin, Activity act, View progress, View container){
        super(idUser, token, act, progress, container);
        this.voc = v;
        this.sp = spa;
        this.en = eng;
        this.spin = spin;

    }

    @Override
    protected  Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            String jsonObject = new Gson().toJson(this.voc);
            url = new URL (Connection.getHost()+"users/"+super.getIdUser()+"/vocabulario?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
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
        super.showProgress(false);
        if (res == 200){
            this.sp.setText("");
            this.en.setText("");
            this.spin.setSelection(0);
            Toast t1 = Toast.makeText(super.getActivity(),"Insertado :)",Toast.LENGTH_SHORT);
            t1.show();
        }else if (res == 304) {
            Toast t1 = Toast.makeText(super.getActivity(), "Ya insertado", Toast.LENGTH_SHORT);
            t1.show();
        }else if (res == 406){
            super.tokenExpired();
        }else {
            Toast.makeText(super.getActivity(),"Error insertando vocabulario... :( Codigo: "+res,Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCancelled() {

    }

}