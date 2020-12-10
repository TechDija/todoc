package com.cleanup.todoc;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cleanup.todoc.androidTestUtils.DeleteViewAction;
import com.cleanup.todoc.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.androidTestUtils.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.equalTo;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addAndRemoveTask() throws InterruptedException {
        MainActivity activity = rule.getActivity();
        TextView lblNoTask = activity.findViewById(R.id.lbl_no_task);
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);
        int anteriorCount = getRecyclerViewCount();
        if (anteriorCount != 0) {
            for (int i = 0; i < anteriorCount; i++) {
                onView(withId(R.id.list_tasks))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
                Thread.sleep(1000);
            }
        }

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);

        // Check that lblNoTask is not displayed anymore
        assertThat(lblNoTask.getVisibility(), equalTo(View.GONE));
        // Check that recyclerView is displayed
        assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
        // Check that it contains one element
        assertThat(Objects.requireNonNull(listTasks.getAdapter()).getItemCount(), equalTo(1));

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        Thread.sleep(500);

        // Check that it contains one element less than before
        assertThat(listTasks.getAdapter().getItemCount(), equalTo(0));

    }
    @Test
    public void alphabeticalSorting() throws InterruptedException {
        // deleting previous tasks
        int anteriorCount = getRecyclerViewCount();

        if (anteriorCount != 0) {
            for (int i = 0; i < anteriorCount; i++) {
                onView(withId(R.id.list_tasks)).
                        perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
                Thread.sleep(1000);
            }
        }
        // creating new tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);
        // Sort

        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

    }

    @Test
    public void invertedAlphabeticalSorting() throws InterruptedException {
        // deleting previous tasks
        int anteriorCount = getRecyclerViewCount();

        if (anteriorCount != 0) {
            for (int i = 0; i < anteriorCount; i++) {
                onView(withId(R.id.list_tasks)).
                        perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
                Thread.sleep(1000);
            }
        }
        // creating new tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);
        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }

    @Test
    public void olderSorting() throws InterruptedException {
        // deleting previous tasks
        int anteriorCount = getRecyclerViewCount();

        if (anteriorCount != 0) {
            for (int i = 0; i < anteriorCount; i++) {
                onView(withId(R.id.list_tasks)).
                        perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
                Thread.sleep(1000);
            }
        }
        // creating new tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);
        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
    }

    @Test
    public void recentSorting() throws InterruptedException {
        // deleting previous tasks
        int anteriorCount = getRecyclerViewCount();

        if (anteriorCount != 0) {
            for (int i = 0; i < anteriorCount; i++) {
                onView(withId(R.id.list_tasks)).
                        perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
                Thread.sleep(1000);
            }
        }
        // creating new tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);
        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }


    private int getRecyclerViewCount() {
        MainActivity activity = rule.getActivity();
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);
        if (listTasks != null) {
            return Objects.requireNonNull(listTasks.getAdapter()).getItemCount();
        } else {
            return 0;
        }
    }
}