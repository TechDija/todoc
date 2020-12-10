package com.cleanup.todoc;

import com.cleanup.todoc.model.Task;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {
    @Test
    public void test_projects() {
        final Task task1 = new Task(1L, "task 1", new Date().getTime());
        final Task task2 = new Task(2L, "task 2", new Date().getTime());
        final Task task3 = new Task(3L, "task 3", new Date().getTime());
        final Task task4 = new Task(4L, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", Objects.requireNonNull(task1.getProject()).getName());
        assertEquals("Projet Lucidia", Objects.requireNonNull(task2.getProject()).getName());
        assertEquals("Projet Circus", Objects.requireNonNull(task3.getProject()).getName());
        assertNull(task4.getProject());
    }
}
