package com.example.selldroid_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class SellerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Layout variables
    private DrawerLayout sellerDrawer;
    private NavigationView sellerNavigation;
    private Toolbar sellerToolBar;
    private FrameLayout sellerFrameLayout;
    private ActionBarDrawerToggle toggle;

    // Fragment Objects
    private SellerProfile sellerProfile;
    private SellerProfileUpdate sellerUpdate;
    private ProductPage productPage;
    private SelectPayment selectPay;
    private HomePage homePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        // Assign layout variables
        sellerDrawer = findViewById(R.id.seller_drawer_layout);
        sellerNavigation = findViewById(R.id.seller_navigation_view);
        sellerToolBar = findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(this,sellerDrawer,R.string.open,R.string.close);
        //
        sellerToolBar.setTitle("SellDroid Seller");
        setSupportActionBar(sellerToolBar);
        sellerDrawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sellerNavigation.setNavigationItemSelectedListener(this);

        // Create fragment objects
        sellerProfile = new SellerProfile();
        sellerUpdate = new SellerProfileUpdate();
        productPage = new ProductPage();
        selectPay = new SelectPayment();
        homePage = new HomePage();

        setFragment(homePage);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.seller_drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void displaySelectedListner(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_seller_account:
                fragment = new SellerProfile();
                break;
            case R.id.nav_seller_account_update:
                fragment = new SellerProfileUpdate();
                break;
            case R.id.nav_add_product:
                fragment = new AddProduct();
                break;
            case R.id.nav_seller_items:
                fragment = new SellerItems();
                break;
            case R.id.nav_home:
                fragment = new HomePage();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, fragment);
            transaction.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.seller_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListner(item.getItemId());
        return false;
    }
}