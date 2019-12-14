package com.alisha.olxapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.olxapp.adapters.RecyclerGeneralAdapter;
import com.alisha.olxapp.models.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class PostNewAddActivity extends AppCompatActivity {

    EditText newAddressEt;
    View addressPicker;
    RecyclerView newImagesRv;
    RecyclerGeneralAdapter<File> imagesAdapter;
    Button postAddBtn;
    Post post;
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

            }
        });

    }

    private void pickImage(){
        ImagePicker.with(this) // Activity or Fragment
                .setMaxSize(4)
                .start();
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
