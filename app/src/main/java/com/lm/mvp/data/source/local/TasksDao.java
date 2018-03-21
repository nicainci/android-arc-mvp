package com.lm.mvp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lm.mvp.data.Task;

import java.util.List;

/**
 * @Author: LM
 * @Date: 2018/3/19
 * @Description:
 */
@Dao
public interface TasksDao {

    @Query("SELECT * FROM Tasks")
    List<Task> getTasks();

    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    Task getTaskById(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("UPDATE tasks SET completed = :completed WHERE entryid = :taskId")
    void updateCompleted(String taskId, boolean completed);

    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
    int deleteTaskById(String taskId);

    @Query("DELETE FROM Tasks")
    void deleteTasks();

    @Query("DELETE FROM Tasks WHERE completed = 1")
    int deleteCompletedTasks();
}
