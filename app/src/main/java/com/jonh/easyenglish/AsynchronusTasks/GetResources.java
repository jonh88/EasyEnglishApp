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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jonh on 18/08/16.
 */
public class GetResources extends APICalls {

    private static final String TAG = "GetResources";
    private ArrayList<String> resources;
    private ListView lvResources;


    public GetResources (String token, int idUser, Activity act, ListView lv, View progress, View container){
        super(idUser, token, act, progress, container);
        this.lvResources = lv;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (Connection.getHost()+"media/resources?id="+super.getIdUser());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
            if (urlConnection.getResponseCode()==200){
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
                TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>(){};
                resources = gson.fromJson(linea,token.getType());
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetResources.TAG, e.getMessage(),e);
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer res) {
        super.showProgress(false);
        if (res == 200){
            String [] data = new String[resources.size()];

            for (int i=0; i<resources.size();i++){
                data[i] = resources.get(i);
            }

            ArrayAdapter<String> adapterList = new ArrayAdapter<String>(super.getActivity(),android.R.layout.simple_list_item_1,data);
            lvResources.setAdapter(adapterList);
        }else if (res == 406){
            super.tokenExpired();
        }else {
            Toast t = Toast.makeText(super.getActivity(),"Ha habido un error al obtener los vocabularios :( Codigo: "+ res, Toast.LENGTH_LONG);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
