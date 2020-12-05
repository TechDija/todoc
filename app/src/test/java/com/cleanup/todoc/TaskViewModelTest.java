package com.cleanup.todoc;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)

public class TaskViewModelTest {
    private TaskViewModel viewModel;
    private TaskDataRepository repository;
    private Executor executor ;
    private Task task;

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup(){
        repository = mock(TaskDataRepository.class);
        executor = mock(Executor.class);
        viewModel = new TaskViewModel(repository, executor);
    }

    @Test
    public void gettingAllTasks_WithSuccess(){
        when(repository.getAllTasks()).thenReturn(mock(LiveData.class));
        viewModel.getAllTasks();
        verify(repository, atLeastOnce()).getAllTasks();
    }

    @Test
    public void gettingAllTasks_doesNotCreateTask() {
        when(repository.getAllTasks()).thenReturn(mock(LiveData.class));
        task = mock(Task.class);

        viewModel.getAllTasks();
        verify(repository).getAllTasks();
        verify(repository, never()).createTask(task);
    }

    @Test
    public void refresh() {
        verifyNoMoreInteractions(repository);
    }


    @Test
    public void creatingTask_WithSuccess() throws InterruptedException {
        task = new Task(1, "exemple", 1);
        viewModel.createTask(task);
        Thread.sleep(500);
        verify(repository, atLeast(1)).createTask(task);
    }

}
