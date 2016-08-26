package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 17/08/16.
 */
public class CreateTest extends AsyncTask<Void, Void, Integer> {

    String token;
    int numPreguntas, idUser, idTest;
    Activity actividad;

    public CreateTest (String t, int n, int id, Activity act){
        this.token = t;
        this.numPreguntas = n;
        this.idUser = id;
        this.actividad = act;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+this.idUser+"/test?id="+this.idUser+"&num="+this.numPreguntas);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);
            urlConnection.setRequestMethod("POST");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String linea = null;
            StringBuilder response = new StringBuilder();
            while ((linea = br.readLine())!= null){
                response.append(linea);
            }

            linea = response.toString();
            Gson gson = new Gson();
            Test t = gson.fromJson(linea,Test.class);
            this.idTest = t.getId();

            return urlConnection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){

        if (resp == 200){
            //llamar task getPreguntas;
            GetTestPreguntas getQuestions = new GetTestPreguntas(this.token, this.idTest, this.idUser,this.actividad);
            getQuestions.execute();
            this.actividad.finish();
        }else if (resp == 406){
            Toast t = Toast.makeText(actividad,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(actividad, LoginActivity.class);
            actividad.startActivity(i);
        }else {
            Toast t = Toast.makeText(actividad, "Ha habido un error al crear el test... :(. Codigo: " + resp, Toast.LENGTH_LONG);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}