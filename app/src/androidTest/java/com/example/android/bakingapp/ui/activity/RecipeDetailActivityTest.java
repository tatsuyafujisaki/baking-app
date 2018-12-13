package com.example.android.bakingapp.ui.activity;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.adapter.RecipeViewHolderAdapter;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {
    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void recipeDetailActivityTest() {
        final String recipe = "Nutella Pie";

        final int ingredientPosition = 0;
        final String ingredient = "Graham Cracker crumbs";

        final int stepPosition = 0;
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
                .perform(RecyclerViewActions.scrollToPosition(ingredientPosition));

        onView(allOf(withId(R.id.ingredient_text_view), withText(ingredient)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(stepPosition));

        onView(allOf(withId(R.id.step_text_view), withText(step)))
                .check(matches(isDisplayed()));
    }
}