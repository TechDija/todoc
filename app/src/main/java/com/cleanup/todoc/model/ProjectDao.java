package com.cleanup.todoc.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void createProject(Project project);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();

    @Query("SELECT * FROM Project WHERE project_id = :id")
    LiveData<Project> getProjectById(long id);
}

