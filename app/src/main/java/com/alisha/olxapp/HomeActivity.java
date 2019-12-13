package com.alisha.olxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.olxapp.adapters.RecyclerGeneralAdapter;
import com.alisha.olxapp.models.Post;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements RecyclerGeneralAdapter.OnViewBinder<Post>, RecyclerGeneralAdapter.OnViewTypeDecision {

    ArrayList<Post> recentAdds;
    RecyclerGeneralAdapter adapter;
    RecyclerView homeRecentPostsRv;
    Map<Integer,Integer> types;
    Button postAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupView();
    }

    private void setupView(){
        recentAdds = new ArrayList<>();
        SampleData.fillData(recentAdds);
        homeRecentPostsRv = findViewById(R.id.homeRecentPostsRv);
        types = new HashMap();
        types.put(1,R.layout.post_item_layout);
        types.put(2,R.layout.header_layout);
        adapter = new RecyclerGeneralAdapter<Post>(R.layout.post_item_layout,recentAdds,this,this,types);
        homeRecentPostsRv.setAdapter(adapter);
        homeRecentPostsRv.setLayoutManager(new LinearLayoutManager(this));
        postAddBtn = findViewById(R.id.postAddBtn);
        postAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,PostNewAddActivity.class));
            }
        });
    }

    @Override
    public void onBindView(Post data, RecyclerGeneralAdapter.RecyclerGeneralViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        if(viewHolder.getItemViewType() == 1) {
            ImageView imageIv = itemView.findViewById(R.id.postItemImageIv);
            TextView titleTv = itemView.findViewById(R.id.postItemTitleTv);
            TextView priceTv = itemView.findViewById(R.id.postItemPriceTv);
            TextView dateTv = itemView.findViewById(R.id.postItemDateTv);

            Glide.with(this).load(data.getImagesDemo().get(0)).into(imageIv);
            titleTv.setText(data.getTitle());
            priceTv.setText("Rs. " + data.getPrice());
            dateTv.setText(data.getCreatedDate());
        }else{
            TextView postListHeaderTv = itemView.findViewById(R.id.postListHeaderTv);
            postListHeaderTv.setText(R.string.homeListTitle);
        }
    }

    @Override
    public int onViewType(int position) {
        return (recentAdds.get(position).getDescription() == null ? 2 : 1);
    }
}
