package com.softwaretraining.cddgame.gamestate;

import com.softwaretraining.cddgame.game.PositionEnum;

public class RightPlayer implements PlayerState {

    private final Round round;
    private int numberOfCards = 13;

    RightPlayer(Round round) {
        this.round = round;
    }

    @Override
    public void play() {
        round.getGamePresenter().hideLastTurnPlayed(PositionEnum.RIGHT);
        round.listen(PositionEnum.RIGHT);
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
        round.setState(round.getUpPlayer());
    }
}
