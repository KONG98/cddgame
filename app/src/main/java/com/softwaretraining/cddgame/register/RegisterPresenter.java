package com.softwaretraining.cddgame.register;

import android.content.Context;

import com.softwaretraining.cddgame.message.LocalUsername;
import com.softwaretraining.cddgame.message.MessageFilter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class RegisterPresenter implements RegisterContract.Presenter, RegisterListener {

    private final RegisterModel mRegisterModel;

    private final Reference<RegisterContract.View> viewReference;

    private Context context;

    RegisterPresenter(RegisterModel registerModel, RegisterContract.View registerView) {
        mRegisterModel = registerModel;
        viewReference = new WeakReference<>(registerView);

        mRegisterModel.setRegisterListener(this);
        registerView.setPresenter(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void register(String name, String password) {
        new Thread(() -> mRegisterModel.register(name, MessageFilter.getEncode(password))).start();
    }

    @Override
    public void responseMessage(String username, String message) {
        try {
            String[] strAry = message.split("\\$");
            switch (strAry[0]) {
                case "success":
                    LocalUsername.setLocalUsername(context, strAry[1]);
                    viewReference.get().registerSuccess(strAry[1]);
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

    @Override
    public boolean checkRepeatPassword(String password, String repw) {
        if (repw == null || repw.equals("")) {
            viewReference.get().showRepeatPasswordLimit("重复密码不能为空");
            return false;
        } else if (!password.equals(repw)) {
            viewReference.get().showRepeatPasswordLimit("重复密码与密码不同");
            return false;
        } else viewReference.get().showRepeatPasswordLimit("");
        return true;
    }

}
