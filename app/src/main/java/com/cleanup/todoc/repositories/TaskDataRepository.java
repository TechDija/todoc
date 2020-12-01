package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskDao;

import java.util.List;

public class TaskDataRepository {
    private final TaskDao taskDao;

    //------CONSTRUCTOR----------
    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    //-------GET-----------
    public LiveData<List<Task>> getAllTasks() {
        return this.taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasksFromAToZ() {
        return this.taskDao.getAllTasksFromAToZ();
    }

    public LiveData<List<Task>> getAllTasksFromZToA() {
        return this.taskDao.getAllTasksFromZToA();
    }

    public LiveData<List<Task>> getAllTasksFromRecentToOld() {
        return this.taskDao.getAllTasksFromRecentToOld();
    }

    public LiveData<List<Task>> getAllTasksFromOldToRecent() {
        return this.taskDao.getAllTasksFromOldToRecent();
    }

    //-------CREATE-----------
    public void createTask(Task task) {
        taskDao.insertTask(task);
    }

    //-------DELETE-----------
    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    //------UPDATE -----------
    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }
}
