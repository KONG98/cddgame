package com.softwaretraining.cddgame.game;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaretraining.cddgame.R;
import com.softwaretraining.cddgame.message.LocalSetting;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameActivity extends Activity implements GameContract.View {

    private static R.id R_ID_INSTANCE = null;
    private static R.drawable R_DRAWABLE_INSTANCE = null;

    private GameContract.Presenter mGamePresenter = null;

    private String username;

    private CheckBox lastCheckBox;

    private SoundPool soundPool = null;
    private int effectId;

    private int[] index2cardID = null;

    private static R.id getRidInstance() {
        if (R_ID_INSTANCE == null) R_ID_INSTANCE = new R.id();
        return R_ID_INSTANCE;
    }

    private static R.drawable getRDrawInstance() {
        if (R_DRAWABLE_INSTANCE == null) R_DRAWABLE_INSTANCE = new R.drawable();
        return R_DRAWABLE_INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT < 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initGameView();
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        this.mGamePresenter = presenter;
    }

    private void onClickCard(View view, int cardIndex, CheckBox checkBox) {

        if (cardIndex <= 0 || cardIndex > 13) {
            System.out.println("Index out of range");
            return;
        }
        if (checkBox == null) {
            System.out.println("Null value: checkBox");
            return;
        }

        if (checkBox.isChecked()) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) checkBox.getLayoutParams();
            layoutParams.setMargins(8, 8, 8, 40);
            checkBox.setLayoutParams(layoutParams);
            mGamePresenter.selectCard(index2cardID[cardIndex]);
        } else {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) checkBox.getLayoutParams();
            layoutParams.setMargins(8, 8, 8, 14);
            checkBox.setLayoutParams(layoutParams);
            mGamePresenter.unSelectCard(index2cardID[cardIndex]);
        }
    }

    public void onClickPlayCardButton(View view) {
        mGamePresenter.playCard();
    }

    public void onClickPassButton(View view) {
        mGamePresenter.pass();
    }

    public void onClickReselectButton(View view) {
        mGamePresenter.reselectCard();
        try {
            for (int i = 1; i <= 13; ++i) {
                CheckBox checkBox = getCardCheckBoxByIndex(i);
                if (checkBox.isChecked()) {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) checkBox.getLayoutParams();
                    layoutParams.setMargins(8, 8, 8, 14);
                    checkBox.setLayoutParams(layoutParams);
                    checkBox.setChecked(false);
                }
            }
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    public void onClickPauseButton(View view) {
        if (findViewById(R.id.constraintLayoutPause).getVisibility() == View.VISIBLE) {
            onClickClosePauseButton(view);
        } else {
            findViewById(R.id.constraintLayoutPause).setVisibility(View.VISIBLE);
            onClickReselectButton(view);
            setCardClickable(false);
        }

    }

    public void onClickSettingButton(View view) {
        toSettingView();
    }

    public void onClickRankButton(View view) {
        toRankView();
    }

    public void onClickClosePauseButton(View view) {
        findViewById(R.id.constraintLayoutPause).setVisibility(View.GONE);
        setCardClickable(true);
    }

    public void onClickInPauseViewExitButton(View view) {
        mGamePresenter.exit();
        toWaitView();
    }

    public void onClickBackToWaitingButton(View view) {
        mGamePresenter.backToWait();
        toWaitView();
    }

    public void onClickSortCardsButton(View view) {
        mGamePresenter.sortCard();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) (ev.getRawX() + getNavigationBarHeight(this) * getStretchFactor());
        int y = (int) ev.getRawY();
        View root = getWindow().getDecorView();
        View findView = findViewByXY(root, x, y);
        CheckBox checkBox;
        if (findView instanceof CheckBox) {
            checkBox = (CheckBox) findViewByXY(root, x, y);
        } else {
            return super.dispatchTouchEvent(ev);
        }
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastCheckBox = checkBox;
                    checkBox.performClick();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (checkBox != lastCheckBox) {
                        lastCheckBox = checkBox;
                        checkBox.performClick();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void startGame() {
        initCheckBoxListener();

        showServerMessage("游戏即将在3秒内开始");
        hideServerMessageDelay(3000);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mGamePresenter.start();
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showDetailCards(int[] cards) {
        this.runOnUiThread(() -> {

            if (index2cardID == null) {
                findViewById(R.id.buttonSortCard).setVisibility(View.VISIBLE);
                index2cardID = new int[14];
            }

            try {
                for (int i = 0; i < cards.length; ++i) {
                    index2cardID[i + 1] = cards[i];
                    CheckBox checkBox = getCardCheckBoxByIndex(i + 1);
                    checkBox.setBackground(getDrawableCardByCardID(cards[i]));
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) checkBox.getLayoutParams();
                    layoutParams.setMargins(8, 8, 8, 14);
                    checkBox.setLayoutParams(layoutParams);
                    checkBox.setVisibility(View.VISIBLE);
                }
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }

            try {
                for (int i = cards.length; i < 13; ++i) {
                    CheckBox checkBox = getCardCheckBoxByIndex(i + 1);
                    checkBox.setVisibility(View.GONE);
                    checkBox.setClickable(false);
                }
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }

        });
    }

    @Override
    public void showNumberOfCards(PositionEnum playerIndex, int number) {
        this.runOnUiThread(() -> {
            try {
                getTextViewNumberOfCardsByPos(playerIndex).setText(String.valueOf(number));
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        });
    }

    @Override
    public void hideLastTurnPlayed(PositionEnum player) {
        this.runOnUiThread(() -> {
            try {
                ConstraintLayout constraintLayout = getConstraintLayoutPlayedCardByPos(player);
                constraintLayout.setVisibility(View.GONE);
                getImageViewPass(player).setVisibility(View.GONE);
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showPlayerPlayCards(PositionEnum player, int[] playCards) {
        this.runOnUiThread(() -> {
            try {
                if (player != PositionEnum.THIS) {
                    getImageViewListenPlayer(player).setVisibility(View.GONE);
                }
                if (playCards.length == 0) {
                    getImageViewPass(player).setVisibility(View.VISIBLE);
                } else {
                    ConstraintLayout constraintLayout = getConstraintLayoutPlayedCardByPos(player);
                    for (int i = 1; i <= playCards.length; ++i) {
                        ImageView imageView = getImageViewPlayedCard(player, i);
                        imageView.setBackground(getDrawableCardByCardID(playCards[i - 1]));
                        imageView.setVisibility(View.VISIBLE);
                    }
                    for (int i = playCards.length + 1; i <= 5; ++i) {
                        getImageViewPlayedCard(player, i).setVisibility(View.GONE);
                    }
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
            playSoundEffect();
        });
    }

    @Override
    public void promotePlayerPlayCard() {
        this.runOnUiThread(() -> {
            Button buttonPlayCard = findViewById(R.id.buttonPlayCard);
            Button buttonPass = findViewById(R.id.buttonPass);
            Button buttonReselect = findViewById(R.id.buttonReselect);
            buttonPlayCard.setVisibility(View.VISIBLE);
            buttonPass.setVisibility(View.VISIBLE);
            buttonReselect.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void promotePlayerToListen() {
        this.runOnUiThread(() -> {
            Button buttonPlayCard = findViewById(R.id.buttonPlayCard);
            Button buttonPass = findViewById(R.id.buttonPass);
            Button buttonReselect = findViewById(R.id.buttonReselect);
            buttonPlayCard.setVisibility(View.GONE);
            buttonPass.setVisibility(View.GONE);
            buttonReselect.setVisibility(View.GONE);
        });
    }

    @Override
    public void showPlayerName(PositionEnum playerIndex, String name) {
        this.runOnUiThread(() -> {
            try {
                getTextViewNameOfPlayerByPos(playerIndex).setText(name);
                TextView textView = getTextViewScoreViewPlayer(playerIndex);
                textView.setText(name);
                textView.setTextColor(0xffffffff);
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        });
    }

    @Override
    public void showListeningPlayer(PositionEnum player) {
        this.runOnUiThread(() -> {
            try {
                getImageViewListenPlayer(player).setVisibility(View.VISIBLE);
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        });
    }

    @Override
    public void showUserScore(int score) {
        this.runOnUiThread(() -> {
            TextView textView = findViewById(R.id.textViewTotalScore);
            textView.setText(String.valueOf(score));
        });
    }

    @Override
    public void showServerMessage(String message) {
        this.runOnUiThread(() -> {
            setGameMessage(message);
            hideServerMessageDelay(10 * 1000);
        });
    }

    @Override
    public void hideServerMessage() {
        this.runOnUiThread(() -> findViewById(R.id.textViewGameMessage).setVisibility(View.GONE));
    }

    @Override
    public void setGameOverPlayerGainScore(PositionEnum player, int gainScore) {
        this.runOnUiThread(() -> {
            try {
                TextView textView = getTextViewScoreViewPlayerScore(player);
                textView.setText(String.valueOf(gainScore));
                textView.setTextColor(0xffffffff);
            } catch (NoSuchFieldException nsfe) {
                nsfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        });
    }

    @Override
    public void showGameOverScore() {
        this.runOnUiThread(() -> {
            setCardClickable(false);
            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutGameFinishScore);
            constraintLayout.setVisibility(View.VISIBLE);
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showIsAllowToPlayCard(boolean isAllow) {
        this.runOnUiThread(() -> {
            Button button = findViewById(R.id.buttonPlayCard);
            try {
                button.setBackground(getDrawableButtonPlayCard(isAllow));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setClickable(isAllow);
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showIsAllowToPass(boolean isAllow) {
        this.runOnUiThread(() -> {
            Button button = findViewById(R.id.buttonPass);
            try {
                button.setBackground(getDrawableButtonPass(isAllow));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setClickable(isAllow);
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showSortMethod(String method) {
        this.runOnUiThread(() -> {
            Button button = findViewById(R.id.buttonSortCard);
            try {
                button.setBackground(getDrawableSortCard(method));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void playSoundEffect() {
        soundPool.play(effectId,
                (float) LocalSetting.getEffectLevel() / 3, (float) LocalSetting.getEffectLevel() / 3,
                0, 0, 1.0f);
    }

    private void toWaitView() {

        mGamePresenter.close();
        mGamePresenter = null;
        System.gc();

        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.game.GameActivity.this, com.softwaretraining.cddgame.wait.WaitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        intent.putExtras(bundle);

        startActivity(intent);
        this.finish();
    }

    private void toSettingView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.game.GameActivity.this, com.softwaretraining.cddgame.setting.SettingActivity.class);
        startActivity(intent);
    }

    private void toRankView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.game.GameActivity.this, com.softwaretraining.cddgame.rank.RankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressWarnings("ConstantConditions")
    private void initPlayer() {
        try {
            this.username = getIntent().getExtras().getString("Player" + String.valueOf(PositionEnum.THIS.ordinal()));
            mGamePresenter.initializePlayer(username,
                    getIntent().getExtras().getString("Player" + String.valueOf(PositionEnum.RIGHT.ordinal())),
                    getIntent().getExtras().getString("Player" + String.valueOf(PositionEnum.UP.ordinal())),
                    getIntent().getExtras().getString("Player" + String.valueOf(PositionEnum.LEFT.ordinal())));
        } catch (NullPointerException e) {
            this.username = "";
            e.printStackTrace();
        }
    }

    private void initCheckBoxListener() {
        try {
            for (int i = 1; i <= 13; ++i) {
                final CheckBox checkBox = getCardCheckBoxByIndex(i);
                final int finalI = i;
                checkBox.setOnClickListener(view -> onClickCard(view, finalI, checkBox));
            }
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void setGameMessage(String message) {
        TextView textView = findViewById(R.id.textViewGameMessage);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    private void hideServerMessageDelay(final long delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                hideServerMessage();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }).start();
    }

    private View findViewByXY(View view, int x, int y) {
        View targetView = null;
        if (view instanceof ViewGroup) {

            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                targetView = findViewByXY(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        return targetView;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        return view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right;
    }

    private View getTouchTarget(View view, int x, int y) {
        View targetView = null;

        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    private int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private float getStretchFactor() {
        int navHeight = getNavigationBarHeight(this);
        int width = this.getResources().getDisplayMetrics().widthPixels;
        return ((float) width / ((float) width - (float) navHeight));
    }

    private void setCardClickable(boolean clickable) {
        try {
            for (int i = 1; i <= 13; ++i) {
                getCardCheckBoxByIndex(i).setClickable(clickable);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private CheckBox getCardCheckBoxByIndex(int index) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("playerCard" + String.valueOf(index));
        return findViewById(field.getInt(instance));
    }

    private Drawable getDrawableCardByCardID(int cardID) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("card" + String.valueOf(cardID));
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private Drawable getDrawableButtonPlayCard(boolean isAllow) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("playcard_" + String.valueOf(isAllow));
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private Drawable getDrawableButtonPass(boolean isAllow) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("pass_" + String.valueOf(isAllow));
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private Drawable getDrawableSortCard(String method) throws NoSuchFieldException, IllegalAccessException {
        R.drawable instance = getRDrawInstance();
        Field field = instance.getClass().getField("btn_sort_" + method);
        return this.getResources().getDrawable(field.getInt(instance));
    }

    private TextView getTextViewNumberOfCardsByPos(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("textViewNumOfCardPlayer" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private TextView getTextViewNameOfPlayerByPos(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("textViewNameOfPlayer" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private ConstraintLayout getConstraintLayoutPlayedCardByPos(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("constraintLayoutPlayedCard" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private ImageView getImageViewPlayedCard(PositionEnum positionEnum, int cardIndex) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("imageViewPlayer" + String.valueOf(positionEnum.ordinal()) + "Card" + String.valueOf(cardIndex));
        return findViewById(field.getInt(instance));
    }

    private ImageView getImageViewPass(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("imageViewPass" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private ImageView getImageViewListenPlayer(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("imageViewListening" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private TextView getTextViewScoreViewPlayer(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("textViewScoreViewPlayer" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private TextView getTextViewScoreViewPlayerScore(PositionEnum positionEnum) throws NoSuchFieldException, IllegalAccessException {
        R.id instance = getRidInstance();
        Field field = instance.getClass().getField("textViewScoreViewPlayerScore" + String.valueOf(positionEnum.ordinal()));
        return findViewById(field.getInt(instance));
    }

    private void initGameView() {
        new GamePresenter(new GameModel(), this);
        promotePlayerToListen();
        initPlayer();
        mGamePresenter.initUserScore();

        initSoundEffect();
        initBackgroundMusic();
    }

    private void initSoundEffect() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        effectId = soundPool.load(this, R.raw.showcard, 1);
    }

    private void initBackgroundMusic() {
        Intent intent = new Intent(GameActivity.this, com.softwaretraining.cddgame.music.BackgroundMusic.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "game");
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(GameActivity.this, com.softwaretraining.cddgame.music.BackgroundMusic.class);
        stopService(intent);
        super.finish();
    }

}
