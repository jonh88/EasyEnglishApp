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
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.Cuestionario.CuestionarioExam;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Tests.TestExam;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Pregunta;
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
public class GetCuestionarioPreguntas extends APICalls {

    private static final String TAG = "GetCuestionarioPregunta";
    private int idCues;
    private List<Pregunta> preg;


    public GetCuestionarioPreguntas (String token, int idCues, int idUser, Activity act, View progress, View container){
        super(idUser, token, act, progress, container);
        this.idCues = idCues;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"cuestionario/"+this.idCues+"/questions?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());

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
                TypeToken<List<Pregunta>> token = new TypeToken<List<Pregunta>>(){};
                this.preg = gson.fromJson(linea,token.getType());
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetCuestionarioPreguntas.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){
        super.showProgress(false);
        if (resp == 200){
            //llamar actividad testExamn
            Intent i = new Intent(super.getActivity(), CuestionarioExam.class);
            i.putExtra("token",(Serializable)super.getToken());
            i.putExtra("idUser",(Serializable)super.getIdUser());
            i.putExtra("idCuest",(Serializable)idCues);
            i.putExtra("preguntas",(Serializable)this.preg);
            super.getActivity().startActivity(i);

        }else if (resp == 404){
            Toast t = Toast.makeText(super.getActivity(),"No existen preguntas para este test... :(", Toast.LENGTH_LONG);
            t.show();
        } else if (resp == 406) {
            super.tokenExpired();
        }else {
            Toast t = Toast.makeText(super.getActivity(),"Ha habido un error al obtener las preguntas del test... :(", Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}
