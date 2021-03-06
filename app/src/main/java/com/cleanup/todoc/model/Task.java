package com.cleanup.todoc.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * <p>Model for the tasks of the application.</p>
 *
 * @author Gaëtan HERFRAY
 */
@Entity(foreignKeys = @ForeignKey(entity = Project.class, parentColumns = "project_id", childColumns = "project_id", onDelete = ForeignKey.CASCADE))
public class Task {
    /**
     * The unique identifier of the task
     */
    @PrimaryKey(autoGenerate = true)
    private long task_id;

    /**
     * The unique identifier of the project associated to the task
     */

    private long project_id;

    /**
     * The name of the task
     */
    // Suppress warning because setName is called in constructor
    @SuppressWarnings("NullableProblems")
    @NonNull
    private String name;

    /**
     * The timestamp when the task has been created
     */
    private long creationTimestamp;

    /**
     * Instantiates a new Task.
     *
     * @param project_id        the unique identifier of the project associated to the task to set
     * @param name              the name of the task to set
     * @param creationTimestamp the timestamp when the task has been created to set
     */


    public Task(long project_id, @NonNull String name, long creationTimestamp) {
        this.setTask_id(task_id);
        this.setProjectId(project_id);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    /**
     * Returns the unique identifier of the task.
     *
     * @return the unique identifier of the task
     */
    public long getTask_id() {
        return task_id;
    }

    public long getProject_id() {
        return project_id;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Sets the unique identifier of the task.
     *
     * @param task_id the unique idenifier of the task to set
     */
    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }


    /**
     * Sets the unique identifier of the project associated to the task.
     *
     * @param projectId the unique identifier of the project associated to the task to set
     */
    public void setProjectId(long projectId) {
        this.project_id = projectId;
    }

    /**
     * Returns the project associated to the task.
     *
     * @return the project associated to the task
     */
    @Nullable
    public Project getProject() {
        return Project.getProjectById(project_id);
    }

    /**
     * Returns the name of the task.
     *
     * @return the name of the task
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     *
     * @param name the name of the task to set
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Sets the timestamp when the task has been created.
     *
     * @param creationTimestamp the timestamp when the task has been created to set
     */
    private void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}