package com.softwaretraining.cddgame.message;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LocalUsername {

    private final static String fileName = "cddaccount.txt";

    private static String localUsername = null;

    public static String getLocalUsername(Context context) {
        readLocalUsername(context);
        return localUsername;
    }


    public static void setLocalUsername(Context context, String name) {
        localUsername = name;
        writeLocalUsername(context);
    }


    private static void readLocalUsername(Context context) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            localUsername = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeLocalUsername(Context context) {
        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(localUsername);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
