package com.softwaretraining.cddgame.gamestate;

import com.softwaretraining.cddgame.game.PositionEnum;

public class ThisPlayer implements PlayerState {

    private final Round round;
    private int numberOfCards = 13;

    ThisPlayer(Round round) {
        this.round = round;
    }

    @Override
    public void play() {
        round.getGamePresenter().hideLastTurnPlayed(PositionEnum.THIS);
        round.getGamePresenter().promotePlayerToPlay();
    }

    @Override
    public boolean deleteCard(int number) {
        numberOfCards -= number;
        return numberOfCards == 0;
    }

    @Override
    public int getNumOfCard() {
        return numberOfCards;
    }

    @Override
    public void next() {
        round.setState(round.getRightPlayer());
    }
}
