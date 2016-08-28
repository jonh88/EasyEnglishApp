package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jonh.easyenglish.MainActivity;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.Util.TokenManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonh on 28/08/16.
 */
public class Login extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "Login";
    private final String mEmail;
    private final String mPassword;
    private Activity actividad;
    private String token;
    private static View pBar;
    private static View container;

    public Login(String email, String password, Activity act, View progresBar, View container) {
        this.mEmail = email;
        this.mPassword = password;
        this.actividad = act;
        this.pBar = progresBar;
        this.container = container;
    }

    @Override
    protected Integer doInBackground(Void... paradoms) {
        //el metodo setVisibility de un view solo puede ser ejecutado por el hilo ppal
        actividad.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Login.pBar.setVisibility(View.VISIBLE);
                Login.container.setVisibility(View.GONE);
            }
        });

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL (Connection.getHost()+"authz?user="+this.mEmail+"&pass="+this.mPassword);
            urlConnection = (HttpURLConnection) url.openConnection();

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
                this.token = linea;
            }

            return urlConnection.getResponseCode();
        } catch (Exception e) {
            Log.e(Login.TAG, e.getMessage(), e);
            return -1;
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(final Integer resp) {
        //mAuthTask = null;
        //el metodo setVisibility de un view solo puede ser ejecutado por el hilo ppal
        actividad.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Login.pBar.setVisibility(View.GONE);
                Login.container.setVisibility(View.VISIBLE);
            }
        });
        switch (resp){
            case 200:
                //obtener id usuario del token
                TokenManager tm = new TokenManager();
                Intent i = new Intent(actividad, MainActivity.class);
                i.putExtra("token", (Serializable) token);
                i.putExtra("idUser", (Serializable) tm.getUserFromToken(this.token));
                actividad.startActivity(i);
                actividad.finish();
                break;
            case 403:
                Toast.makeText(actividad,"Usuario y/o contraseña incorrectos",Toast.LENGTH_LONG).show();
                break;
            case 500:
                Toast.makeText(actividad,"Error interno en el servidor...",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(actividad,"Error en la aplicación ...",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCancelled() {
        //mAuthTask = null;
        //el metodo setVisibility de un view solo puede ser ejecutado por el hilo ppal
        actividad.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Login.pBar.setVisibility(View.GONE);
                Login.container.setVisibility(View.VISIBLE);
            }
        });
    }

}
