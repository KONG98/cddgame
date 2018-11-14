package com.softwaretraining.cddgame.setting;

import com.softwaretraining.cddgame.message.LocalSetting;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class SettingPresenter implements SettingContract.Presenter {

    private final Reference<SettingContract.View> viewReference;

    SettingPresenter(SettingContract.View view) {
        viewReference = new WeakReference<>(view);
        viewReference.get().setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public int getMusicLevel() {
        return LocalSetting.getMusicLevel();
    }

    @Override
    public int getEffectLevel() {
        return LocalSetting.getEffectLevel();
    }

    @Override
    public void setLevel(String type, int level) {
        switch (type) {
            case "Music":
                setMusicLevel(level);
                break;
            case "Effect":
                setEffectLevel(level);
                break;
            default:
                System.out.println(type + " Level: " + level);
        }
    }

    private void setMusicLevel(int musicLevel) {
        LocalSetting.setMusicLevel(musicLevel);
        viewReference.get().refreshMusic();
    }

    private void setEffectLevel(int effectLevel) {
        LocalSetting.setEffectLevel(effectLevel);
        viewReference.get().refreshEffect();
    }

}
