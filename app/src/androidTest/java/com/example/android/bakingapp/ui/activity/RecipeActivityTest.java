package com.example.android.bakingapp.ui.activity;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void recipeActivityTest1() {
        final int recipeIndex = 0;
        final String recipeName = "Nutella Pie";

        // Scroll to the first recipe
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(recipeIndex));

        // Check that the name of the first recipe is Nutella Pie
        onView(allOf(withId(R.id.recipe_text_view), withText(recipeName)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void recipeActivityTest2() {
        final int recipeIndex = 0;
        final String ingredient = "Graham Cracker crumbs";

        // Click on the first recipe.
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipeIndex, click()));

        // Check that one of the ingredients is "Graham Cracker crumbs"
        onView(allOf(withId(R.id.ingredient_text_view), withText(ingredient))).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}