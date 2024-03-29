package com.example.ambuservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ambuservice.helper.Constants;
import com.example.ambuservice.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class BaseDrawerActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    private FrameLayout view_stub;
    private NavigationView navigation_view;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;

    SwitchCompat switchCompat;
    DatabaseReference dbRef;

    User currentUser;
    boolean isOnline;

    private FusedLocationProviderClient fusedLocationClient;
    public double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);
        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

//        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.actionBarText));

        dbRef = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        View view = navigation_view.inflateHeaderView(R.layout.nav_header);
        CircleImageView civUserPhoto = view.findViewById(R.id.civ_user_photo);
        TextView tvUserName = view.findViewById(R.id.tv_username);
        TextView tvUserRole = view.findViewById(R.id.tv_user_role);

        currentUser = Paper.book().read(Constants.CURR_USER_KEY);
        tvUserName.setText(currentUser.name);

        if (currentUser.roleId == 0) {
            tvUserRole.setText("Driver");

        } else {
            tvUserRole.setText("Client");
        }

        navigation_view.inflateMenu(R.menu.menu_drawer_items);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerMenu = navigation_view.getMenu();
        for (int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        // and so on...


    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (currentUser.userRole.equalsIgnoreCase("mechanic")) {
//            getLocation();

//            MenuItem menuItem = navigation_view.getMenu().findItem(R.id.nav_switch); // This is the menu item that contains your switch
//            switchCompat = menuItem.getActionView().findViewById(R.id.switcher);
//            checkOnlineStatus();
//
//            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    isOnline = isChecked;
//
//                    if (isChecked) {
//                        LiveMechanic mechanic = new LiveMechanic();
//                        mechanic.latitude = lat;
//                        mechanic.longitude = lng;
//
//                        dbRef.child("lives").child(currentUser.id).setValue(mechanic);
//
//                    } else {
//                        dbRef.child("lives").child(currentUser.id).removeValue();
//                    }
//                }
//            });
//
//        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }


    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        // set item as selected to persist highlight
        item.setChecked(true);

        switch (item.getItemId()) {

            case R.id.menu_drawer_item_profile:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, MyProfileActivity.class));
                break;

            case R.id.menu_drawer_item_history:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, HistoryActivity.class));
                break;

            case R.id.menu_drawer_item_about:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, AboutActivity.class));
                break;

            case R.id.menu_drawer_item_sign_out:
                mDrawerLayout.closeDrawers();
                FirebaseAuth.getInstance().signOut();
                dbRef.child("lives").child(currentUser.id).removeValue();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Paper.book().delete(Constants.CURR_USER_KEY);
                startActivity(intent);
                finish();

        }

        return false;
    }


    public void checkOnlineStatus() {
        dbRef.child("lives").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.getKey();
                        if (id != null && id.equals(currentUser.id)) {
                            isOnline = true;
                            switchCompat.setChecked(true);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                });
    }

}