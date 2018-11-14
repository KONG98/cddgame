package com.softwaretraining.cddgame.game;

import com.softwaretraining.cddgame.BasePresenter;
import com.softwaretraining.cddgame.BaseView;

interface GameContract {

    interface View extends BaseView<Presenter> {

        void startGame();

        void showDetailCards(int[] cards);

        void promotePlayerPlayCard();

        void promotePlayerToListen();

        void showNumberOfCards(PositionEnum player, int number);

        void hideLastTurnPlayed(PositionEnum player);

        void showPlayerPlayCards(PositionEnum player, int[] playCards);

        void showPlayerName(PositionEnum player, String name);

        void showListeningPlayer(PositionEnum player);

        void showUserScore(int score);

        void showServerMessage(String message);

        void hideServerMessage();

        void setGameOverPlayerGainScore(PositionEnum player, int gainScore);

        void showGameOverScore();

        void showIsAllowToPlayCard(boolean isAllow);

        void showIsAllowToPass(boolean isAllow);

        void showSortMethod(String method);

    }

    interface Presenter extends BasePresenter {

        void initializePlayer(String username, String player1, String player2, String player3);

        void exit();

        void backToWait();

        void play();

        void pass();

        void selectCard(int cardID);

        void unSelectCard(int cardID);

        void playCard();

        void reselectCard();

        void sortCard();

        void initOtherPlayer();

        void initDetailCards();

        void initUserScore();

        void close();

    }

}
