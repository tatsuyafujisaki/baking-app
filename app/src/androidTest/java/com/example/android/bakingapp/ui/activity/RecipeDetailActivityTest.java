package com.example.android.bakingapp.ui.activity;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.adapter.RecipeViewHolderAdapter;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
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
public class RecipeDetailActivityTest {
    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void test() {
        final String recipe = "Nutella Pie";
        final String ingredient = "Graham Cracker crumbs";
        final String step = "1. Recipe Introduction";

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnHolderItem(new TypeSafeMatcher<RecipeViewHolderAdapter.ViewHolder>() {
                    @Override
                    protected boolean matchesSafely(RecipeViewHolderAdapter.ViewHolder item) {
                        return item.binding.recipeTextView.getText().equals(recipe);
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }, click()));

        onView(withId(R.id.ingredient_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(allOf(withId(R.id.ingredient_text_view), withText(ingredient)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(allOf(withId(R.id.step_text_view), withText(step)))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}