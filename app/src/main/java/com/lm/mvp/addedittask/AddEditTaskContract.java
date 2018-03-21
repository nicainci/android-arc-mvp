package com.lm.mvp.addedittask;

import com.lm.mvp.base.BasePresenter;
import com.lm.mvp.base.BaseView;

/**
 * @Author: LM
 * @Date: 2018/3/21
 * @Description:
 */

public interface AddEditTaskContract {

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }
}
