package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jonh.easyenglish.LoginActivity;
import com.jonh.easyenglish.Util.Connection;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jonh on 18/08/16.
 */
public class GetMp3 extends APICalls {

    private static final String TAG = "GetMP3";
    private String name;
    private ListView listView;

    public GetMp3 (String token, int idUser, String archivo, Activity act, ListView lv, View progress, View container ){
        super(idUser, token, act, progress, container);
        this.name = archivo;
        this.listView = lv;
    }

    @Override
    protected  Integer doInBackground(Void... params) {
        super.showProgress(true);
        URL url;
        HttpURLConnection urlConnection = null;
        int totalSize = 0;

        try {
            url = new URL (Connection.getHost()+"media?id="+super.getIdUser()+"&name="+this.name);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", super.getToken());
            InputStream is = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            totalSize = urlConnection.getContentLength();

            if (urlConnection.getResponseCode() == 200){
                FileOutputStream fos = super.getActivity().openFileOutput(this.name, Context.MODE_PRIVATE);

                byte [] buffer = new byte[totalSize];
                int byteRead = -1;
                while ((byteRead = is.read(buffer)) != -1){
                    fos.write(buffer,0,byteRead);
                }

                fos.close();
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(GetMp3.TAG, e.getMessage(), e);
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(final Integer res) {
        super.showProgress(false);
        if (res == 200){
            //this.setVisibility(View.GONE);
            //this.listView.setVisibility(View.VISIBLE);
            Toast t = Toast.makeText(super.getActivity(),"Descarga realizada",Toast.LENGTH_SHORT);
            t.show();
        }else if (res == 406){
            super.tokenExpired();
        }else{
            Toast t = Toast.makeText(super.getActivity(),"Descarga no realizada... Codigo: "+res,Toast.LENGTH_SHORT);
            t.show();
        }

    }

    @Override
    protected void onCancelled() {

    }
}
