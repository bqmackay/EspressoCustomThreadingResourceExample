package com.example.myapplication2.app;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.contrib.CountingIdlingResource;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;

/**
 * Created by byronmackay on 4/25/14.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    @SuppressWarnings("deprecation")
    public MainActivityTest() {
        super("com.example.myapplication2.app.MainActivity", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();

        DownloadHelper downloadHelper = getActivity().getDownloadHelper();
        CountingIdlingResource countingIdlingResource = new CountingIdlingResource("DownloadHelperCalls");
        getActivity().setDownloadHelper(new DecoratedDownloadHelper(downloadHelper, countingIdlingResource));
        registerIdlingResources(countingIdlingResource);
    }

    public void testAsyncRetrieval() throws Exception {
        onView(withText("AsyncTask")).perform(click());
        onView(withText("Async Retrieved")).check(matches(isDisplayed()));
    }

    public void testThreadRetrieval() throws Exception {
        onView(withText("Thread")).perform(click());
        onView(withText("Thread Retrieved")).check(matches(isDisplayed()));
    }

    private class DecoratedDownloadHelper extends DownloadHelper {

        private final DownloadHelper realDownloadHelper;
        private final CountingIdlingResource countingIdlingResource;

        private DecoratedDownloadHelper(DownloadHelper realDownloadHelper, CountingIdlingResource countingIdlingResource) {
            super(getActivity());
            this.realDownloadHelper = realDownloadHelper;
            this.countingIdlingResource = countingIdlingResource;
        }

        @Override
        public void getReps() {
            countingIdlingResource.increment();
            try {
                realDownloadHelper.getReps();
            } finally {
                countingIdlingResource.decrement();
            }
        }
    }
}
