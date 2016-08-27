package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Tests.TestExam;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Test;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonh on 17/08/16.
 */
public class GetTests extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "GetTests";
    private String token;
    private int idUser;
    private List<Test> tests;
    private Activity actividad;
    ListView lista;

    public GetTests (String t, int idUser, Activity act, ListView lv){
        this.token = t;
        this.idUser = idUser;
        this.actividad = act;
        this.lista = lv;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+this.idUser+"/tests?id="+this.idUser);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);

            if (urlConnection.getResponseCode() == 200){
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
                TypeToken<List<Test>> token = new TypeToken<List<Test>>(){};
                this.tests = gson.fromJson(linea,token.getType());
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetTests.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){

        if (resp == 200){
            ArrayList<String> testsString =  new ArrayList<String>();
            //rellenar el arrayAdapter con los test del usuario
            for (Test t : this.tests){
                testsString.add(t.toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(actividad,android.R.layout.simple_list_item_1,testsString);
            lista.setAdapter(adapter);

        }else if (resp == 204){
            Toast t = Toast.makeText(actividad,"No se ha realizado ningún test todavía", Toast.LENGTH_LONG);
            t.show();
        } else if (resp == 406) {
            Toast t = Toast.makeText(actividad,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(actividad, LoginActivity.class);
            actividad.startActivity(i);
        }else {
            Toast t = Toast.makeText(actividad, "Ha habido un error al obtener los tests realizados... :( Codigo: " + resp, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}