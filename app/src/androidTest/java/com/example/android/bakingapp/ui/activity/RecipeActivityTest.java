package com.example.android.bakingapp.ui.activity;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.adapter.RecipeViewHolderAdapter;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void recipeNameTest() {
        String recipe = "Nutella Pie";

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.scrollToHolder(new TypeSafeMatcher<RecipeViewHolderAdapter.ViewHolder>() {
                    @Override
                    protected boolean matchesSafely(RecipeViewHolderAdapter.ViewHolder item) {
                        return item.binding.recipeTextView.getText().equals(recipe);
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        onView(allOf(withId(R.id.recipe_text_view), withText(recipe)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void recipeServingsTest() {
        String recipe = "Brownies";
        String servings = "Servings: 8";

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.scrollToHolder(new TypeSafeMatcher<RecipeViewHolderAdapter.ViewHolder>() {
                    @Override
                    protected boolean matchesSafely(RecipeViewHolderAdapter.ViewHolder item) {
                        return item.binding.recipeTextView.getText().equals(recipe);
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        onView(allOf(withId(R.id.servings_text_view), withText(servings), hasSibling(withText(recipe))))
                .check(matches(isDisplayed()));
    }
}