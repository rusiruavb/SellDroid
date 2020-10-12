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

public class UserProfileUpdateTest {

    private TestClassActivity userProfileTestActivity = null;

    @Rule
    public ActivityTestRule<TestClassActivity> userProfileRule = new ActivityTestRule<TestClassActivity>(TestClassActivity.class);

    @Before
    public void setUp() throws Exception {
        userProfileTestActivity = userProfileRule.getActivity();
    }

    @Test
    public void testUserProfileLaunchFragment() {
        RelativeLayout relativeContainer = userProfileTestActivity.findViewById(R.id.test_container);
        assertNotNull(relativeContainer);
        UserProfileUpdate fragmentAddProduct = new UserProfileUpdate();
        userProfileTestActivity.getSupportFragmentManager().beginTransaction().add(relativeContainer.getId(), fragmentAddProduct).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragmentAddProduct.getView().findViewById(R.id.fragment_user_profile_update);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        userProfileTestActivity = null;
    }
}