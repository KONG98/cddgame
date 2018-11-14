package com.softwaretraining.cddgame.login;

import com.softwaretraining.cddgame.message.CommunicateServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class LoginModel {

    private LoginListener loginListener;

    private final ExecutorService pool = Executors.newFixedThreadPool(1);
    private Callable callable;
    private Future future;

    LoginModel() {

    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void login(String name, String password) {
        callable = new CommunicateServer("login", name + "$" + password);
        future = pool.submit(callable);
        responseLogin();
    }

    private void responseLogin() {
        String message;
        try {
            message = future.get().toString();
        } catch (InterruptedException ie) {
            message = "网络受到了波动，请稍后尝试重新登录";
        } catch (ExecutionException ee) {
            message = "java.util.concurrent.ExecutionException";
        }
        loginListener.responseMessage(message);
    }
}
