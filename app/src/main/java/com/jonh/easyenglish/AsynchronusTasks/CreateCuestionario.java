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
import com.jonh.easyenglish.domain.Cuestionario;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 17/08/16.
 */
public class CreateCuestionario extends APICalls {

    private static final String TAG = "CreateCuestionario";
    int numPreguntas,  idCuest;


    public CreateCuestionario (String token, int numPreguntas, int idUser, Activity act, View progress, View container){
        super(idUser, token, act, progress, container);
        this.numPreguntas = numPreguntas;

    }

    @Override
    protected Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+super.getIdUser()+"/cuestionario?id="+super.getIdUser()+"&num="+this.numPreguntas);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
            urlConnection.setRequestMethod("POST");

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
                Cuestionario c = gson.fromJson(linea,Cuestionario.class);
                this.idCuest = c.getId();
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(CreateCuestionario.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){
        super.showProgress(false);
        if (resp == 200){
            //llamar task getPreguntas;
            GetCuestionarioPreguntas getQuestions = new GetCuestionarioPreguntas(super.getToken(), this.idCuest,
                    super.getIdUser(),super.getActivity(), super.getProgress(), super.getContainer());
            getQuestions.execute();
            super.getActivity().finish();
        }else if (resp == 406){
            super.tokenExpired();
        }else {
            Toast t = Toast.makeText(super.getActivity(), "Ha habido un error al crear el cuestionario... :(. Codigo: " + resp, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}
