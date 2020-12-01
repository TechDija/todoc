package com.cleanup.todoc.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Task.class, Project.class}, version = 1)
public abstract class Database extends RoomDatabase {

    // -------SINGLETON--------
    private static volatile Database INSTANCE;

    //---------DAO--------------
    public abstract TaskDao taskDao();

    public abstract ProjectDao ProjectDao();

    //--------INSTANCE----------
    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "Todoc_database")
                            .addCallback(populateDatabase())
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    //-----------Callback----------
    private static Callback populateDatabase() {
        return new Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                new PopulateDatabaseAsyncTask(INSTANCE).execute();
            }

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDatabaseAsyncTask(INSTANCE).execute();
            }
        };
    }

    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProjectDao projectDao;

        private PopulateDatabaseAsyncTask(Database db) {
            this.projectDao = db.ProjectDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            projectDao.createProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
            projectDao.createProject(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
            projectDao.createProject(new Project(3L, "Projet Circus", 0xFFA3CED2));
            return null;
        }
    }

}
