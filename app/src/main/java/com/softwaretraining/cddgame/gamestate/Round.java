package com.softwaretraining.cddgame.gamestate;

import com.softwaretraining.cddgame.game.GamePresenter;
import com.softwaretraining.cddgame.game.PositionEnum;
import com.softwaretraining.cddgame.player.Rule;

public class Round {

    private final PlayerState rightPlayer;
    private final PlayerState upPlayer;
    private final PlayerState leftPlayer;
    private final PlayerState thisPlayer;

    private PlayerState state;

    private GamePresenter gamePresenter;

    public Round(PositionEnum state) {

        Rule.initializeRule();

        this.rightPlayer = new RightPlayer(this);
        this.upPlayer = new UpPlayer(this);
        this.leftPlayer = new LeftPlayer(this);
        this.thisPlayer = new ThisPlayer(this);

        switch (state) {
            case THIS:
                this.state = thisPlayer;
                break;
            case RIGHT:
                this.state = rightPlayer;
                break;
            case UP:
                this.state = upPlayer;
                break;
            case LEFT:
                this.state = leftPlayer;
                break;
            default:
        }

    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void play() {
        state.play();
    }

    public boolean deleteCard(int number) {
        return state.deleteCard(number);
    }

    public int getNumOfCards() {
        return state.getNumOfCard();
    }

    public int getNumOfCards(PositionEnum positionEnum) {
        return getState(positionEnum).getNumOfCard();
    }

    public void nextPlay() {
        state.next();
        state.play();
    }

    public void listen(PositionEnum positionEnum) {
        gamePresenter.listenPlayer(positionEnum);
    }

    public GamePresenter getGamePresenter() {
        return gamePresenter;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getState() {
        return state;
    }

    public PlayerState getState(PositionEnum positionEnum) {
        switch (positionEnum) {
            case UP:
                return upPlayer;
            case LEFT:
                return leftPlayer;
            case THIS:
                return thisPlayer;
            case RIGHT:
                return rightPlayer;
            default:
                return null;
        }
    }

    public PlayerState getLeftPlayer() {
        return leftPlayer;
    }

    public PlayerState getRightPlayer() {
        return rightPlayer;
    }

    public PlayerState getThisPlayer() {
        return thisPlayer;
    }

    public PlayerState getUpPlayer() {
        return upPlayer;
    }
}

