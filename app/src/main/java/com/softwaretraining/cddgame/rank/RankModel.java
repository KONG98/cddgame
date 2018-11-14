package com.softwaretraining.cddgame.rank;

import com.softwaretraining.cddgame.message.CommunicateServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class RankModel {

    private RankListener rankListener;

    private final ExecutorService pool;
    private Callable callable;
    private Future future;

    RankModel() {
        pool = Executors.newSingleThreadExecutor();
    }

    public void setRankListener(RankListener rankListener) {
        this.rankListener = rankListener;
    }

    public void listRankList(String username) {
        callable = new CommunicateServer("game", "rank" + "$" + username);
        future = pool.submit(callable);
        responseRank();
    }

    private void responseRank() {
        String message;
        try {
            message = future.get().toString();
        } catch (InterruptedException ie) {
            message = "网络受到了波动，请稍后尝试重新登录";
        } catch (ExecutionException ee) {
            message = "java.util.concurrent.ExecutionException";
        }
        rankListener.responseMessage(message);
    }

}
