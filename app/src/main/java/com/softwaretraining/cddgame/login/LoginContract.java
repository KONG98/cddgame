package com.softwaretraining.cddgame.login;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

interface LoginContract {

    interface View extends BaseView<Presenter> {

        void setRecentUsedName(String name);

        void showServerResponse(String responseMessage);

        void showNameLimit(String limit);

        void showPasswordLimit(String limit);

        void loginSuccess(String username);

    }

    interface Presenter extends BasePresenter {

        void login(String name, String password);

        boolean checkName(String name);

        boolean checkPassword(String password);

    }

}
