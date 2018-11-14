package com.softwaretraining.cddgame.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.softwaretraining.cddgame.R;
import com.softwaretraining.cddgame.message.LocalSetting;

import java.lang.reflect.Field;

public class BackgroundMusic extends Service {

    private static MediaPlayer mediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(this, getMusicResID(intent.getExtras().getString("name")));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
            mediaPlayer.setVolume((float) LocalSetting.getMusicLevel() / 3, (float) LocalSetting.getMusicLevel() / 3);
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer = null;
        super.onDestroy();
    }

    public static void refreshVolume() {
        mediaPlayer.setVolume((float) LocalSetting.getMusicLevel() / 3, (float) LocalSetting.getMusicLevel() / 3);
    }

    private int getMusicResID(String name) throws NoSuchFieldException, IllegalAccessException {
        R.raw instance = new R.raw();
        Field field = instance.getClass().getField(name);
        return field.getInt(instance);
    }

}
