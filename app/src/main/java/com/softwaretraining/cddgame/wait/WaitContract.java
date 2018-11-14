package com.softwaretraining.cddgame.wait;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

interface WaitContract {

    interface View extends BaseView<Presenter> {

        void showNumberOfOnlineUser(int online);

        void showOnlineUserName(String[] onlineUser);

        void showGameView(String player1, String player2, String player3);

    }

    interface Presenter extends BasePresenter {

        void getOnlineUser(String name);

        void userReady(boolean ready, String name);

        void offline(String name);

        void stopWaiting();

        void close();

    }
}
