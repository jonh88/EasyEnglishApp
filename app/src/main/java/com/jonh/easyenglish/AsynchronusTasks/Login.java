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
public class Login extends APICalls {

    private static final String TAG = "Login";
    private final String mEmail;
    private final String mPassword;

    private String token;


    public Login(String email, String password, Activity act, View progress, View container) {
        super(-1, "", act, progress, container);
        this.mEmail = email;
        this.mPassword = password;
    }

    @Override
    protected Integer doInBackground(Void... paradoms) {
       super.showProgress(true);

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
        super.showProgress(false);
        switch (resp){
            case 200:
                //obtener id usuario del token
                TokenManager tm = new TokenManager();
                Intent i = new Intent(super.getActivity(), MainActivity.class);
                i.putExtra("token", (Serializable) token);
                i.putExtra("idUser", (Serializable) tm.getUserFromToken(this.token));
                super.getActivity().startActivity(i);
                super.getActivity().finish();
                break;
            case 403:
                Toast.makeText(super.getActivity(),"Usuario y/o contraseña incorrectos",Toast.LENGTH_LONG).show();
                break;
            case 500:
                Toast.makeText(super.getActivity(),"Error interno en el servidor...",Toast.LENGTH_LONG).show();
                break;
            case 406:
                super.tokenExpired();
                break;
            default:
                Toast.makeText(super.getActivity(),"Error en la aplicación ...",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCancelled() {
        //mAuthTask = null;
        //el metodo setVisibility de un view solo puede ser ejecutado por el hilo ppal
        super.showProgress(false);
    }

}
