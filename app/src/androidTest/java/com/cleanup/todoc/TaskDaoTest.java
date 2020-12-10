package com.cleanup.todoc;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.androidTestUtils.LiveDataTestUtil;
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
    public void closeDatabase() {
        db.close();
    }

    @Test
    public void insertAndGetTask() throws Exception {
        //given
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);
        //then
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks());
        assertEquals(tasks.size(), 1);
    }

    @Test
    public void insertAndDeleteTask() throws Exception {
        //given
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);
        //when
        Task addedTask = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks()).get(0);
        this.db.taskDao().deleteTask(addedTask);
        // then
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndUpdateTask() throws Exception {
        //given
        this.mProjectDao.createProject(DEMO_PROJECT);
        this.mTaskDao.insertTask(DEMO_TASK);
        //when
        Task taskToUpdate = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks()).get(0);
        taskToUpdate.setName("testChore");
        this.mTaskDao.updateTask(taskToUpdate);
        //then
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTaskDao.getAllTasks());
        assertTrue(tasks.size() == 1  && tasks.get(0).getName().equals("testChore"));

    }

    @Test
    public void getTasks_whenTasksAreEmpty() throws InterruptedException {
        //when
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getAllTasks());
        //then
        assertTrue(tasks.isEmpty());

    }
}
