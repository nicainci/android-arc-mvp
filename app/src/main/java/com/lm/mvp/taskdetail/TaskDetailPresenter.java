package com.lm.mvp.taskdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lm.mvp.data.Task;
import com.lm.mvp.data.source.TasksDataSource;
import com.lm.mvp.data.source.TasksRepository;
import com.lm.mvp.util.Utils;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TaskDetailContract.View mTaskDetailView;

    @Nullable
    private String mTaskId;

    public TaskDetailPresenter(@Nullable String taskId,
                               @NonNull TasksRepository tasksRepository,
                               @NonNull TaskDetailContract.View taskDetailView) {
        mTaskId = taskId;
        mTasksRepository = Utils.checkNotNull(tasksRepository);
        mTaskDetailView = Utils.checkNotNull(taskDetailView);

        mTaskDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }

        mTaskDetailView.setLoadingIndicator(true);
        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.setLoadingIndicator(false);
                if (null == task) {
                    mTaskDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.showMissingTask();
            }
        });
    }

    private void showTask(@NonNull Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (TextUtils.isEmpty(title)) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (TextUtils.isEmpty(description)) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }
        mTaskDetailView.showCompletionStatus(task.isCompleted());
    }

    @Override
    public void editTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskDetailView.showEditTask(mTaskId);
    }

    @Override
    public void deleteTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTasksRepository.deleteTask(mTaskId);
        mTaskDetailView.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTasksRepository.completeTask(mTaskId);
        mTaskDetailView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTasksRepository.activateTask(mTaskId);
        mTaskDetailView.showTaskMarkedActive();
    }
}
