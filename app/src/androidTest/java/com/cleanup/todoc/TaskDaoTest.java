package com.cleanup.todoc;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.model.Database;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.ProjectDao;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {
    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;
    private Database db;
    private final Calendar calendar = new GregorianCalendar();
    private final Task DEMO_TASK = new Task(1L, "ménage", calendar.getTimeInMillis());
    private final Project DEMO_PROJECT = new Project(1L, "Projet Tartampion", 0xFFEADAD1);

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new
            InstantTaskExecutorRule();

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, Database.class)
                .allowMainThreadQueries()
                .build();
        mTaskDao = db.taskDao();
        mProjectDao = db.projectDao();
    }

    @After
    public void closeDatabase() throws IOException {
        db.close();
    }

    @Test
    public void insertAndGetTask() throws Exception {
        //inserting data
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);
        //observing the changes in LiveData values
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks());
        //running the test
        assertEquals(tasks.size(), 1);
    }

    @Test
    public void insertAndDeleteTask() throws Exception {
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);

        Task addedTask = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks()).get(0);
        this.db.taskDao().deleteTask(addedTask);

        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndUpdateTask() throws Exception {
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);

        Task taskToUpdate = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks()).get(0);
        taskToUpdate.setName("testChore");
        this.mTaskDao.updateTask(taskToUpdate);

        List<Task> tasks = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks());
        assertTrue(tasks.size() == 1  && tasks.get(0).getName().equals("testChore"));

    }

    @Test
    public void getTasks_whenTasksAreEmpty() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());

    }
}
