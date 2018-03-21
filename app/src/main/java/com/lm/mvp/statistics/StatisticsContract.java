package com.lm.mvp.statistics;

import com.lm.mvp.base.BasePresenter;
import com.lm.mvp.base.BaseView;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public interface StatisticsContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();

        boolean isActive();
    }
}
