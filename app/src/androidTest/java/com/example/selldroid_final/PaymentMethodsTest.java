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

public class PaymentMethodsTest {

    private TestClassActivity paymentMethodTestActivity = null;

    @Rule
    public ActivityTestRule<TestClassActivity> paymentMethodTestRule = new ActivityTestRule<TestClassActivity>(TestClassActivity.class);

    @Before
    public void setUp() throws Exception {
        paymentMethodTestActivity = paymentMethodTestRule.getActivity();
    }

    @Test
    public void testPaymentMethodFragmentLaunch() {
        RelativeLayout relativeContainer = paymentMethodTestActivity.findViewById(R.id.test_container);
        assertNotNull(relativeContainer);
        PaymentMethods fragmentPaymentMethods = new PaymentMethods();
        paymentMethodTestActivity.getSupportFragmentManager().beginTransaction().add(relativeContainer.getId(), fragmentPaymentMethods).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragmentPaymentMethods.getView().findViewById(R.id.fragment_payment_methods);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        paymentMethodTestActivity = null;
    }
}