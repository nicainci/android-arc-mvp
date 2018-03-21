package com.lm.mvp.taskdetail;

import com.lm.mvp.base.BasePresenter;
import com.lm.mvp.base.BaseView;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public interface TaskDetailContract {
    interface Presenter extends BasePresenter {
        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();
    }
}
