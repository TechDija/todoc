package com.cleanup.todoc.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ActivityMainBinding;
import com.cleanup.todoc.databinding.DialogAddTaskBinding;
import com.cleanup.todoc.injections.Injection;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {
    /**
     * ViewBinding
     */
    ActivityMainBinding binding;
    DialogAddTaskBinding binder;
    /**
     * List of all projects available in the application
     */
    private List<Project> allProjects = new ArrayList<>();

    /**
     * List of all current tasks of the application
     */
    @NonNull
    private List<Task> allTasks;

    /**
     * The adapter which handles the list of tasks
     */
    private TasksAdapter adapter;

    /**
     * The sort method to be used to display tasks
     */
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     *
     * @Nullable private EditText dialogEditText = null;
     * <p>
     * /**
     * Spinner that allows the user to associate a project to a task
     * @Nullable private Spinner dialogSpinner = null;
     * <p>
     * /**
     * The RecyclerView which displays the list of tasks
     * <p>
     * // Suppress warning is safe because variable is initialized in onCreate
     * @SuppressWarnings("NullableProblems")
     * @NonNull private RecyclerView listTasks;
     * <p>
     * /**
     * The TextView displaying the empty state
     * <p>
     * // Suppress warning is safe because variable is initialized in onCreate
     * @SuppressWarnings("NullableProblems")
     * @NonNull private TextView lblNoTasks;
     * <p>
     * /**
     * The Viewmodel
     */
    private TaskViewModel mTaskViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();

        /**
         listTasks = findViewById(R.id.list_tasks);
         lblNoTasks = findViewById(R.id.lbl_no_task);
         */
        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                if (tasks != null) {
                    setListTasks(tasks);
                    adapter.updateTasks(tasks);
                    updateTasks(tasks);
                }
            }
        });

        mTaskViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                if (projects != null) {
                    setListProjects(projects);
                    adapter.updateProjects(projects);
                }
            }
        });

        configureRecyclerView();


        binding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }

        updateTasks(allTasks);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {
        mTaskViewModel.deleteTask(task);
        allTasks.remove(task);
        updateTasks(allTasks);
        adapter.notifyDataSetChanged();
    }

    private void setListTasks(List<Task> tasks) {
        this.allTasks = tasks;
    }

    private void setListProjects(List<Project> projects) {
        this.allProjects = projects;
    }

    /**
     * Retrieving Data from ViewModel through Injection
     */
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mTaskViewModel = new ViewModelProvider(this, mViewModelFactory).get(TaskViewModel.class);
    }

    /**
     * Configuring the recyclerView
     */
    private void configureRecyclerView() {
        adapter = new TasksAdapter(allTasks, this);
        binding.listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.listTasks.setAdapter(adapter);
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open

        if (binder.txtTaskName != null && binder.projectSpinner != null) {
            // Get the name of the task
            String taskName = binder.txtTaskName.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (binder.projectSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) binder.projectSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                binder.txtTaskName.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                //  Replace this by id of persisted task
                //long id = (long) (Math.random() * 50000);
                Task task = new Task(
                        //id
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                addTask(task);


                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();
/**
 dialogEditText = dialog.findViewById(R.id.txt_task_name);
 dialogSpinner = dialog.findViewById(R.id.project_spinner);
 */
        populateDialogSpinner();
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        mTaskViewModel.createTask(task);
        allTasks.add(task);
        updateTasks(allTasks);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<Task> allTasks) {
        if (allTasks.size() == 0) {
            binding.lblNoTask.setVisibility(View.VISIBLE);
            binding.listTasks.setVisibility(View.GONE);
        } else {
            binding.lblNoTask.setVisibility(View.GONE);
            binding.listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    //Collections.sort(tasks, new Task.TaskAZComparator());
                    mTaskViewModel.getAllTasksFromAToZ().observe(this, new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            adapter.updateTasks(tasks);
                        }
                    });
                    break;
                case ALPHABETICAL_INVERTED:
                    //Collections.sort(tasks, new Task.TaskZAComparator());
                    mTaskViewModel.getAllTasksFromZToA().observe(this, new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            adapter.updateTasks(tasks);
                        }
                    });
                    break;
                case RECENT_FIRST:
                    //Collections.sort(tasks, new Task.TaskRecentComparator());
                    mTaskViewModel.getAllTasksFromRecentToOld().observe(this, new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            adapter.updateTasks(tasks);
                        }
                    });
                    break;
                case OLD_FIRST:
                    //Collections.sort(tasks, new Task.TaskOldComparator());
                    mTaskViewModel.getAllTasksFromOldToRecent().observe(this, new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            adapter.updateTasks(tasks);
                        }
                    });
                    break;

            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        binder = DialogAddTaskBinding
                .inflate(LayoutInflater.from(this));
        final AlertDialog.Builder alertBuilder = new
                AlertDialog.Builder(this, R.style.Dialog);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                /**   binder.txtTaskName = null;
                 binder.projectSpinner  = null;
                 dialog = null;*/
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (binder.projectSpinner != null) {
            binder.projectSpinner.setAdapter(adapter);
        }
    }

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }
}
