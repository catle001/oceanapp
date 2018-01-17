package com.example.user.fypapp.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.fypapp.Presenter.Stemmer;
import com.example.user.fypapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @InjectView(R.id.input_matric)
    EditText _matricText;
    @InjectView(R.id.btn_login)
    Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        changeStatusBarColor();
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "- ON PAUSE -");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "- ON DESTROY -");
    }

    public void login() {
        Log.d(TAG, "LoginActivity");


        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String matric = _matricText.getText().toString();

        if (validate(matric)) {
            onLoginSuccess(matric);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("matric", matric);
            editor.commit();
        } else {
            Toast.makeText(this, "Please enter a valid matriculation number", Toast.LENGTH_SHORT).show();
            onLoginFailed();
            _loginButton.setEnabled(true);
        }
        progressDialog.dismiss();


    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String matric) {
        _loginButton.setEnabled(true);
        PrefManager prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch() && prefManager.getMatric().equals(matric) && prefManager.getQuizDone()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void onLoginFailed() {
        Log.e(TAG, "Login Fail");
        _loginButton.setEnabled(true);
    }

    public boolean validate(String matric) {
        boolean valid = true;


        String regexp = "^[A-z]\\d{7}[A-z]$"; // regexp here

        if (matric.matches(regexp)) {
            return valid;
        }

        return false;

    }

}
