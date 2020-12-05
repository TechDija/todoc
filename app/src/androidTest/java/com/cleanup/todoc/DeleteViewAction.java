package com.cleanup.todoc;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

public class DeleteViewAction implements ViewAction {

    /**
     * A mechanism for ViewActions to specify what type of views they can operate on.
     *
     * <p>A ViewAction can demand that the view passed to perform meets certain constraints. For
     * example it may want to ensure the view is already in the viewable physical screen of the device
     * or is of a certain type.
     *
     * @return a <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     * <code>Matcher</code></a> that will be tested prior to calling perform.
     */
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    /**
     * Returns a description of the view action. The description should not be overly long and should
     * fit nicely in a sentence like: "performing %description% action on view with id ..."
     */
    @Override
    public String getDescription() {
        return "Click on specific button";
    }

    /**
     * Performs this action on the given view.
     *
     * @param uiController the controller to use to interact with the UI.
     * @param view         the view to act upon. never null.
     */
    @Override
    public void perform(UiController uiController, View view) {
        View button = view.findViewById(R.id.img_delete);
        button.performClick();
    }
}
