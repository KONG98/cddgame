package com.softwaretraining.cddgame.player;

public interface IPlayer {

    void getDistributedCards(Card[] cards);

    void getDistributedCards(int[] cards);

    int[] getDetailCards();

    int getNumOfHandCards();

    int getNumOfSelectedCards();

    void selectCard(int cardID);

    void selectCard(Card card);

    void unSelectCard(int cardID);

    void unSelectCard(Card card);

    void reselectCard();

    int[] playCard();

    void deleteCard(int[] cards);

    void pass();

    int punish();

    void confirmCardPlayed(int[] playCards);

    boolean isLegal();

    boolean isAllowPass();

}
