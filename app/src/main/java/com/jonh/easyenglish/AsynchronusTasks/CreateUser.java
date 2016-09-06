package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Usuario;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 18/08/16.
 */
public class CreateUser extends APICalls {

    private static final String TAG = "CreateUser";
    private Usuario user;


    public CreateUser (Usuario u, Activity act, View progress, View container){
        super(-1, "", act, progress, container);
        this.user = u;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            String jsonObject = new Gson().toJson(user);

            url = new URL (Connection.getHost()+"users/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(jsonObject.getBytes().length); //gets system default chunk size

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonObject.getBytes());
            out.flush();
            out.close();

            return urlConnection.getResponseCode();

        } catch (Exception e) {
            Log.e(CreateUser.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer result) {
        super.showProgress(false);
        if (result == 200){
            Toast.makeText(super.getActivity(),"Usuario creado.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(super.getActivity(), LoginActivity.class);
            super.getActivity().startActivity(i);
            super.getActivity().finish();
        }else if (result == 304){
            //ya existe un usuario con ese mail
            Toast.makeText(super.getActivity(),"Ya existe un usuario con el mismo e-mail.",Toast.LENGTH_LONG).show();
        }else if (result == 406){
            super.tokenExpired();
        }else {
            //error en SErver
            Toast.makeText(super.getActivity(),"Error creando el usuario... :( Codigo: "+result,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
