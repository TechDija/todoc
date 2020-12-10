package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskDao;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.testUtils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TaskDataRepositoryTest {
    private TaskDataRepository repository;
    private TaskDao mTaskDao;
    private Task task;
    private List<Task> tasks;
    MutableLiveData liveDataTasks = new MutableLiveData();

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        mTaskDao = mock(TaskDao.class);
        repository = new TaskDataRepository(mTaskDao);
        task = new Task(1, "exemple", 1);
        tasks = Arrays.asList(task, task, task);
        liveDataTasks.setValue(tasks);
    }

    @Test
    public void gettingAllTasks_WithSuccess() throws InterruptedException {
        //given
        when(mTaskDao.getAllTasks()).thenReturn(liveDataTasks);
        Observer observer = mock(Observer.class);
        repository.getAllTasks().observeForever(observer);

        //then
        List<Task> actual = LiveDataTestUtil.getValue(this.repository.getAllTasks());
        assertEquals(tasks, actual);
    }

    //failure case
    @Test
    public void gettingAllTasks_IfThereIsNoTask() throws InterruptedException {
        //given
        when(mTaskDao.getAllTasks()).thenReturn(new MutableLiveData<List<Task>>());
        Observer observer = mock(Observer.class);
        repository.getAllTasks().observeForever(observer);

        //then
        List<Task> actual = LiveDataTestUtil.getValue(this.repository.getAllTasks());
        assertNull(actual);
    }

    @Test
    public void insertingTask_WithSuccess() {
        //when
        repository.createTask(task);
        //then
        verify(mTaskDao).insertTask(task);
    }

    @Test
    public void updatingTask_WithSuccess() {
        //when
        repository.updateTask(task);
        //then
        verify(mTaskDao).updateTask(task);
    }

    //failure case
    @Test
    public void updatingTask_IfThereIsNoTask() {
        //given
        when(mTaskDao.getAllTasks()).thenReturn(null);
        //when
        repository.updateTask(task);
        //then
        verify(mTaskDao).updateTask(task);
    }

    @Test
    public void deletingTask_WithSuccess() {
        //when
        repository.deleteTask(task);
        //then
        verify(mTaskDao).deleteTask(task);
    }

    //failure case
    @Test
    public void deletingTask_IfThereIsNoTask() {
        //given
        when(mTaskDao.getAllTasks()).thenReturn(null);
        //when
        repository.deleteTask(task);
        //then
        verify(mTaskDao).deleteTask(task);

    }
}
