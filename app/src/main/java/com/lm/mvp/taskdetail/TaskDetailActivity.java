package com.lm.mvp.taskdetail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lm.mvp.R;
import com.lm.mvp.data.source.TasksDataSource;
import com.lm.mvp.data.source.TasksRepository;
import com.lm.mvp.data.source.local.TasksLocalDataSource;
import com.lm.mvp.data.source.local.ToDoDatabase;
import com.lm.mvp.data.source.remote.TasksRemoteDataSource;
import com.lm.mvp.util.ActivityUtils;
import com.lm.mvp.util.AppExecutors;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public class TaskDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail_act);
        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.contentFrame);
        }

        TasksRepository repository = TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(), ToDoDatabase.getInstance(this).taskDao()));

        new TaskDetailPresenter(taskId,repository,taskDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
