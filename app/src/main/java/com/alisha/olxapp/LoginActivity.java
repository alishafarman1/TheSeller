package com.alisha.olxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ProgressBar loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,DashboardActivity.class));
            finish();
        }
        loader = findViewById(R.id.showLoading);
        Button suBtn = findViewById(R.id.suBtn);
        final EditText uname = findViewById(R.id.uname);
        final EditText password = findViewById(R.id.password);

        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        Button siBtn = findViewById(R.id.siBtn);
        siBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uname.getText().toString().isEmpty()) {
                    uname.setError("Please enter username");
                    return;
                }
                uname.setError(null);

                if (!uname.getText().toString().contains("@")) {
                    uname.setError("Please enter valid email");
                    return;
                }
                uname.setError(null);

                if (password.getText().toString().isEmpty()) {
                    password.setError("Please enter password");
                    return;
                }
                password.setError(null);
                signIn(uname.getText().toString(), password.getText().toString());
            }
        });
    }


    void gotoDash() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    void signIn(String uname, final String password) {
        loader.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uname,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    gotoDash();
                }else{
                    showMessage("Sign In Error",task.getException().getMessage());
                }
            }
        });

    }


    void showMessage(String title, String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, null).show();
    }
}
