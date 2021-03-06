package com.example.papr_w8;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.papr_w8.ProfilePack.EditProfile;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the profile activity functionality
 */
public class ProfileTest{
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.enterText((EditText) solo.getView((R.id.email)), "mazi@mail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test123");
        solo.clickOnButton("Login");
        solo.waitForFragmentById(R.id.fragment);
        View profile = solo.getView("profile");
        solo.clickOnView(profile);
    }


    /**
     * check if everything is presented correctly on profile page
     */
    @Test
    public void checkInfo(){

        solo.waitForFragmentById(R.id.profile);
        assertTrue(solo.waitForText("Mazi"));
        assertTrue(solo.waitForText("mazi@mail.com"));
        assertTrue(solo.waitForText("Mazi st, Mazi ave."));
        solo.clickOnButton("Edit Profile");
        solo.assertCurrentActivity("Wrong Activity", EditProfile.class);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
