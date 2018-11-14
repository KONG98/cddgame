package com.softwaretraining.cddgame.game;

import com.softwaretraining.cddgame.message.CommunicateServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class GameModel {

    private boolean isClose = false;
    private GameListener mGameListener;

    private ExecutorService pool;
    private Callable callable;
    private Future future;

    GameModel() {
        pool = Executors.newFixedThreadPool(1);
    }

    public void setGameListener(GameListener gameListener) {
        this.mGameListener = gameListener;
    }

    public void sendPlayCardMessage(String message) {
        if (isClose) return;
        callable = new CommunicateServer("game", "play$" + message);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendOtherPlayerInfoRequest(String thisname) {
        if (isClose) return;
        callable = new CommunicateServer("game", "other$" + thisname);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendPlayerHandRequest(String name) {
        if (isClose) return;
        callable = new CommunicateServer("game", "card$" + name);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendOtherPlayCardRequest(String name) {
        if (isClose) return;
        callable = new CommunicateServer("game", "otherplay$" + name);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendFirstPlayerRequest(String name) {
        if (isClose) return;
        callable = new CommunicateServer("game", "first$" + name);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendUserScoreRequest(String name) {
        if (isClose) return;
        callable = new CommunicateServer("game", "score$" + name);
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendUserGainScore(String name, int score) {
        if (isClose) return;
        callable = new CommunicateServer("game", "gainscore$" + name + "$" + String.valueOf(score));
        future = pool.submit(callable);
        listenServerResponse();
    }

    public void sendUserBackToWait(String name) {
        if (isClose) return;
        callable = new CommunicateServer("wait", "back$" + name);
        future = pool.submit(callable);
    }

    private void listenServerResponse() {
        if (isClose) return;
        String message;
        try {
            message = future.get().toString();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            message = "java.lang.InterruptedException";
        } catch (ExecutionException ee) {
            ee.printStackTrace();
            message = "java.util.concurrent.ExecutionException";
        }
        if (isClose) return;
        mGameListener.responseMessage(message);
    }

    public void close() {
        isClose = true;
        mGameListener = null;
        pool = null;
        callable = null;
        future = null;
    }

}
