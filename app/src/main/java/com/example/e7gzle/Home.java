package com.example.e7gzle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import Fragments.FragmentSearch;
import Fragments.FragmentSetting;
import Fragments.FragmentTicket;
import Fragments.Fragment_Booking;
import Fragments.Fragment_Result;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView header_name,header_email;
    private CircleImageView header_image;
    private Uri imageUri;
    private View view;
    private DatabaseReference myRef1;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        toolbar = findViewById(R.id.home_toolbar);
        navigationView = findViewById(R.id.home_nav_view);
        drawerLayout = findViewById(R.id.home_drawer_layout);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        // This line to make the menu elemnts clickable
        navigationView.bringToFront();
        // This condation to make a default fragment
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.home_nav);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentSearch()).commit();
        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Users").child(mAuth.getUid()).child("Personal_Info");

        imageUri = null;
        view = navigationView.getHeaderView(0);
        header_image = view.findViewById(R.id.nav_header_img);
        header_name = view.findViewById(R.id.nav_header_text1);
        header_email = view.findViewById(R.id.nav_header_text2);

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals("email")) {
                        header_email.setText(dataSnapshot1.getValue().toString());
                    } else if (dataSnapshot1.getKey().equals("first_name")) {
                        header_name.setText(dataSnapshot1.getValue().toString());
                    } else if (dataSnapshot1.getKey().equals("Profile_images")) {
                        Picasso.get().load(dataSnapshot1.getValue().toString())
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.ic_baseline_account_circle_24)
                                .into(header_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentSearch()).commit();
                break;
            case R.id.my_ticket_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentTicket()).commit();
                break;
            case R.id.setting_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentSetting()).commit();
                break;
            case R.id.log_out_nav:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finishAffinity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void addFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.home_container);
        if (fragment instanceof FragmentSearch) {
            fragment = new Fragment_Result();
        } else if (fragment instanceof Fragment_Result) {
            fragment = new Fragment_Booking();
        } else if (fragment instanceof Fragment_Booking) {
            fragment = new FragmentTicket();
        }
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_container,fragment);
        fragmentTransaction.commit();
    }
}