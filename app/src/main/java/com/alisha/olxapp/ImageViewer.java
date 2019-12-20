package com.alisha.olxapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.alisha.olxapp.models.Post;
import com.google.gson.Gson;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.builder.GalleryBuilder;
import com.veinhorn.scrollgalleryview.builder.GallerySettings;

import static com.veinhorn.scrollgalleryview.loader.picasso.dsl.DSL.image;


public class ImageViewer extends AppCompatActivity {
    private ScrollGalleryView galleryView;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        post = (new Gson()).fromJson(getIntent().getStringExtra("post"), Post.class);
        GalleryBuilder builder = ScrollGalleryView
                .from((ScrollGalleryView) findViewById(R.id.scroll_gallery_view))
                .settings(
                        GallerySettings
                                .from(getSupportFragmentManager())
                                .thumbnailSize(100)
                                .enableZoom(true)
                                .build()
                );
        for(String imageUrl : post.getImages()){
            builder.add(image(imageUrl));
        }
        galleryView = builder.build();


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
