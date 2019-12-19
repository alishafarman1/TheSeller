package com.alisha.olxapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton ppImageButton;
    Bitmap pp;

    void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText name = findViewById(R.id.name);
        final EditText uname = findViewById(R.id.uname);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText cpassword = findViewById(R.id.cpassword);
        final Button signUpBtn = findViewById(R.id.signUpBtn);
        ppImageButton = findViewById(R.id.ppImageButton);

        ppImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pp == null){
                    showMessage("Validation Error","Please select Image");
                    return;
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Please enter name");
                    return;
                }
                name.setError(null);

                if(uname.getText().toString().isEmpty()){
                    uname.setError("Please enter username");
                    return;
                }
                uname.setError(null);

                if (uname.getText().toString().contains(".") || uname.getText().toString().contains("#") || uname.getText().toString().contains("$") || uname.getText().toString().contains("[") || uname.getText().toString().contains("]")) {
                    uname.setError("Please enter a valid username");
                    return;
                }
                uname.setError(null);

                if(email.getText().toString().isEmpty()){
                    email.setError("Please enter Email");
                    return;
                }
                email.setError(null);

                if(password.getText().toString().isEmpty()){
                    password.setError("Please enter Password");
                    return;
                }
                password.setError(null);

                if(password.getText().toString().length() < 6){
                    password.setError("Password Must be at least 6 characters long!");
                    return;
                }
                password.setError(null);

                if(cpassword.getText().toString().isEmpty()){
                    cpassword.setError("Please Confirm Password");
                    return;
                }
                cpassword.setError(null);

                if(!password.getText().toString().equals(cpassword.getText().toString())){
                    cpassword.setError("Confirm Password Must Match Password");
                    return;
                }
                cpassword.setError(null);
                signUp(name.getText().toString(),uname.getText().toString(),email.getText().toString(),password.getText().toString());

            }
        });
    }

    void showMessage(String title, String message) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, null).show();
    }

    void signUp(final String name, final String uname, final String email, final String password){
        Log.e("e","e");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(task.getResult().getUser().getUid());
                    ref.child("name").setValue(name);
                    ref.child("username").setValue(uname);
                    ref.child("email").setValue(email);
                    ref.child("password").setValue(password);
                    ref.child("image").setValue(ImageUtil.convert(pp));
                    finish();
                }else{
                    showMessage("Sign up Error",task.getException().getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pp = data.getParcelableExtra("data");
            ppImageButton.setImageBitmap(pp);
            // Do other work with full size photo saved in locationForPhotos

        }
    }
}
