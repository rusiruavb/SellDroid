package com.example.selldroid_final;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class SellerProfileTest {

    private TestClassActivity sellerProfileTestActivity = null;

    @Rule
    public ActivityTestRule<TestClassActivity> sellerProfileRule = new ActivityTestRule<TestClassActivity>(TestClassActivity.class);

    @Before
    public void setUp() throws Exception {
        sellerProfileTestActivity = sellerProfileRule.getActivity();
    }

    @Test
    public void testUserProfileLaunchFragment() {
        RelativeLayout relativeContainer = sellerProfileTestActivity.findViewById(R.id.test_container);
        assertNotNull(relativeContainer);
        SellerProfile fragmentAddProduct = new SellerProfile();
        sellerProfileTestActivity.getSupportFragmentManager().beginTransaction().add(relativeContainer.getId(), fragmentAddProduct).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragmentAddProduct.getView().findViewById(R.id.seller_profile_fragment);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        sellerProfileTestActivity = null;
    }
}