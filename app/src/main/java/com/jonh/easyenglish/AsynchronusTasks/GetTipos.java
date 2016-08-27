package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
public class GetTipos extends AsyncTask<Void, Void, List<Tipo>> {

    private static final String TAG = "GetTipos";
    Spinner spin;
    ArrayAdapter<String> adapter;
    int idUser;
    String token;
    Context actividad;

    public GetTipos (Spinner spin, ArrayAdapter<String> adapter, int id, String t, Context act){
        this.spin = spin;
        this.adapter = adapter;
        this.idUser = id;
        this.token = t;
        this.actividad = act;
    }

    @Override
    protected  List<Tipo> doInBackground(Void... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"tipoVoc?id="+this.idUser);
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
                TypeToken<List<Tipo>> token = new TypeToken<List<Tipo>>(){};
                List<Tipo> t = gson.fromJson(linea,token.getType());

                return t;
            }else if (urlConnection.getResponseCode() == 406){
                //token expirado reiniciar app
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
                this.cancel(true);
            }else{
                return null;
            }

            return null;
        } catch (Exception e) {
            Log.e(GetTipos.TAG, e.getMessage(), e);
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final List<Tipo> lTipos) {

        if (lTipos != null){
            String [] tipos = new String[lTipos.size()];

            for (int i=0; i<tipos.length;i++){
                tipos[i]=lTipos.get(i).getType();
            }

            this.adapter = new ArrayAdapter<String>(this.actividad,android.R.layout.simple_list_item_1,tipos);
            this.spin.setAdapter(adapter);
        }else{
            Toast t1 = Toast.makeText(this.actividad,"Error en obteniendo tipos",Toast.LENGTH_SHORT);
            t1.show();
        }
    }

    @Override
    protected void onCancelled() {

    }

}
