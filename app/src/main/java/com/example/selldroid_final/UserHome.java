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
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;

    private UserProfile userProfileFragment;
    private UserProfileUpdate updateUserProfileFragment;
    private AddPaymentMethod addPayment;
    private HomePage homePage;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        auth = FirebaseAuth.getInstance();
        bottomNavigation = findViewById(R.id.user_bottom_navigation);
        mFrameLayout = findViewById(R.id.user_main_frame);
        mToolbar = findViewById(R.id.user_toolbar);

        mToolbar.setTitle("SellDroid");
        setSupportActionBar(mToolbar);

        userProfileFragment = new UserProfile();
        updateUserProfileFragment = new UserProfileUpdate();
        addPayment = new AddPaymentMethod();
        homePage = new HomePage();
        setFragment(homePage);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_user_payment_methods:
                        setFragment(new PaymentMethods());
                        return true;
                    case R.id.nav_user_add_payment:
                        setFragment(new AddPaymentMethod());
                        return true;
                    case R.id.nav_user_cart:
                        setFragment(new CartItems());
                        return true;
                    case R.id.nav_user_home:
                        setFragment(new HomePage());
                        return true;
                    case R.id.nav_user_profile:
                        setFragment(new UserProfile());
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
                setFragment(new UserProfileUpdate());
                return true;
            case R.id.nav_user_logout:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_main_frame,fragment);
        transaction.commit();
    }
}