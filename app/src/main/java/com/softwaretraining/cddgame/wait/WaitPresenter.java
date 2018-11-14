package com.softwaretraining.cddgame.wait;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class WaitPresenter implements WaitContract.Presenter, WaitListener {

    private boolean isClose = false;

    private Reference<WaitContract.View> viewReference;
    private WaitModel mWaitModel;

    private boolean isWaiting = true;
    private static String lastMessage;

    private int roomNumber;

    WaitPresenter(WaitModel waitModel, WaitContract.View view) {
        mWaitModel = waitModel;
        viewReference = new WeakReference<>(view);

        mWaitModel.setWaitListener(this);
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stopWaiting() {
        isWaiting = false;
    }

    @Override
    public void getOnlineUser(final String name) {

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mWaitModel.requestOnlineUser(name);
            }
        };

        new Thread(() -> {
            try {
                while (isWaiting){
                    handler.sendMessage(new Message());
                    Thread.sleep(2000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void userReady(final boolean ready, final String name) {
        new Thread(() -> mWaitModel.sendReadyMessage(ready, name)).start();
    }

    @Override
    public void offline(final String name) {
        mWaitModel.sendOfflineMessage(name);
    }

    @Override
    public void responseMessage(String username, String message) {
        String[] strAry = message.split("\\$");
        int numOfOnlineUser;
        if ("on".equals(strAry[0])) {
            onlineUserInfoHandler(username, message);
        } else if ("ready".equals(strAry[0])) {
            try {
                setUserRoomNumber(Integer.parseInt(strAry[1]));
            } catch (NumberFormatException e) {
                System.out.println("java.lang.NumberFormatException: Server reply message :" + message);
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("java.lang.IndexOutOfBoundsException: Server reply message :" + message);
                e.printStackTrace();
            }
        } else if ("game".equals(strAry[0])) {
            gamePlayerHandler(username, message);
        } else if ("offline".equals(strAry[0])) {

        } else if ("online".equals(strAry[0])) {

        } else {

            try {
                numOfOnlineUser = Integer.parseInt(strAry[0]);
            } catch (NumberFormatException e) {
                System.out.println("java.lang.NumberFormatException: Server reply message :" + message);
                numOfOnlineUser = 1;
            } catch (IndexOutOfBoundsException e) {
                numOfOnlineUser = 1;
            }

            String[] nameAry = new String[numOfOnlineUser];

            try {
                for (int i = 0; i < numOfOnlineUser; ++i) {
                    nameAry[i] = strAry[2 * i + 1] + "         --" + strAry[2 * i + 2];
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("java.lang.IndexOutOfBoundsException: Server reply message :" + message);
                e.printStackTrace();
                numOfOnlineUser = 1;
                nameAry = new String[]{username};
            }

            if (!message.equals(lastMessage)) {
                viewReference.get().showNumberOfOnlineUser(numOfOnlineUser);
                viewReference.get().showOnlineUserName(nameAry);
                lastMessage = message;
            }

        }
    }

    @Override
    public void close() {
        stopWaiting();
        isClose = true;
        mWaitModel.close();
        mWaitModel = null;
        viewReference.clear();
        viewReference = null;
        lastMessage = null;
    }

    private void setUserRoomNumber(final int roomNumber) {
        this.roomNumber = roomNumber;
    }

    private void onlineUserInfoHandler(String username, String message) {
        String[] strAry = message.split("\\$");
        int numOfOnlineUser;
        try {
            numOfOnlineUser = Integer.parseInt(strAry[1]);
        } catch (IndexOutOfBoundsException e) {
            numOfOnlineUser = 1;
        } catch (NumberFormatException e) {
            numOfOnlineUser = 1;
        }

        String[] nameAry = new String[numOfOnlineUser];

        try {
            for (int i = 1; i <= numOfOnlineUser; ++i) {
                nameAry[i - 1] = strAry[2 * i] + "         --" + strAry[2 * i + 1];
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("java.lang.IndexOutOfBoundsException: Server reply message :" + message);
            e.printStackTrace();
            numOfOnlineUser = 1;
            nameAry = new String[]{username};
        }

        if (!message.equals(lastMessage)) {
            viewReference.get().showNumberOfOnlineUser(numOfOnlineUser);
            viewReference.get().showOnlineUserName(nameAry);
            lastMessage = message;
        }
    }

    private void gamePlayerHandler(String username, String message) {
        String[] strAry = message.split("\\$");
        String[] playerNameAry = new String[3];
        int userIndex;
        try {
            for (userIndex = 1; userIndex < 5; ++userIndex) {
                if (username.equals(strAry[userIndex])) {
                    break;
                }
            }
            for (int i = userIndex + 1; i < 5; ++i) {
                playerNameAry[i - userIndex - 1] = strAry[i];
            }
            System.arraycopy(strAry, 1, playerNameAry, 3 - userIndex + 1, userIndex - 1);
            viewReference.get().showGameView(playerNameAry[0], playerNameAry[1], playerNameAry[2]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("java.lang.IndexOutOfBoundsException: Server reply message :" + message);
            e.printStackTrace();
        }
    }

}

