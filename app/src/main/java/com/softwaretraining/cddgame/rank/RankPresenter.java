package com.softwaretraining.cddgame.rank;

import android.os.Looper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RankPresenter implements RankContract.Presenter, RankListener {

    private final Reference<RankContract.View> viewReference;
    private final RankModel mRankModel;

    RankPresenter(RankModel rankModel, RankContract.View view) {
        mRankModel = rankModel;
        viewReference = new WeakReference<>(view);

        rankModel.setRankListener(this);
        view.setPresenter(this);
    }

    @Override
    public void responseMessage(String message) {
        try {
            if (message.split("\\$")[0].equals("rank")) {
                rankMessageHandler(message);
            } else {
                System.out.println(message);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(message);
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void getRank(String username) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(() -> mRankModel.listRankList(username)).start();
        } else {
            mRankModel.listRankList(username);
        }
    }

    private void rankMessageHandler(String message) {
        String[] strAry = message.split("\\$");
        ArrayList<RankList> rankListArrayList = new ArrayList<>();

        RankList rankListHeader = new RankList("排名", "玩家名", "积分");
        rankListArrayList.add(rankListHeader);

        try {
            for (int seq = 1; seq <= 50; ++seq) {
                RankList rankListObject = new RankList(String.valueOf(seq), strAry[2 * seq - 1], strAry[2 * seq]);
                rankListArrayList.add(rankListObject);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("注册玩家不足50人");
        }

        viewReference.get().showRank(rankListArrayList);
    }

}
