package com.softwaretraining.cddgame.player;


public class Player implements IPlayer {

    private Hand hand;

    @Override
    public void getDistributedCards(Card[] cards) {
        if (hand == null) {
            hand = new Hand(cards);
        }
    }

    @Override
    public void getDistributedCards(int[] cards) {
        Card[] cardsAry = new Card[cards.length];
        for (int i = 0; i < cards.length; ++i) {
            cardsAry[i] = Card.id2card(cards[i]);
        }
        getDistributedCards(cardsAry);
    }

    @Override
    public int[] getDetailCards() {
        return hand.getDetailCardsID();
    }

    @Override
    public int getNumOfHandCards() {
        return hand.getNumOfHandCards();
    }

    @Override
    public int getNumOfSelectedCards() {
        return hand.getNumOfSelectedCards();
    }

    @Override
    public void selectCard(int cardID) {
        hand.selectCard(cardID);
    }

    @Override
    public void selectCard(Card card) {
        hand.selectCard(card);
    }

    @Override
    public void unSelectCard(int cardID) {
        hand.unSelectCard(cardID);
    }

    @Override
    public void unSelectCard(Card card) {
        hand.unSelectCard(card);
    }

    @Override
    public void reselectCard() {
        hand.reselectCard();
    }

    @Override
    public int[] playCard() {
        return hand.showSelectCardsID();
    }

    @Override
    public void deleteCard(int[] cards) {
        hand.deleteCards(cards);
    }

    @Override
    public void pass() {

    }

    @Override
    public int punish() {
        return -200;
    }

    @Override
    public void confirmCardPlayed(int[] playCards) {
        Rule.setLastPlayCard(cardIdAryToCardAry(playCards));
    }

    @Override
    public boolean isLegal() {
        return Rule.isLegal(cardIdAryToCardAry(playCard())) && playCard().length != 0;
    }

    @Override
    public boolean isAllowPass() {
        return Rule.isAllowPass();
    }

    private static Card[] cardIdAryToCardAry(int[] cardID) {
        Card[] cards = new Card[cardID.length];
        for (int i = 0; i < cardID.length; ++i) {
            cards[i] = Card.id2card(cardID[i]);
        }
        return cards;
    }
}
