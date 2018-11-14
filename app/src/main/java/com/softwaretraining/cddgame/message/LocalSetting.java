package com.softwaretraining.cddgame.message;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LocalSetting {

    private final static String fileName = "setting.txt";

    private static int musicLevel = -1;
    private static int effectLevel = -1;

    private static Context context = null;

    public static void initSetting(Context context) {
        LocalSetting.context = context;

        readSetting();
    }

    public static int getMusicLevel() {
        if (musicLevel < 0 || musicLevel > 3) setMusicLevel(2);
        return musicLevel;
    }

    public static void setMusicLevel(int musicLevel) {
        if (musicLevel > 3) {
            LocalSetting.musicLevel = 3;
        } else if (musicLevel < 0) {
            LocalSetting.musicLevel = 0;
        } else {
            LocalSetting.musicLevel = musicLevel;
        }
        saveSetting();
    }

    public static int getEffectLevel() {
        if (effectLevel < 0 || effectLevel > 3) setEffectLevel(2);
        return effectLevel;
    }

    public static void setEffectLevel(int effectLevel) {
        if (effectLevel > 3) {
            LocalSetting.effectLevel = 3;
        } else if (effectLevel < 0) {
            LocalSetting.effectLevel = 0;
        } else {
            LocalSetting.effectLevel = effectLevel;
        }
        saveSetting();
    }

    private static void readSetting() {
        String settingString = null;

        try {
            FileInputStream in = context.openFileInput(fileName);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            settingString = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (settingString == null) {
            setMusicLevel(2);
            setEffectLevel(2);
        } else {
            String[] settingAry = settingString.split("\\$");
            try {
                setMusicLevel(Integer.parseInt(settingAry[0]));
                setEffectLevel(Integer.parseInt(settingAry[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveSetting() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(musicLevel).append("$");
        stringBuilder.append(effectLevel).append("$");

        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
