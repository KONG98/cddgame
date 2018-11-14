package com.softwaretraining.cddgame.game;

import android.os.Looper;

import com.softwaretraining.cddgame.gamestate.Round;
import com.softwaretraining.cddgame.player.IPlayer;
import com.softwaretraining.cddgame.player.Player;
import com.softwaretraining.cddgame.player.Rule;
import com.softwaretraining.cddgame.player.sorter.Sorter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GamePresenter implements GameContract.Presenter, GameListener {

    private boolean isClose = false;
    private boolean isExit = false;

    private Reference<GameContract.View> viewReference;
    private GameModel mGameModel;

    private Round round;

    private final HashMap<String, PositionEnum> name2pos;

    private final HashMap<PositionEnum, String> pos2name;

    private IPlayer player;

    GamePresenter(GameModel gameModel, GameContract.View view) {
        mGameModel = gameModel;
        viewReference = new WeakReference<>(view);
        name2pos = new HashMap<>(6);
        pos2name = new HashMap<>(6);

        mGameModel.setGameListener(this);
        view.setPresenter(this);
    }

    @Override
    public void start() {
        initDetailCards();
        requestFirstPlayer();
    }

    @Override
    public void initializePlayer(String username, String player1, String player2, String player3) {

        name2pos.put(username, PositionEnum.THIS);
        name2pos.put(player1, PositionEnum.RIGHT);
        name2pos.put(player2, PositionEnum.UP);
        name2pos.put(player3, PositionEnum.LEFT);

        pos2name.put(PositionEnum.THIS, username);
        pos2name.put(PositionEnum.RIGHT, player1);
        pos2name.put(PositionEnum.UP, player2);
        pos2name.put(PositionEnum.LEFT, player3);

        viewReference.get().showPlayerName(PositionEnum.THIS, username);
        viewReference.get().showPlayerName(PositionEnum.RIGHT, player1);
        viewReference.get().showPlayerName(PositionEnum.UP, player2);
        viewReference.get().showPlayerName(PositionEnum.LEFT, player3);

        player = new Player();
        viewReference.get().showNumberOfCards(PositionEnum.RIGHT, 13);
        viewReference.get().showNumberOfCards(PositionEnum.UP, 13);
        viewReference.get().showNumberOfCards(PositionEnum.LEFT, 13);

        viewReference.get().startGame();
    }

    @Override
    public void exit() {
        if (isClose) {
            return;
        }
        try {
            if (!isExit) {
                new Thread(() -> mGameModel.sendUserGainScore(pos2name.get(PositionEnum.THIS), player.punish())).start();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
        backToWait();
    }

    @Override
    public void backToWait() {
        if (isClose) {
            return;
        }
        try {
            mGameModel.sendUserBackToWait(pos2name.get(PositionEnum.THIS));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        if (isClose) {
            return;
        }
        try {
            round.play();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pass() {
        if (isClose) {
            return;
        }
        String str = pos2name.get(PositionEnum.THIS) + "$" +
                0 + "$";
        new Thread(() -> mGameModel.sendPlayCardMessage(str)).start();
    }

    @Override
    public void selectCard(int cardID) {
        try {
            player.selectCard(cardID);
            if (player.isLegal()) {
                viewReference.get().showIsAllowToPlayCard(true);
            } else {
                viewReference.get().showIsAllowToPlayCard(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            try {
                viewReference.get().showServerMessage("java.lang.NullPointerException");
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    @Override
    public void unSelectCard(int cardID) {
        try {
            player.unSelectCard(cardID);
            if (player.isLegal()) {
                viewReference.get().showIsAllowToPlayCard(true);
            } else {
                viewReference.get().showIsAllowToPlayCard(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            try {
                viewReference.get().showServerMessage("java.lang.NullPointerException");
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    @Override
    public void playCard() {
        if (isClose) {
            return;
        }
        int[] cards = player.playCard();

        StringBuilder sb = new StringBuilder();
        sb.append(pos2name.get(PositionEnum.THIS)).append("$");
        sb.append(String.valueOf(cards.length)).append("$");
        for (int cardID : cards) {
            sb.append(String.valueOf(cardID)).append("$");
        }

        new Thread(() -> mGameModel.sendPlayCardMessage(sb.toString())).start();
    }

    @Override
    public void reselectCard() {
        if (isClose) {
            return;
        }
        try {
            player.reselectCard();
            viewReference.get().showIsAllowToPlayCard(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sortCard() {
        if (isClose) {
            return;
        }
        new Thread(() -> {
            try {
                reselectCard();
                Sorter.getInstance().changeSorter();
                viewReference.get().showDetailCards(Sorter.getInstance().sort(player.getDetailCards()));
                viewReference.get().showSortMethod(Sorter.getInstance().getCurrentSorter());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void requestFirstPlayer() {
        if (isClose) {
            return;
        }
        new Thread(() -> mGameModel.sendFirstPlayerRequest(pos2name.get(PositionEnum.THIS))).start();
    }

    public void hideLastTurnPlayed(PositionEnum positionEnum) {
        if (isClose) {
            return;
        }
        viewReference.get().hideLastTurnPlayed(positionEnum);
    }

    public void listenPlayer(PositionEnum positionEnum) {
        if (isClose) {
            return;
        }
        viewReference.get().showListeningPlayer(positionEnum);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(() -> mGameModel.sendOtherPlayCardRequest(pos2name.get(positionEnum))).start();
        } else {
            mGameModel.sendOtherPlayCardRequest(pos2name.get(positionEnum));
        }
    }

    @Override
    public void initOtherPlayer() {
        if (isClose) {
            return;
        }
        new Thread(() -> mGameModel.sendOtherPlayerInfoRequest(pos2name.get(PositionEnum.THIS))).start();
    }

    @Override
    public void initDetailCards() {
        if (isClose) {
            return;
        }
        new Thread(() -> mGameModel.sendPlayerHandRequest(pos2name.get(PositionEnum.THIS))).start();
    }

    @Override
    public void initUserScore() {
        if (isClose) {
            return;
        }
        new Thread(() -> mGameModel.sendUserScoreRequest(pos2name.get(PositionEnum.THIS))).start();
    }

    @Override
    public void responseMessage(String message) {
        if (isClose) {
            return;
        }
        String[] strAry = message.split("\\$");

        switch (strAry[0]) {
            case "otherplay": {
                otherPlayerPlayCardHandler(strAry);
                break;
            }
            case "play": {
                playerPlayCardHandler(strAry);
                break;
            }
            case "other": {
                otherPlayerInfoHandler(strAry);
                break;
            }
            case "card": {
                playerGetCardsHandler(strAry);
                break;
            }
            case "first": {
                firstPlayerHandler(strAry);
                break;
            }
            case "score": {
                playerScoreHandler(strAry);
                break;
            }
            case "back": {
                backToWaitHandler(strAry);
                break;
            }
            default:
                viewReference.get().showServerMessage(message);
                break;
        }
    }

    public void close() {
        isClose = true;
        mGameModel.close();
        mGameModel = null;
        viewReference.clear();
        viewReference = null;
        round = null;
        player = null;
    }

    public void promotePlayerToPlay() {
        if (isClose) {
            return;
        }
        viewReference.get().promotePlayerPlayCard();
        viewReference.get().showIsAllowToPass(player.isAllowPass());
        if (player.isLegal()) {
            viewReference.get().showIsAllowToPlayCard(true);
        } else {
            viewReference.get().showIsAllowToPlayCard(false);
        }
    }

    private void initRound(String firstPlayer) {
        if (name2pos.containsKey(firstPlayer)) {
            round = new Round(name2pos.get(firstPlayer));
            round.setGamePresenter(this);
        } else {
            System.out.println("Initialize round error: Server reply first player: " + firstPlayer);
            viewReference.get().showServerMessage("游戏初始化出错，请重新开始游戏");
        }
    }

    private void otherPlayerInfoHandler(String[] strAry) {
        if (isClose) {
            return;
        }
        try {
            viewReference.get().showNumberOfCards(name2pos.get(strAry[1]), Integer.parseInt(strAry[2]));
            viewReference.get().showNumberOfCards(name2pos.get(strAry[3]), Integer.parseInt(strAry[4]));
            viewReference.get().showNumberOfCards(name2pos.get(strAry[5]), Integer.parseInt(strAry[6]));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void playerGetCardsHandler(String[] strAry) {
        if (isClose) {
            return;
        }
        final int baseIndex = 1;
        final int numOfCards = 13;
        int[] cardIdAry = new int[numOfCards];
        try {
            for (int i = baseIndex; i < numOfCards + baseIndex; ++i) {
                cardIdAry[i - baseIndex] = Integer.parseInt(strAry[i]);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        player.getDistributedCards(cardIdAry);
        viewReference.get().showSortMethod(Sorter.getInstance().getCurrentSorter());
        viewReference.get().showDetailCards(Sorter.getInstance().sort(player.getDetailCards()));
    }

    private void playerPlayCardHandler(String[] strAry) {

        try {
            if (!strAry[1].equals(pos2name.get(PositionEnum.THIS))) {
                System.out.println("Player name not match error. "
                        + "Require name :" + pos2name.get(PositionEnum.THIS)
                        + " | Server reply name: " + strAry[1]);
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }

        final int baseIndex = 3;
        int numOfCards;

        try {
            numOfCards = Integer.parseInt(strAry[2]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        int[] cards = new int[numOfCards];

        try {
            for (int i = baseIndex; i < numOfCards + baseIndex; ++i) {
                cards[i - baseIndex] = Integer.parseInt(strAry[i]);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }


        player.deleteCard(cards);
        player.confirmCardPlayed(cards);
        player.reselectCard();
        viewReference.get().showDetailCards(Sorter.getInstance().sort(player.getDetailCards()));
        viewReference.get().showPlayerPlayCards(name2pos.get(strAry[1]), cards);
        viewReference.get().promotePlayerToListen();
        if (round.deleteCard(cards.length)) {
            gameOverHandler();
        } else {
            round.nextPlay();
        }
    }

    private void otherPlayerPlayCardHandler(String[] strAry) {
        if (isClose) {
            return;
        }
        try {
            if (strAry[1].equals(pos2name.get(PositionEnum.THIS))) {
                System.out.println("Player name not match error: "
                        + " Server reply name: " + strAry[1]);
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }
        if (strAry[1] == null || "null".equals(strAry[1])) {
            if (isClose) {
            } else {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            round.play();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    try {
                        Thread.sleep(2000);
                        round.play();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            final int baseIndex = 3;
            int numOfCards;
            try {
                numOfCards = Integer.parseInt(strAry[2]);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            int[] cards = new int[numOfCards];
            try {
                for (int i = baseIndex; i < numOfCards + baseIndex; ++i) {
                    cards[i - baseIndex] = Integer.parseInt(strAry[i]);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }

            player.confirmCardPlayed(cards);
            viewReference.get().showPlayerPlayCards(name2pos.get(strAry[1]), cards);
            if (round.deleteCard(cards.length)) {
                gameOverHandler();
            } else {
                viewReference.get().showNumberOfCards(name2pos.get(strAry[1]), round.getNumOfCards());
                round.nextPlay();
            }
        }

    }

    private void firstPlayerHandler(String[] strAry) {
        try {
            if ("null".equals(strAry[1])) {
                requestFirstPlayer();
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }
        viewReference.get().hideServerMessage();
        if (round != null) {
            return;
        }
        initRound(strAry[1]);
        play();
    }

    private void playerScoreHandler(String[] strAry) {
        int score;
        try {
            score = Integer.parseInt(strAry[1]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            score = 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            score = 0;
        }
        viewReference.get().showUserScore(score);
    }

    @SuppressWarnings("ConstantConditions")
    private void gameOverHandler() {
        if (isClose) {
            return;
        }
        int[] restCards = new int[4];
        for (int i = 0; i < 4; ++i) {
            restCards[i] = round.getState(PositionEnum.values()[i]).getNumOfCard();
        }
        int[] score = Rule.getPlayerGainScore(restCards);
        try {
            mGameModel.sendUserGainScore(pos2name.get(PositionEnum.THIS), score[0]);
            for (int i = 0; i < 4; ++i) {
                viewReference.get().setGameOverPlayerGainScore(PositionEnum.values()[i], score[i]);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        viewReference.get().showGameOverScore();
    }

    private void backToWaitHandler(String[] strAry) {
        if (isClose) {
            return;
        }
        try {
            if (Integer.parseInt(strAry[1]) == -1) {
                viewReference.get().showServerMessage("有玩家退出了房间，请返回等待界面重新开始游戏");
                isExit = true;
            } else if (Integer.parseInt(strAry[1]) == 1) {
                viewReference.get().showServerMessage("房间人数不足，请返回等待界面重新开始游戏");
            } else if (Integer.parseInt(strAry[1]) == 0) {

            } else {

            }
        } catch (NumberFormatException e) {
            viewReference.get().showServerMessage("java.lang.NumberFormatException");
        } catch (NullPointerException e) {
            viewReference.get().showServerMessage("java.lang.NullPointerException");
        }
    }

}
