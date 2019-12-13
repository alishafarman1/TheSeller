package com.alisha.olxapp;

import com.alisha.olxapp.models.Post;

import java.util.ArrayList;

public class SampleData {
    public static void fillData(ArrayList<Post> posts) {
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.sample_pic_1);
        images.add(R.drawable.sample_pic_2);
        images.add(R.drawable.sample_pic_3);
        Post hp = new Post("Plot available for sale", null, "3 Dec 2019", 5000000, images);
        Post p = new Post("Plot available for sale", "Glide supports fetching, decoding, and displaying video stills, images, and animated GIFs. Glide includes a flexible API that allows developers to plug in to almost any network stack.", "3 Dec 2019", 5000000, images);

        for (int i = 0; i < 20; i++) {
            if (i == 0) {
                posts.add(hp);
            } else {
                posts.add(p);
            }
        }
    }
}
