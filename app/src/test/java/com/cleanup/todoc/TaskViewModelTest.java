package com.cleanup.todoc;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.testUtils.LiveDataTestUtil;
import com.cleanup.todoc.testUtils.SynchronousExecutor;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(JUnit4.class)

public class TaskViewModelTest {
    private TaskViewModel viewModel;
    private TaskDataRepository repository;
    private Task task;
    private List<Task> tasks;
    MutableLiveData liveDataTasks = new MutableLiveData();

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        repository = mock(TaskDataRepository.class);
        Executor executor = new SynchronousExecutor();
        viewModel = new TaskViewModel(repository, executor);
        task = new Task(1, "exemple", 1);
        tasks = Arrays.asList(task, task, task);
        liveDataTasks.setValue(tasks);
    }

    @Test
    public void gettingAllTasks_interactWithRepository_WithSuccess() {
        //given
        when(repository.getAllTasks()).thenReturn(mock(LiveData.class));
        //when
        viewModel.getAllTasks();
        //then
        verify(repository, atLeastOnce()).getAllTasks();
    }

    @Test
    public void gettingAllTasks_WithSuccess() throws InterruptedException {
        //given
        when(repository.getAllTasks()).thenReturn(liveDataTasks);
        Observer observer = mock(Observer.class);
        viewModel.getAllTasks().observeForever(observer);
        //then
        List<Task> actual = LiveDataTestUtil.getValue(this.viewModel.getAllTasks());
        assertEquals(tasks, actual);
    }

    @Test
    public void gettingAllTasks_interactWithRepository_doesNotCreateTask() {
        //given
        when(repository.getAllTasks()).thenReturn(mock(LiveData.class));
        task = mock(Task.class);
        //when
        viewModel.getAllTasks();
        //then
        verify(repository).getAllTasks();
        verify(repository, never()).createTask(task);
    }

    @Test
    public void creatingTask_interactWithRepository_WithSuccess() {
        //when
        viewModel.createTask(task);
        //then
        verify(repository).createTask(task);
    }

    @Test
    public void updatingTask_interactWithRepository_WithSuccess() {
        //when
        viewModel.updateTask(task);
        //then
        verify(repository).updateTask(task);
    }

    @Test
    public void deletingTask_interactWithRepository_WithSuccess() {
        //when
        viewModel.deleteTask(task);
        //then
        verify(repository).deleteTask(task);
    }


}


