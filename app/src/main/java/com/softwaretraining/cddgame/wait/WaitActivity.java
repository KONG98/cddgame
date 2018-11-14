package com.softwaretraining.cddgame.wait;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaretraining.cddgame.R;
import com.softwaretraining.cddgame.message.LocalSetting;

import java.lang.reflect.Field;

public class WaitActivity extends Activity implements WaitContract.View {

    private WaitContract.Presenter mWaitPresenter = null;
    private String username;
    private boolean isReady = false;

    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT < 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        initWaitView();
        initBackgroundMusic();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClickReadyButton(View view) {
        mWaitPresenter.userReady(!isReady, username);
        isReady = !isReady;
        Button readyButton = findViewById(R.id.buttonReady);
        try {
            if (isReady) {
                R.drawable instance = new R.drawable();
                Field field = instance.getClass().getField("btn_no_ready");
                readyButton.setBackground(this.getResources().getDrawable(field.getInt(instance)));
            } else {
                R.drawable instance = new R.drawable();
                Field field = instance.getClass().getField("btn_ready");
                readyButton.setBackground(this.getResources().getDrawable(field.getInt(instance)));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onClickBackButton(View view) {
        this.toLoginView();
    }

    public void onClickToSettingButton(View view) {
        this.toSettingView();
    }

    public void onClickToRankButton(View view) {
        toRankView();
    }

    @Override
    public void setPresenter(WaitContract.Presenter presenter) {
        this.mWaitPresenter = presenter;
    }

    @Override
    public void showNumberOfOnlineUser(int online) {
        this.runOnUiThread(() -> {
            TextView numTv = findViewById(R.id.textViewNumOfOnlineUser);
            numTv.setText("当前大厅人数： " + online);
        });
    }

    @Override
    public void showOnlineUserName(String[] onlineUser) {
        this.runOnUiThread(() -> {
            ListView listView = findViewById(R.id.listView);
            ArrayAdapter<String> arrayAdapter;
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlineUser);
            listView.setAdapter(arrayAdapter);
        });
    }

    @Override
    public void showGameView(final String playerName1, final String playerName2, final String playerName3) {
        toGameView(playerName1, playerName2, playerName3);
    }

    private void initWaitView() {
        new WaitPresenter(new WaitModel(), this);
        setUsername();
        mWaitPresenter.getOnlineUser(username);

        LocalSetting.initSetting(getApplicationContext());
    }

    private void initBackgroundMusic() {
        Intent intent = new Intent(WaitActivity.this, com.softwaretraining.cddgame.music.BackgroundMusic.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "waiting");
        intent.putExtras(bundle);
        startService(intent);
    }

    @SuppressWarnings("ConstantConditions")
    private void setUsername() {
        try {
            this.username = getIntent().getExtras().getString("Username");
        } catch (NullPointerException e) {
            this.username = "";
            e.printStackTrace();

        }
        TextView textView = findViewById(R.id.textViewWaitingUsername);
        textView.setText("当前用户名\n" + username);
    }

    private void toLoginView() {
        mWaitPresenter.offline(username);
        mWaitPresenter.close();
        mWaitPresenter = null;
        System.gc();
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.wait.WaitActivity.this, com.softwaretraining.cddgame.login.LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void toGameView(final String player1, final String player2, final String player3) {
        mWaitPresenter.close();
        mWaitPresenter = null;
        System.gc();
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.wait.WaitActivity.this, com.softwaretraining.cddgame.game.GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Player0", username);
        bundle.putString("Player1", player1);
        bundle.putString("Player2", player2);
        bundle.putString("Player3", player3);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent(WaitActivity.this, com.softwaretraining.cddgame.music.BackgroundMusic.class);
        stopService(intent);
        super.finish();
    }

    private void toSettingView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.wait.WaitActivity.this, com.softwaretraining.cddgame.setting.SettingActivity.class);
        startActivity(intent);
    }

    private void toRankView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.wait.WaitActivity.this, com.softwaretraining.cddgame.rank.RankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
