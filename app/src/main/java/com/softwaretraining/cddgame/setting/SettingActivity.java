package com.softwaretraining.cddgame.setting;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.softwaretraining.cddgame.R;
import com.softwaretraining.cddgame.music.BackgroundMusic;

import java.lang.reflect.Field;

public class SettingActivity extends Activity implements SettingContract.View {

    private static R.id R_ID_INSTANCE = null;
    private static R.drawable R_DRAWABLE_INSTANCE = null;

    private SettingContract.Presenter mSettingPresenter;

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
        setContentView(R.layout.activity_setting);

        initSettingView();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.mSettingPresenter = presenter;
    }

    public void onClickButtonBack(View view) {
        this.finish();
    }

    private void initSettingView() {
        new SettingPresenter(this);
        initMusicButton();
        initEffectButton();

        initSetting();
    }

    private void initMusicButton() {
        try {
            for (int i = 0; i < 4; ++i) {
                final Button button = getButtonSetting("Music", i);
                final int finalI = i;
                button.setOnClickListener(view -> onClickButtonSetting("Music", finalI, button));
            }
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void initEffectButton() {
        try {
            for (int i = 0; i < 4; ++i) {
                final Button button = getButtonSetting("Effect", i);
                final int finalI = i;
                button.setOnClickListener(view -> onClickButtonSetting("Effect", finalI, button));
            }
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void initSetting() {
        try {
            getButtonSetting("Music", mSettingPresenter.getMusicLevel()).performClick();
            getButtonSetting("Effect", mSettingPresenter.getEffectLevel()).performClick();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onClickButtonSetting(String type, int index, Button button) {

        for (int i = 0; i < index; ++i) {
            showButton(type, i, true);
            if (i != 0) showProgress(type, i, true);
        }

        mSettingPresenter.setLevel(type, index);
        try {
            button.setBackground(getDrawableButtonSelected());
            if (index != 0) {
                getImageViewProgress(type, index).setImageDrawable(getDrawableProgress(true));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = index + 1; i < 4; ++i) {
            showButton(type, i, false);
            showProgress(type, i, false);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showButton(String type, int index, boolean isReach) {
        try {
            getButtonSetting(type, index).setBackground(getDrawableButton(index, isReach));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void showProgress(String type, int levelIndex, boolean isReach) {
        try {
            getImageViewProgress(type, levelIndex).setImageDrawable(getDrawableProgress(isReach));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshMusic() {
        BackgroundMusic.refreshVolume();
    }

    @Override
    public void refreshEffect() {

    }

    private Button getButtonSetting(String type, int index) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("button" + type + "Level" + String.valueOf(index));
        return findViewById(field.getInt(instance));
    }

    private ImageView getImageViewProgress(String type, int levelIndex) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("imageView" + type + "Progress" + String.valueOf(levelIndex - 1) + "To" + String.valueOf(levelIndex));
        return findViewById(field.getInt(instance));
    }

    private Drawable getDrawableButton(int level, boolean isReach) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("setting_level_" + String.valueOf(level) + "_" + String.valueOf(isReach));
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private Drawable getDrawableButtonSelected() throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("setting_level_selected");
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private Drawable getDrawableProgress(boolean isReach) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("setting_bgm_progress_" + String.valueOf(isReach));
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private static R.id getRidInstance() {
        if (R_ID_INSTANCE == null) R_ID_INSTANCE = new R.id();
        return R_ID_INSTANCE;
    }

    private static R.drawable getRDrawInstance() {
        if (R_DRAWABLE_INSTANCE == null) R_DRAWABLE_INSTANCE = new R.drawable();
        return R_DRAWABLE_INSTANCE;
    }

}
