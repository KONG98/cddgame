package com.softwaretraining.cddgame.setting;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

interface SettingContract {

    interface View extends BaseView<Presenter> {

        void refreshMusic();

        void refreshEffect();

    }

    interface Presenter extends BasePresenter {

        void setLevel(String type, int level);

        int getMusicLevel();

        int getEffectLevel();

    }

}
