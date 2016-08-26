package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
public class CreateUser extends AsyncTask<Void, Void, Integer> {

    private Usuario user;
    private Activity act;

    public CreateUser (Usuario u, Activity act){
        this.user = u;
        this.act = act;
    }

    @Override
    protected Integer doInBackground(Void... params) {

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
            e.printStackTrace();
            return 502;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer result) {

        if (result == 200){
            Toast.makeText(this.act,"Usuario creado.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this.act, LoginActivity.class);
            this.act.startActivity(i);
            this.act.finish();
        }else if (result == 304){
            //ya existe un usuario con ese mail
            Toast.makeText(this.act,"Ya existe un usuario con el mismo e-mail.",Toast.LENGTH_LONG).show();
        }else if (result == 406){
            Toast t = Toast.makeText(act,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(act, LoginActivity.class);
            act.startActivity(i);
            act.finish();
        }else {
            //error en SErver
            Toast.makeText(this.act,"Error creando el usuario... :( Codigo: "+result,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
