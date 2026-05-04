package com.example.todolist.database;
import android.content.Context;

import com.example.todolist.structure.Task;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.todolist.structure.Task;
import com.example.todolist.does.TaskDao;
@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public static TaskDatabase tsdatabase;

    public abstract TaskDao TaskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (tsdatabase == null) {
            tsdatabase = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return tsdatabase;
    }
}
