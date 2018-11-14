package com.softwaretraining.cddgame.login;

import android.content.Context;

import com.softwaretraining.cddgame.message.LocalUsername;
import com.softwaretraining.cddgame.message.MessageFilter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class LoginPresenter implements LoginContract.Presenter, LoginListener {

    private final LoginModel mLoginModel;

    private final Reference<LoginContract.View> viewReference;
    private Context context;

    LoginPresenter(LoginModel loginModel, LoginContract.View loginView) {
        mLoginModel = loginModel;
        viewReference = new WeakReference<>(loginView);

        mLoginModel.setLoginListener(this);
        loginView.setPresenter(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void start() {

        viewReference.get().setRecentUsedName(LocalUsername.getLocalUsername(context));
    }

    @Override
    public void login(String name, String password) {
        new Thread(() -> mLoginModel.login(name, MessageFilter.getEncode(password))).start();
        LocalUsername.setLocalUsername(context, name);
    }

    @Override
    public void responseMessage(String message) {
        try {
            String[] strAry = message.split("\\$");
            switch (strAry[0]) {
                case "success":
                    viewReference.get().loginSuccess(strAry[1]);
                    break;
                case "fail":
                    viewReference.get().showServerResponse(strAry[2]);
                    break;
                default:
                    viewReference.get().showServerResponse(message);
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(message);
            viewReference.get().showServerResponse(message);
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkName(String name) {
        if (name == null || name.equals("")) {
            viewReference.get().showNameLimit("用户名不能为空");
            return false;
        } else if (name.length() > 30) {
            viewReference.get().showNameLimit("用户名长度不能大于30个字符");
            return false;
        } else if (name.contains("$")) {
            viewReference.get().showNameLimit("用户名不允许有特殊符号");
            return false;
        } else if (name.contains(" ")) {
            viewReference.get().showNameLimit("用户名不允许有空格");
            return false;
        } else viewReference.get().showNameLimit("");
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (password == null || password.equals("")) {
            viewReference.get().showPasswordLimit("密码不能为空");
            return false;
        } else if (password.contains(" ")) {
            viewReference.get().showPasswordLimit("密码不允许有空格");
            return false;
        } else viewReference.get().showPasswordLimit("");
        return true;
    }

}
