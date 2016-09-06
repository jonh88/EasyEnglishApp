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
public class BorrarVocabulario extends APICalls {

    private static final String TAG = "BorrarVocabulario";
    private int  idVoc, pos;
    ListView listView;


    public BorrarVocabulario (String t, int id, View progress, View container, int idVoc, ListView lv, int pos, Activity act){
        super(id, t, act, progress, container);
        this.idVoc = idVoc;
        this.listView = lv;
        this.pos = pos;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        super.showProgress(true);

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"users/"+super.getIdUser()+"/vocabulario/"+this.idVoc+"?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
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
        super.showProgress(false);
        if (res == 200){
            Toast t1 = Toast.makeText(super.getActivity(),"Borrado :)",Toast.LENGTH_SHORT);
            t1.show();
            VocabArrayAdapter adapter = (VocabArrayAdapter) listView.getAdapter();
            Vocabulario v = adapter.getItem(pos);
            adapter.remove(v);
            adapter.notifyDataSetChanged();
        }else if (res == 304) {
            Toast t1 = Toast.makeText(super.getActivity(), "No se ha borrado el vocabulario...", Toast.LENGTH_SHORT);
            t1.show();
        }else if (res == 406){
            super.tokenExpired();
        }else {
            Toast t1 = Toast.makeText(super.getActivity(),"Error borrando vocabulario... :( Codigo: "+res,Toast.LENGTH_SHORT);
            t1.show();
        }
    }

    @Override
    protected void onCancelled() {

    }

}
