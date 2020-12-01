package com.cleanup.todoc.viewmodel;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor mExecutor;

    @Nullable
    private LiveData<List<Task>> allTasks;
    @Nullable
    private LiveData<List<Project>> allProjects;


    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        mExecutor = executor;
    }

    //-------------------
    //FOR PROJECTS
    //-------------------
    public LiveData<List<Project>> getAllProjects() {
        return projectDataSource.getAllProjects();
    }

    public LiveData<Project> getProjectById(long id) {
        return projectDataSource.getProjectById(id);
    }

    public LiveData<Project> getProjectByTask(Task task) {
        return projectDataSource.getProjectById(task.getProjectId());
    }

    public void createProject(final Project project) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                projectDataSource.createProject(project);
            }
        });
    }

    //--------------------
    //FOR TASKS
    //-------------------
    public LiveData<List<Task>> getAllTasks() {
        return taskDataSource.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasksFromAToZ() {
        return taskDataSource.getAllTasksFromAToZ();
    }
    public LiveData<List<Task>> getAllTasksFromZToA() {
        return taskDataSource.getAllTasksFromZToA();
    }
    public LiveData<List<Task>> getAllTasksFromRecentToOld() {
        return taskDataSource.getAllTasksFromRecentToOld();
    }
    public LiveData<List<Task>> getAllTasksFromOldToRecent() {
        return taskDataSource.getAllTasksFromOldToRecent();
    }

    public void createTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDataSource.createTask(task);
            }
        });
    }

    public void deleteTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDataSource.deleteTask(task);
            }
        });
    }

    public void updateTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDataSource.updateTask(task);
            }
        });
    }

    public LiveData<Project> getProject (final Task task) {
        return projectDataSource.getProjectById(task.getProjectId());
    }
}

