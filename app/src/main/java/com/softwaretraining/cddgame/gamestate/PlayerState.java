package com.softwaretraining.cddgame.gamestate;

public interface PlayerState {

    void play();

    boolean deleteCard(int number);

    int getNumOfCard();

    void next();

}
