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

public class AddProductTest {

    private TestClassActivity addProductTestActivity = null;

    @Rule
    public ActivityTestRule<TestClassActivity> addProductTestRule = new ActivityTestRule<TestClassActivity>(TestClassActivity.class);

    @Before
    public void setUp() throws Exception {
        addProductTestActivity = addProductTestRule.getActivity();
    }

    @Test
    public void testAddProductFragmentLaunch() {
        RelativeLayout relativeContainer = addProductTestActivity.findViewById(R.id.test_container);
        assertNotNull(relativeContainer);
        AddProduct fragmentAddProduct = new AddProduct();
        addProductTestActivity.getSupportFragmentManager().beginTransaction().add(relativeContainer.getId(), fragmentAddProduct).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragmentAddProduct.getView().findViewById(R.id.add_product_fragment);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        addProductTestActivity = null;
    }

}