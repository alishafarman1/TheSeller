package com.alisha.olxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisha.olxapp.models.Post;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.Serializable;

public class PostDetailActivity extends AppCompatActivity {

    ImageView dpostMainImage;
    TextView dpostTitle;
    TextView dpostPrice;
    TextView dpostUserName;
    TextView dpostAddreess;
    TextView dpostDate;
    TextView dpostDescp;
    View dpostCallBtn;
    View dpostLocateBtn;
    Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        post = (new Gson()).fromJson(getIntent().getStringExtra("post"), Post.class);

        dpostMainImage = findViewById(R.id.dpostMainImage);
        dpostTitle = findViewById(R.id.dpostTitle);
        dpostPrice = findViewById(R.id.dpostPrice);
        dpostUserName = findViewById(R.id.dpostUserName);
        dpostAddreess = findViewById(R.id.dpostAddreess);
        dpostDate = findViewById(R.id.dpostDate);
        dpostDescp = findViewById(R.id.dpostDescp);
        dpostCallBtn = findViewById(R.id.dpostCallBtn);
        dpostLocateBtn = findViewById(R.id.dpostLocateBtn);

        Glide.with(this).load(post.getImages().get(0)).into(dpostMainImage);
        dpostMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostDetailActivity.this,ImageViewer.class);
                i.putExtra("post",getIntent().getStringExtra("post"));
                startActivity(i);
            }
        });
        dpostTitle.setText(post.getTitle());
        dpostPrice.setText("Rs. " + post.getPrice());
        dpostUserName.setText("Posted By " + post.getUser().getName());
        dpostAddreess.setText("Address : " + post.getAddress());
        dpostDate.setText("Address : " + post.getCreatedDate());
        dpostDescp.setText(post.getDescription());
        dpostCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", post.getUser().getPhone(), null));
                startActivity(intent);
            }
        });
        dpostLocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "https://www.google.com/maps/dir/?api=1&travelmode=driving&dir_action=navigate&destination=" + post.getLocation().getLat() + "," + post.getLocation().getLng();
                Uri location = Uri.parse(URL);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
