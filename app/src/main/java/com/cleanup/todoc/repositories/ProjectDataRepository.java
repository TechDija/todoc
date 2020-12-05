package com.cleanup.todoc.repositories;

//import com.cleanup.todoc.model.ProjectDao;
/**
public class ProjectDataRepository {

    final ProjectDao projectDao;

    //------CONSTRUCTOR--------

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    //------GET--------
    public LiveData<List<Project>> getAllProjects() {
        return this.projectDao.getAllProjects();
    }

    public LiveData<Project> getProjectById(long id) {
        return this.projectDao.getProjectById(id);
    }

    //--------CREATE-------
    public void createProject(Project project) {
        projectDao.createProject(project);
    }
}
*/