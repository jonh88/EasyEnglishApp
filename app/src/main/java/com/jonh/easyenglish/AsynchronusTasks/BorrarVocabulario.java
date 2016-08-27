package com.jonh.easyenglish.AsynchronusTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.Util.VocabArrayAdapter;
import com.jonh.easyenglish.domain.Vocabulario;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 19/08/16.
 */
public class BorrarVocabulario extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "BorrarVocabulario";
    private int idUser, idVoc, pos;
    private String token;
    Context actividad;
    ListView listView;


    public BorrarVocabulario (String t, int id, int idVoc, ListView lv, int pos, Context act){
        this.idUser = id;
        this.idVoc = idVoc;
        this.token = t;
        this.actividad = act;
        this.listView = lv;
        this.pos = pos;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+this.idUser+"/vocabulario/"+this.idVoc+"?id="+this.idUser);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", this.token);
            urlConnection.setRequestMethod("DELETE");

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(BorrarVocabulario.TAG, e.getMessage(),e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer res) {

        if (res == 200){
            Toast t1 = Toast.makeText(this.actividad,"Borrado :)",Toast.LENGTH_SHORT);
            t1.show();
            VocabArrayAdapter adapter = (VocabArrayAdapter) listView.getAdapter();
            Vocabulario v = adapter.getItem(pos);
            adapter.remove(v);
            adapter.notifyDataSetChanged();
        }else if (res == 304) {
            Toast t1 = Toast.makeText(this.actividad, "No se ha borrado el vocabulario...", Toast.LENGTH_SHORT);
            t1.show();
        }else if (res == 406){
            Toast t = Toast.makeText(actividad,"Se ha caducado el token...", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(actividad, LoginActivity.class);
            actividad.startActivity(i);
        }else {
            Toast t1 = Toast.makeText(this.actividad,"Error borrando vocabulario... :( Codigo: "+res,Toast.LENGTH_SHORT);
            t1.show();
        }
    }

    @Override
    protected void onCancelled() {

    }

}
