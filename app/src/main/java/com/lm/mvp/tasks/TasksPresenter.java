package com.lm.mvp.tasks;

import android.support.annotation.NonNull;

import com.lm.mvp.data.Task;
import com.lm.mvp.data.source.TasksDataSource;
import com.lm.mvp.data.source.TasksRepository;
import com.lm.mvp.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LM
 * @Date: 2018/3/19
 * @Description:
 */

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull TasksRepository tasksRepository, @NonNull TasksContract.View tasksView) {
        mTasksRepository = Utils.checkNotNull(tasksRepository);
        mTasksView = Utils.checkNotNull(tasksView);
        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            mTasksView.showSuccessfullySavedMessage();
//        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<Task>();
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (!task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                    }
                    if (!mTasksView.isActive()) {
                        return;
                    }
                    if (showLoadingUI) {
                        mTasksView.setLoadingIndicator(false);
                    }
                    processTasks(tasksToShow);
                }
            }

            private void processTasks(List<Task> tasks) {
                if (tasks.isEmpty()) {
                    processEmptyTasks();
                } else {
                    mTasksView.showTasks(tasks);

                    showFilterLabel();
                }
            }

            private void processEmptyTasks() {
                switch (mCurrentFiltering) {
                    case ACTIVE_TASKS:
                        mTasksView.showNoActiveTasks();
                        break;
                    case COMPLETED_TASKS:
                        mTasksView.showNoCompletedTasks();
                        break;
                    default:
                        mTasksView.showNoTasks();
                        break;
                }
            }

            private void showFilterLabel() {
                switch (mCurrentFiltering) {
                    case ACTIVE_TASKS:
                        mTasksView.showActiveFilterLabel();
                        break;
                    case COMPLETED_TASKS:
                        mTasksView.showCompletedFilterLabel();
                        break;
                    default:
                        mTasksView.showAllFilterLabel();
                        break;
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }
                mTasksView.showLoadingTasksError();
            }
        });
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        Utils.checkNotNull(requestedTask);
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        Utils.checkNotNull(completedTask);
        mTasksRepository.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        Utils.checkNotNull(activeTask);
        mTasksRepository.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

}
