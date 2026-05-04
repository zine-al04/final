package com.example.todolist.structure;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    int id ;
    String title;
    String date;
    String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Task (String time , String date , String title , int id){
        this.date=date;
        this.time=time;
        this.id=id;
        this.title=title;
    }
    @Ignore
    public Task (String time ,String date , String title){
        this.date=date;
        this.time=time;
        this.title=title;
    }

}
