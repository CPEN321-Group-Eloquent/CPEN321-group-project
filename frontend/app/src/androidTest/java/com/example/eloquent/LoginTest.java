package com.example.eloquent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.*;

import android.view.View;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

public class LoginTest {

    @Rule
    public IntentsTestRule<Login> intentsTestRule = new IntentsTestRule<>(Login.class);

    @Test
    public void testIntent(){
        onView(withId(R.id.sign_in_button)).perform(click());
        intended (hasComponent(MainActivity.class.getName()));

    }


}