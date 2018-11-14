package com.softwaretraining.cddgame.register;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void showServerResponse(String responseMessage);

        void showNameLimit(String limit);

        void showPasswordLimit(String limit);

        void showRepeatPasswordLimit(String limit);

        void registerSuccess(String username);

    }

    interface Presenter extends BasePresenter {

        void register(String name, String password);

        boolean checkName(String name);

        boolean checkPassword(String password);

        boolean checkRepeatPassword(String password, String repw);

    }

}
