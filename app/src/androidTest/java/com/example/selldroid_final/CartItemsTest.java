package com.example.selldroid_final;

import android.app.Instrumentation;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class CartItemsTest {

    private TestClassActivity tActivity = null;

    @Rule
    public ActivityTestRule<TestClassActivity> activityTestRule = new ActivityTestRule<TestClassActivity>(TestClassActivity.class);

    @Before
    public void setUp() throws Exception {
        tActivity = activityTestRule.getActivity();
    }

    @Test
    public void testFragmentLaunch() {
        RelativeLayout relativeContainer = tActivity.findViewById(R.id.test_container);
        assertNotNull(relativeContainer);
        CartItems fragmentCartItems = new CartItems();
        tActivity.getSupportFragmentManager().beginTransaction().add(relativeContainer.getId(), fragmentCartItems).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragmentCartItems.getView().findViewById(R.id.cart_fragment);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        tActivity = null;
    }
}