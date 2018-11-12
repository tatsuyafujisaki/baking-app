package com.example.android.bakingapp.ui.activity;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v4.app.FragmentActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Objects;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {
    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    private static Activity getCurrentActivity() {
        Activity[] activity = new Activity[1];

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            if (!activities.isEmpty()) {
                activity[0] = activities.iterator().next();
            }
        });

        return activity[0];
    }

    @Before
    public void registerIdlingResource() {
        int recipePosition = 0;
        int stepPosition = 0;

        // Move from RecipeActivity to RecipeDetailActivity
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipePosition, click()));

        // Move from RecipeDetailActivity to StepDetailActivity
        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(stepPosition, click()));

        // In StepDetailActivity, get IdlingResouce from StepDetailFragment
        idlingResource = ((StepDetailFragment) Objects.requireNonNull(((FragmentActivity) getCurrentActivity())
                .getSupportFragmentManager()
                .findFragmentById(R.id.step_detail_fragment)))
                .getIdlingResource();

        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void stepDetailActivityTest() {
        final String stepDescription = "Recipe Introduction";

        onView(allOf(withId(R.id.step_description_text_view), withText(stepDescription)))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}