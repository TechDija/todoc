package com.cleanup.todoc.injections;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import java.util.concurrent.Executor;

//import com.cleanup.todoc.repositories.ProjectDataRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final TaskDataRepository taskDataSource;
   // private final ProjectDataRepository projectDataSource;
    private final Executor mExecutor;

    public ViewModelFactory(TaskDataRepository taskDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
       // this.projectDataSource = projectDataSource;
        mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)){
            return (T) new TaskViewModel(taskDataSource, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}

