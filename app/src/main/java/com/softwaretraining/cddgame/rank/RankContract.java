package com.softwaretraining.cddgame.rank;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

import java.util.List;

interface RankContract {

    interface View extends BaseView<Presenter> {

        void showRank(List<RankList> rankList);

    }

    interface Presenter extends BasePresenter {

        void getRank(String username);

    }

}
