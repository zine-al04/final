package com.example.todolist.does;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.structure.Task;
import java.util.List;
@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM Task ORDER BY id DESC")
    List<Task> getAllTasks();

    @Query("DELETE FROM Task")
    void deleteAll();
}
