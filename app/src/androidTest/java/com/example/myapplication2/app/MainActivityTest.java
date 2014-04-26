package com.example.myapplication2.app;

import com.google.android.apps.common.testing.ui.espresso.contrib.CountingIdlingResource;

import android.test.ActivityInstrumentationTestCase2;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

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

        Runnable runnable = getActivity().getDownloadHelper().getWebCallRunnable();
        CountingIdlingResource countingIdlingResource = new CountingIdlingResource("WebCallRunnableCall");
        getActivity().getDownloadHelper().setWebCallRunnable(new DecoratedWebCallRunnable(runnable, countingIdlingResource));
        registerIdlingResources(countingIdlingResource);
    }

    public void testAsyncRetrieval() throws Exception {
        onView(withText("AsyncTask")).perform(click());
        onView(withText("Async Retrieved")).check(matches(isDisplayed()));
    }

    public void testThreadRetrieval() throws Exception {
        onView(withText("Thread")).perform(click());
        assertTrue(getActivity().isDidThreadReturn());
//        onView(withText("Thread Retrieved")).check(matches(isDisplayed()));
    }

    private class DecoratedWebCallRunnable implements Runnable {

        private final Runnable realRunnable;
        private final CountingIdlingResource countingIdlingResource;

        private DecoratedWebCallRunnable(Runnable realRunnable, CountingIdlingResource countingIdlingResource) {
            this.realRunnable = realRunnable;
            this.countingIdlingResource = countingIdlingResource;
        }

        @Override
        public void run() {
            countingIdlingResource.increment();
            try {
                realRunnable.run();
            } finally {
                countingIdlingResource.decrement();
            }
        }
    }

//    private class DecoratedDownloadHelper extends DownloadHelper {
//
//        private final DownloadHelper realDownloadHelper;
//        private final CountingIdlingResource countingIdlingResource;
//
//        private DecoratedDownloadHelper(DownloadHelper realDownloadHelper, CountingIdlingResource countingIdlingResource) {
//            super(getActivity());
//            this.realDownloadHelper = realDownloadHelper;
//            this.countingIdlingResource = countingIdlingResource;
//        }
//
//        @Override
//        public void getReps() {
//            countingIdlingResource.increment();
//            try {
//                realDownloadHelper.getReps();
//            } finally {
//                countingIdlingResource.decrement();
//            }
//        }
//    }
}
