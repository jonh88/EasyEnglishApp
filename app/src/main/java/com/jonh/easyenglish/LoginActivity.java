package com.jonh.easyenglish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jonh.easyenglish.AsynchronusTasks.Login;
import com.jonh.easyenglish.Util.Connection;
import com.jonh.easyenglish.Util.TokenManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Login mAuthTask;
    // UI references.
    private CheckBox chkRemember;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // CONTROLES
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        chkRemember = (CheckBox)findViewById(R.id.checkBoxRemember);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        bRegister = (Button) findViewById(R.id.email_register_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //ocultar teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //cargar datos mail y contraseña
        getPreferenceData();

        //METODOS CONTROLES
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL || id == R.id.nextIME) {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //ocultar teclado
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected()){
                    attemptLogin();
                }else {
                    Toast.makeText(LoginActivity.this, "No dispones de conexión a internet... :(", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Register.class);
                startActivity(i);

            }
        });

        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    rememeberData();
                else
                    notRemeberData();
            }
        });

        /*config
        Button bSettings = (Button) findViewById(R.id.config_button);
        bSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,Config.class);
                startActivity(i);

            }
        });
        */
    }

    private void rememeberData(){
        String mail = mEmailView.getText().toString().trim();
        String pass = mPasswordView.getText().toString().trim();

        if ((!mail.equals(""))&&(!pass.equals(""))){
            SharedPreferences preferences = getPreferences(0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("checked",chkRemember.isChecked());
            editor.putString("email", mail);
            editor.putString("pass", pass);
            editor.commit();
        }
    }

    private void notRemeberData(){
        SharedPreferences preferences = getPreferences(0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putBoolean("checked", false);
        editor.commit();
    }

    private void getPreferenceData(){
        SharedPreferences preferences = getPreferences(0);
        String mail = preferences.getString("email", "");
        String pass = preferences.getString("pass", "");
        boolean checked = preferences.getBoolean("checked", false);

        mEmailView.setText(mail);
        mPasswordView.setText(pass);
        chkRemember.setChecked(checked);
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new Login(email, password, LoginActivity.this, this.mProgressView, this.mLoginFormView);
            mAuthTask.execute((Void)null);

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

}
