package com.lm.mvp.statistics;

import android.support.annotation.NonNull;

import com.lm.mvp.data.Task;
import com.lm.mvp.data.source.TasksDataSource;
import com.lm.mvp.data.source.TasksRepository;
import com.lm.mvp.util.Utils;

import java.util.List;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public class StatisticsPresenter implements StatisticsContract.Presenter {
    private final TasksRepository mTasksRepository;

    private final StatisticsContract.View mStatisticsView;

    public StatisticsPresenter(@NonNull TasksRepository tasksRepository,
                               @NonNull StatisticsContract.View statisticsView) {
        mTasksRepository = Utils.checkNotNull(tasksRepository);
        mStatisticsView = Utils.checkNotNull(statisticsView);

        mStatisticsView.setPresenter(this);
    }


    @Override
    public void start() {
        loadStatistics();
    }
    private void loadStatistics() {
        mStatisticsView.setProgressIndicator(true);

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int activeTasks = 0;
                int completedTasks = 0;

                // We calculate number of active and completed tasks
                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks += 1;
                    } else {
                        activeTasks += 1;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.setProgressIndicator(false);

                mStatisticsView.showStatistics(activeTasks, completedTasks);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.showLoadingStatisticsError();
            }
        });
    }
}
