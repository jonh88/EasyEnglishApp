package com.jonh.easyenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jonh.easyenglish.AsynchronusTasks.CreateUser;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.domain.Usuario;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApe;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtPass_;
    private EditText emptyView;
    private EditText samePass;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CONTROLES
        btnReg = (Button)findViewById(R.id.btnRegister);
        txtNombre = (EditText)findViewById(R.id.txtNombreReg);
        txtApe = (EditText)findViewById(R.id.txtApeReg);
        txtEmail = (EditText)findViewById(R.id.txtEmailReg);
        txtPass = (EditText)findViewById(R.id.txtPassReg);
        txtPass_ = (EditText)findViewById(R.id.txtPassRepReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //si no hay datos vacios, la passwords coinciden y el mail es válido
                if (checkEmptyData() && checkSamePass() && checkEmail()){
                    //crear instancia usuario
                    Usuario nUser = new Usuario(txtNombre.getText().toString(),txtApe.getText().toString(),
                            txtEmail.getText().toString(),txtPass.getText().toString());
                    //llamada REST
                    CreateUser restUser = new CreateUser(nUser, Register.this);
                    restUser.execute();
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private boolean checkEmptyData() {

        txtNombre.setError(null);
        txtApe.setError(null);
        txtEmail.setError(null);
        txtPass.setError(null);
        txtPass_.setError(null);

        if (txtNombre.getText().toString().equals("")) {
            txtNombre.setError("This field cannot be empty.");
            txtNombre.requestFocus();
            return false;
        } else if (txtApe.getText().toString().equals("")) {
            txtApe.setError("This field cannot be empty.");
            txtApe.requestFocus();
            return false;
        } else if (txtEmail.getText().toString().equals("")) {
            txtEmail.setError("This field cannot be empty.");
            txtEmail.requestFocus();
            return false;
        }else if (txtPass.getText().toString().equals("")) {
            txtPass.setError("This field cannot be empty.");
            txtPass.requestFocus();
            return false;
        }else if (txtPass_.getText().toString().equals("")) {
            txtPass_.setError("This field cannot be empty.");
            txtPass_.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkSamePass(){
        if (!txtPass.getText().toString().equals(txtPass_.getText().toString()) || txtPass.getText().length()<4){
            txtPass_.setError("The passwords have to be the same.");
            txtPass_.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkEmail(){
        if (!txtEmail.getText().toString().contains("@")){
            txtEmail.setError(null);
            txtEmail.setError("Invalid email.");
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

}
