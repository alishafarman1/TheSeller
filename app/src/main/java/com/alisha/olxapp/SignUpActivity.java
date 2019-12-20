package com.alisha.olxapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.alisha.olxapp.models.AppUser;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    ImageButton ppImageButton;
    ProgressDialog progressDialog;
    File imageFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText name = findViewById(R.id.name);
        final EditText phone = findViewById(R.id.phone);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText cpassword = findViewById(R.id.cpassword);
        final Button signUpBtn = findViewById(R.id.signUpBtn);
        ppImageButton = findViewById(R.id.ppImageButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait!");

        ppImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageFile == null){
                    showMessage("Validation Error","Please select Image");
                    return;
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Please enter name");
                    return;
                }
                name.setError(null);

                if(phone.getText().toString().isEmpty()){
                    phone.setError("Please enter username");
                    return;
                }
                phone.setError(null);

                if (phone.getText().toString().contains(".") || phone.getText().toString().contains("#") || phone.getText().toString().contains("$") || phone.getText().toString().contains("[") || phone.getText().toString().contains("]")) {
                    phone.setError("Please enter a valid username");
                    return;
                }
                phone.setError(null);

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
                signUp(name.getText().toString(),phone.getText().toString(),email.getText().toString(),password.getText().toString());

            }
        });
    }

    void showMessage(String title, String message) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, null).show();
    }

    void signUp(final String name, final String phone, final String email, final String password){
        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> authTask) {
                if(authTask.isSuccessful()) {
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users/"+authTask.getResult().getUser().getUid()).child(imageFile.getName());
                    UploadTask uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) throws Exception {
                            if (!uploadTask.isSuccessful()) {
                                showMessage("Sign up Error",uploadTask.getException().getMessage());
                                progressDialog.dismiss();
                                throw uploadTask.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri downloadUri = task.getResult();
                                final AppUser appUser = new AppUser(name,email,phone,authTask.getResult().getUser().getUid(),downloadUri.toString());

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(authTask.getResult().getUser().getUid());
                                ref.setValue(appUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> dbtask) {
                                        if(dbtask.isSuccessful()){
                                            appUser.setImage(null);
                                            appUser.setId(null);
                                            appUser.setEmail(null);
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName((new Gson()).toJson(appUser))
                                                    .setPhotoUri(downloadUri)
                                                    .build();

                                            authTask.getResult().getUser().updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> Updatetask) {
                                                            progressDialog.dismiss();
                                                            if (Updatetask.isSuccessful()) {
                                                                finish();
                                                            }else{
                                                                showMessage("Update Error",Updatetask.getException().getMessage());
                                                                FirebaseAuth.getInstance().signOut();
                                                            }
                                                        }
                                                    });
                                        }else{
                                            showMessage("DB Error",dbtask.getException().getMessage());
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    }
                                });


                            }else{
                                showMessage("URI Error",task.getException().getMessage());
                                FirebaseAuth.getInstance().signOut();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("URI Error",e.getMessage());
                            FirebaseAuth.getInstance().signOut();
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    showMessage("Sign up Error",authTask.getException().getMessage());
                }
            }
        });
    }

    private void pickImage(){
        ImagePicker.with(this) .setMultipleMode(false).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Config.RC_PICK_IMAGES) {
                ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                imageFile = new File(images.get(0).getPath());
                Glide.with(this).load(imageFile).into(ppImageButton);
            }
        }
    }
}
