package com.softwaretraining.cddgame.register;

import com.softwaretraining.cddgame.message.CommunicateServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class RegisterModel {

    private final ExecutorService pool = Executors.newFixedThreadPool(1);
    private Callable callable;
    private Future future;
    private RegisterListener registerListener;

    RegisterModel() {

    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void register(final String name, String password) {
        callable = new CommunicateServer("register", name + "$" + password);
        future = pool.submit(callable);
        responseRegister(name);
    }

    private void responseRegister(final String username) {
        String message;
        try {
            message = future.get().toString();
        } catch (InterruptedException ie) {
            message = "网络受到了波动，请稍后尝试重新登录";
        } catch (ExecutionException ee) {
            message = "java.util.concurrent.ExecutionException";
        }
        registerListener.responseMessage(username, message);
    }
}
