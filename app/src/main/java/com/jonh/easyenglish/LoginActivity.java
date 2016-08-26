package com.jonh.easyenglish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private UserLoginTask mAuthTask = null;
    // UI references.
    private CheckBox chkRemember;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ocultar teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // CONTROLES
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        chkRemember = (CheckBox)findViewById(R.id.checkBoxRemember);

        getPreferenceData();

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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button bRegister = (Button) findViewById(R.id.email_register_button);
        bRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Register.class);
                startActivity(i);

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //config
        Button bSettings = (Button) findViewById(R.id.config_button);
        bSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,Config.class);
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
        if (mAuthTask != null) {
            return;
        }

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
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            //fab.setVisibility(show ? View.GONE : View.VISIBLE);
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            //fab.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    //<Tipo_empezarBackground, Tipo_duranteBackground, Tipo_terminarBackground>
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... paradoms) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL (Connection.getHost()+"authz?user="+this.mEmail+"&pass="+this.mPassword);
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();

                if (code == 200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader reader = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(reader);
                    String linea = null;
                    StringBuilder response = new StringBuilder();
                    while ((linea = br.readLine())!= null){
                        response.append(linea);
                    }

                    linea = response.toString();
                    return linea;
                }else{
                    return "Forbidden";
                }


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }finally {
                urlConnection.disconnect();
            }

        }

        @Override
        protected void onPostExecute(final String token) {
            mAuthTask = null;
            showProgress(false);
            if ((token != null)&&(!token.equals("Forbidden"))) {
                //obtener id usuario del token
                TokenManager tm = new TokenManager();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("token", (Serializable) token);
                i.putExtra("idUser", (Serializable) tm.getUserFromToken(token));
                startActivity(i);
                finish();
            } else if (token.equals("Forbidden")) {
               Toast toast = Toast.makeText(getApplicationContext(),"User or password incorrect",Toast.LENGTH_LONG);
               toast.show();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"Error in server...",Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
