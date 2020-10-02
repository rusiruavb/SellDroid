package com.example.selldroid_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SellerHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar sellerToolBar;
    private FrameLayout sellerFrameLayout;

    private SellerProfile sellerProfile;
    private AddProduct addProduct;
    private HomePage homePage;
    private SellerItems sellerItems;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        bottomNavigationView = findViewById(R.id.seller_bottom_navigation);
        sellerFrameLayout = findViewById(R.id.seller_main_frame);
        sellerToolBar = findViewById(R.id.toolbar);

        sellerProfile = new SellerProfile();
        homePage = new HomePage();
        addProduct = new AddProduct();
        sellerItems = new SellerItems();
        auth = FirebaseAuth.getInstance();

        sellerToolBar.setTitle("SellDroid Seller");
        setSupportActionBar(sellerToolBar);

        setFragment(sellerItems);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        setFragment(sellerItems);
                        return true;
                    case R.id.nav_seller_home:
                        setFragment(new SellerHomePage());
                        return true;
                    case R.id.nav_seller_add_product:
                        setFragment(addProduct);
                        return true;
                    case R.id.nav_seller_profile:
                        setFragment(sellerProfile);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_user_update_profile:
                setFragment(new SellerProfileUpdate());
                return true;
            case R.id.nav_user_logout:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), SellerLogin.class));
                return true;
            default:
                return false;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.seller_main_frame,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}