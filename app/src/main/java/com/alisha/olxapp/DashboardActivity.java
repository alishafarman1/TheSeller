package com.alisha.olxapp;

import android.content.Intent;
import android.os.Bundle;

import com.alisha.olxapp.adapters.RecyclerGeneralAdapter;
import com.alisha.olxapp.models.AppUser;
import com.alisha.olxapp.models.Post;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RecyclerGeneralAdapter.OnViewBinder<Post>, RecyclerGeneralAdapter.OnViewTypeDecision {

    ArrayList<Post> recentAdds;
    RecyclerGeneralAdapter adapter;
    RecyclerView homeRecentPostsRv;
    Map<Integer,Integer> types;
    Button postAddBtn;
    DatabaseReference posts;
    ChildEventListener listener;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppUser appUser = (new Gson()).fromJson(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),AppUser.class);
        ImageView iv = navigationView.getHeaderView(0).findViewById(R.id.navImageView);
        TextView nameTv = navigationView.getHeaderView(0).findViewById(R.id.navNameView);
        TextView emailTv = navigationView.getHeaderView(0).findViewById(R.id.navEmailView);
        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(iv);
        nameTv.setText(appUser.getName()+" - "+appUser.getPhone());
        emailTv.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.nav_postNewAdd, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.action_logout, R.string.action_logout)
        {
            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setupView();
    }

    private void setupView(){
        posts = FirebaseDatabase.getInstance().getReference("posts");
        recentAdds = new ArrayList<>();
        homeRecentPostsRv = findViewById(R.id.homeRecentPostsRv);
        types = new HashMap();
        types.put(1,R.layout.post_item_layout);
        types.put(2,R.layout.header_layout);
        adapter = new RecyclerGeneralAdapter<Post>(recentAdds,this,this,types);
        homeRecentPostsRv.setAdapter(adapter);
        homeRecentPostsRv.setLayoutManager(new LinearLayoutManager(this));
        postAddBtn = findViewById(R.id.postAddBtn);
        postAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,PostNewAddActivity.class));
            }
        });
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                if(recentAdds.isEmpty()){
                    recentAdds.add(new Post());
                }
                recentAdds.add(post);
                adapter.notifyItemInserted(recentAdds.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        posts.addChildEventListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent newIntent = new Intent(DashboardActivity.this,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBindView(final Post data, RecyclerGeneralAdapter.RecyclerGeneralViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,PostDetailActivity.class);
                i.putExtra("post",(new Gson()).toJson(data));
                startActivity(i);
            }
        });
        if(viewHolder.getItemViewType() == 1) {
            ImageView imageIv = itemView.findViewById(R.id.postItemImageIv);
            TextView titleTv = itemView.findViewById(R.id.postItemTitleTv);
            TextView priceTv = itemView.findViewById(R.id.postItemPriceTv);
            TextView dateTv = itemView.findViewById(R.id.postItemDateTv);

            Glide.with(this).load(data.getImages().get(0)).into(imageIv);
            titleTv.setText(data.getTitle());
            priceTv.setText("Rs. " + data.getPrice());
            dateTv.setText("Posted by "+youOrName(data)+" at "+data.getCreatedDate());
        }else{
            TextView postListHeaderTv = itemView.findViewById(R.id.postListHeaderTv);
            postListHeaderTv.setText(R.string.homeListTitle);
        }
    }

    @Override
    public int onViewType(int position) {
        return (position == 0 ? 2 : 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        posts.removeEventListener(listener);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_postNewAdd){
            startActivity(new Intent(DashboardActivity.this,PostNewAddActivity.class));
            return true;
        }
        if(id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent newIntent = new Intent(DashboardActivity.this,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
            return true;
        }
        return false;
    }

    public static boolean isYours(Post post){
        return (post.getUser().getId().equals(FirebaseAuth.getInstance().getUid()));
    }

    public static String youOrName(Post post){
        return isYours(post) ? "You" : post.getUser().getName();
    }
}
