package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Tests.TestExam;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by jonh on 17/08/16.
 */
public class GetTestPreguntas extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "GetTestPreguntas";
    private String token;
    private int idTest, idUser;
    private List<Vocabulario> preg;
    private Context actividad;

    public GetTestPreguntas (String t, int idTest, int idUser, Context act){
        this.token = t;
        this.idTest = idTest;
        this.idUser = idUser;
        this.actividad = act;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"test/"+this.idTest+"/questions?id="+this.idUser);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);

            if (urlConnection.getResponseCode() ==200){
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
                TypeToken<List<Vocabulario>> token = new TypeToken<List<Vocabulario>>(){};
                this.preg = gson.fromJson(linea,token.getType());
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetTestPreguntas.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){

        if (resp == 200){
            //llamar actividad testExamn
            Intent i = new Intent(actividad, TestExam.class);
            i.putExtra("token",(Serializable)token);
            i.putExtra("idUser",(Serializable)idUser);
            i.putExtra("idTest",(Serializable)idTest);
            i.putExtra("preguntas",(Serializable)this.preg);
            actividad.startActivity(i);

        }else if (resp == 404){
            Toast t = Toast.makeText(actividad,"No existen preguntas para este test... :(", Toast.LENGTH_LONG);
            t.show();

        } else if (resp == 406) {
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
            Toast t = Toast.makeText(actividad,"Ha habido un error al obtener las preguntas del test... :( Codigo: "+resp, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}

