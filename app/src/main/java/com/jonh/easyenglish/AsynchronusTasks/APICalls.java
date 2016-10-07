package com.jonh.easyenglish.AsynchronusTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.jonh.easyenglish.LoginActivity;

/**
 * Created by jonh on 31/08/16.
 */
public abstract class APICalls extends AsyncTask<Void, Void, Integer>{
    private int idUser;
    private String token;
    private Activity actividad;
    private static View pBar;
    private static View container;

    public APICalls (int id, String t, Activity act, View progress, View container){
        this.idUser = id;
        this.token = t;
        this.actividad = act;
        this.pBar = progress;
        this.container = container;
    }

    public int getIdUser(){ return this.idUser;}
    public String getToken(){ return this.token;}
    public Activity getActivity() { return this.actividad;}
    public View getProgress() { return this.pBar;}
    public View getContainer() { return this.container;}

    public void tokenExpired (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(actividad);
        dialogBuilder.setTitle("Sesión caducada");
        dialogBuilder.setMessage("Se ha caducado la sesión, debe volver a entrar al sistema.");
        AlertDialog.Builder builder = dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(actividad, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                actividad.startActivity(i);
                actividad.finish();
            }
        });

        AlertDialog exitAppDialog = dialogBuilder.create();
        exitAppDialog.show();
    }

    public void showProgress(final boolean show){
        actividad.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                if (show) {
                    APICalls.pBar.setVisibility(View.VISIBLE);
                    APICalls.container.setVisibility(View.GONE);
                }else{
                    APICalls.pBar.setVisibility(View.GONE);
                    APICalls.container.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void errorServer(){

    }

}
