package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Tipo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by jonh on 16/08/16.
 */
public class GetTipos extends APICalls {

    private static final String TAG = "GetTipos";
    Spinner spin;
    private String [] tipos;

    public GetTipos (Spinner spin, int idUser, String token, Activity act, View progress, View container){
        super(idUser, token, act, progress, container);
        this.spin = spin;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"tipoVoc?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());

            if (urlConnection.getResponseCode() == 200) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String linea = null;
                StringBuilder response = new StringBuilder();
                while ((linea = br.readLine()) != null) {
                    response.append(linea);
                }

                linea = response.toString();
                Gson gson = new Gson();
                TypeToken<List<Tipo>> token = new TypeToken<List<Tipo>>() {
                };
                List<Tipo> lTipos = gson.fromJson(linea, token.getType());

                this.tipos = new String[lTipos.size()];
                for (int i = 0; i < this.tipos.length; i++) {
                    this.tipos[i] = lTipos.get(i).getType();
                }
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetTipos.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer resp) {
        super.showProgress(false);
        switch (resp){
            case 200:
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(super.getActivity(),android.R.layout.simple_list_item_1, this.tipos);
                this.spin.setAdapter(adapter);
                break;
            case 406:
                super.tokenExpired();
                break;
            case 500:
                Toast.makeText(super.getActivity(),"Error en el servidor... :(",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(super.getActivity(),"Error en la aplicacion... :(",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onCancelled() {

    }

}
