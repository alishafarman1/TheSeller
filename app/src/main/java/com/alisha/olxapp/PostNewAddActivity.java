package com.alisha.olxapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.olxapp.adapters.RecyclerGeneralAdapter;
import com.alisha.olxapp.models.Cords;
import com.alisha.olxapp.models.Post;
import com.alisha.olxapp.models.PostImage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class PostNewAddActivity extends AppCompatActivity {

    EditText newTitleEt;
    EditText newPriceEt;
    EditText newDescpEt;
    EditText newAddressEt;
    View addressPicker;
    RecyclerView newImagesRv;
    Button postAddBtn;

    Post post;
    RecyclerGeneralAdapter<File> imagesAdapter;
    ArrayList<File> images;
    ProgressDialog progressDialog;

    private static int MAP_BUTTON_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        post = new Post();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait!");
        newTitleEt = findViewById(R.id.newTitleEt);
        newPriceEt = findViewById(R.id.newPriceEt);
        newDescpEt = findViewById(R.id.newDescpEt);
        newAddressEt = findViewById(R.id.newAddressEt);
        addressPicker = findViewById(R.id.addressPicker);
        newImagesRv = findViewById(R.id.newImagesRv);
        images = new ArrayList<>();
        images.add(null);
        HashMap<Integer,Integer> viewTypes = new HashMap<Integer,Integer>();
        viewTypes.put(1,R.layout.add_image_layout);
        viewTypes.put(2,R.layout.new_image_layout);
        newImagesRv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        imagesAdapter = new RecyclerGeneralAdapter<File>(images, new RecyclerGeneralAdapter.OnViewBinder<File>() {
            @Override
            public void onBindView(File data, final RecyclerGeneralAdapter.RecyclerGeneralViewHolder viewHolder) {
                View itemView = viewHolder.itemView;
                if(viewHolder.getItemViewType() == 1){
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pickImage();
                        }
                    });
                }else {
                    ImageView imageView = itemView.findViewById(R.id.newImageIv);
                    ImageView newImageRemoveBtn = itemView.findViewById(R.id.newImageRemoveBtn);
                    Glide.with(itemView.getContext()).load(data).into(imageView);
                    newImageRemoveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            images.remove(viewHolder.getAdapterPosition());
                            newImagesRv.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                    });
                }
            }
        }, new RecyclerGeneralAdapter.OnViewTypeDecision() {
            @Override
            public int onViewType(int position) {
                return position == 0 ? 1 : 2;
            }
        },viewTypes);
        newImagesRv.setAdapter(imagesAdapter);
        addressPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationPickerIntent = new LocationPickerActivity.Builder()
                        .withGeolocApiKey("AIzaSyDeZSmlAeFZUbj6lm0mA-WB4UaCw4fgnvM")
                        .withSearchZone("pk_PK")
                        .withUnnamedRoadHidden()
                        .shouldReturnOkOnBackPressed()
                        .build(PostNewAddActivity.this);

                startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
            }
        });
        postAddBtn = findViewById(R.id.postAddBtn);
        postAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newTitleEt.getText().toString().isEmpty()){
                    newTitleEt.setError("Please enter title");
                    return;
                }
                newTitleEt.setError(null);

                if(newPriceEt.getText().toString().isEmpty()){
                    newPriceEt.setError("Please enter price");
                    return;
                }
                newPriceEt.setError(null);

                if(newDescpEt.getText().toString().isEmpty()){
                    newDescpEt.setError("Please enter Description");
                    return;
                }
                newDescpEt.setError(null);

                if(newAddressEt.getText().toString().isEmpty()){
                    newAddressEt.setError("Please select Address");
                    return;
                }
                newAddressEt.setError(null);

                if(images.isEmpty()){
                    Toast.makeText(PostNewAddActivity.this,"Please select at least one image",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
                final String id = reference.push().getKey();
                final ArrayList<String> postImages = new ArrayList<String>();
                for(final File image : images){
                    if(image == null){
                        continue;
                    }
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("posts/"+id).child(image.getName());
                    UploadTask uploadTask = storageReference.putFile(Uri.fromFile(image));
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                postImages.add(downloadUri.toString());
                                if(postImages.size() == images.size()-1){
                                    post.setImages(postImages);
                                    Toast.makeText(PostNewAddActivity.this,""+post.getImages().size(),Toast.LENGTH_SHORT).show();
                                    progressDialog.show();
                                    post.setUser(FirebaseAuth.getInstance().getCurrentUser());
                                    post.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    post.setTitle(newTitleEt.getText().toString());
                                    post.setPrice(Integer.valueOf(newPriceEt.getText().toString()));
                                    post.setDescription(newDescpEt.getText().toString());
                                    post.setAddress(newAddressEt.getText().toString());
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    String data = df.format(new Date());
                                    post.setCreatedDate(data);
                                    reference.child(id).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(PostNewAddActivity.this,"Fail to upload",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void pickImage(){
        ImagePicker.with(this) .setMaxSize(4).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if(requestCode == MAP_BUTTON_REQUEST_CODE){
                String address = data.getStringExtra(LOCATION_ADDRESS);
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.e("LATITUDE****", latitude+"");
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.e("LONGITUDE****", longitude + "");
                Log.e("ADDRESS****", address);
                newAddressEt.setText(address);
                post.setLocation(new Cords(latitude,longitude));
                post.setAddress(address);

            }

            if(requestCode == Config.RC_PICK_IMAGES){
                ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                for(Image img : images){
                   this.images.add(new File((img.getPath())));
                   imagesAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
