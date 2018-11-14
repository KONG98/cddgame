package com.softwaretraining.cddgame.wait;

import com.softwaretraining.cddgame.message.CommunicateServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class WaitModel {

    private boolean isClose = false;
    private WaitListener waitListener;

    private ExecutorService pool;
    private Callable callable;
    private Future future;

    WaitModel() {
        pool = Executors.newSingleThreadExecutor();
    }

    public void setWaitListener(WaitListener waitListener) {
        this.waitListener = waitListener;
    }

    public void requestOnlineUser(final String name) {
        if (isClose) {
            return;
        }
        callable = new CommunicateServer("wait", "online$" + name);
        future = pool.submit(callable);

        responseOnlineUser(name);
    }

    public void sendReadyMessage(final boolean ready, final String name) {
        if (isClose) {
            return;
        }
        if (ready) {
            callable = new CommunicateServer("wait", "ready$" + name);
        } else {
            callable = new CommunicateServer("wait", "noready$" + name);
        }
        try {
            future = pool.submit(callable);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return;
        }
        responseOnlineUser(name);
    }

    public void sendOfflineMessage(final String name) {
        if (isClose) {
            return;
        }
        callable = new CommunicateServer("wait", "offline$" + name);
        try {
            future = pool.submit(callable);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void responseOnlineUser(final String name) {
        if (isClose) {
            return;
        }
        String message;
        try {
            message = future.get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
            message = "java.lang.InterruptedException";
        } catch (ExecutionException e) {
            e.printStackTrace();
            message = "java.util.concurrent.ExecutionException";
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (isClose) return;
        waitListener.responseMessage(name, message);
    }

    public void close() {
        isClose = true;
        waitListener = null;
        pool = null;
        callable = null;
        future = null;
    }

}
