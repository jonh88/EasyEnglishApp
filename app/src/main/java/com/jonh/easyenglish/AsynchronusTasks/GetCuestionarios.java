package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Cuestionario;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonh on 17/08/16.
 */
public class GetCuestionarios extends APICalls {

    private static final String TAG = "GetCuestionarios";
    private List<Cuestionario> cuestionarios;
    ListView lista;

    public GetCuestionarios (String token, int idUser, Activity act, ListView lv, View progress, View container){
        super(idUser, token, act, progress, container);
        this.lista = lv;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+super.getIdUser()+"/cuestionarios?id="+super.getIdUser());
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
                TypeToken<List<Cuestionario>> token = new TypeToken<List<Cuestionario>>(){};
                this.cuestionarios = gson.fromJson(linea,token.getType());
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetCuestionarios.TAG, e.getMessage(),e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute (final Integer resp){
        super.showProgress(false);
        if (resp == 200){
            ArrayList<String> cuestionariosString =  new ArrayList<String>();
            //rellenar el arrayAdapter con los test del usuario
            for (Cuestionario t : this.cuestionarios){
                cuestionariosString.add(t.toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(super.getActivity(),android.R.layout.simple_list_item_1,cuestionariosString);
            lista.setAdapter(adapter);

        }else if (resp == 204){
            Toast t = Toast.makeText(super.getActivity(),"No se ha realizado ningún cuestionario todavía", Toast.LENGTH_LONG);
            t.show();

        } else if (resp == 406) {
            super.tokenExpired();
        }else {
            Toast t = Toast.makeText(super.getActivity(),"Ha habido un error al obtener los cuestionarios realizados... :( Codigo: "+resp, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}
