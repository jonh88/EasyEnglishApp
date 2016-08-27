package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.Util.VocabArrayAdapter;
import com.jonh.easyenglish.domain.Vocabulario;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonh on 16/08/16.
 */
public class GetVocabulariesTipo extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "GetVocTipos";
    String token;
    int idUser;
    int idTipo;
    private ArrayList<Vocabulario> lVocs;
    Activity actividad;
    ListView listview;

    public GetVocabulariesTipo (int id, String t, int tipo, ListView lv, Activity act){
        this.token = t;
        this.idTipo = tipo;
        this.idUser = id;
        this.actividad = act;
        this.listview = lv;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+this.idUser+"/vocabularies?id="+this.idUser);
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
                TypeToken<List<Vocabulario>> token = new TypeToken<List<Vocabulario>>(){};
                List<Vocabulario> t = gson.fromJson(linea,token.getType());
                ArrayList<Vocabulario> res = new ArrayList<Vocabulario>();
                //devuelvo solo los voc que tengan el tipo requerido
                for (Vocabulario voc : t){
                    if (voc.getTipo().getId() == this.idTipo)
                        res.add(voc);
                }
                this.lVocs = res;
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetVocabulariesTipo.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer resp) {

        if (resp == 200){
            VocabArrayAdapter customAdapter = new VocabArrayAdapter(actividad,lVocs);
            listview.setAdapter(customAdapter);
        }else if (resp == 406){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(actividad);
            dialogBuilder.setTitle("Token expirado");
            dialogBuilder.setMessage("Ha expirado el token. Debe volver a iniciar sesión.");
            dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(actividad, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    actividad.startActivity(i);
                    actividad.finish();
                    Runtime.getRuntime().exit(0);
                }
            });
            AlertDialog exitAppDialog = dialogBuilder.create();
            exitAppDialog.show();
        }else if (resp == 204){
            Toast.makeText(actividad, "No existen vocabularios de este tipo.", Toast.LENGTH_SHORT).show();
        }else{
            Toast t = Toast.makeText(actividad,"Ha habido un error al obtener los vocabularios :( Codigo: "+resp, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    protected void onCancelled() {

    }

}
