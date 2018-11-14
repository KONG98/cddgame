package com.softwaretraining.cddgame.player;

import java.util.ArrayList;

class Hand {

    private final Card[] cards = new Card[13];
    private final boolean[] cardIsInHand = new boolean[13];
    private final boolean[] cardIsSelect = new boolean[13];

    Hand(Card[] cards) {
        for (int i = 0; i < 13; ++i) {
            this.cards[i] = cards[i];
            cardIsInHand[i] = true;
        }
        sortCards();
    }

    public boolean selectCard(int cardID) {
        int index = getIndexOfCard(cardID);
        if (index == -1) {
            return false;
        } else {
            if (cardIsInHand[index]) {
                cardIsSelect[index] = true;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean selectCard(Card card) {
        return selectCard(card.getCardID());
    }

    public boolean unSelectCard(int cardID) {
        int index = getIndexOfCard(cardID);
        if (index == -1) {
            return false;
        } else {
            if (cardIsInHand[index]) {
                cardIsSelect[index] = false;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean unSelectCard(Card card) {
        return unSelectCard(card.getCardID());
    }

    public boolean reselectCard() {
        for (int i = 0; i < 13; ++i) {
            cardIsSelect[i] = false;
        }
        return true;
    }

    public int[] showSelectCardsID() {
        ArrayList<Card> selectCardsAry = new ArrayList<>();
        for (int i = 0; i < 13; ++i) {
            if (cardIsSelect[i]) {
                selectCardsAry.add(cards[i]);
            }
        }
        int[] result = new int[selectCardsAry.size()];
        for (int i = 0; i < selectCardsAry.size(); ++i) {
            result[i] = selectCardsAry.get(i).getCardID();
        }
        return result;
    }

    public Card[] showSelectCards() {
        ArrayList<Card> selectCardsAry = new ArrayList<>();
        for (int i = 0; i < 13; ++i) {
            if (cardIsSelect[i]) {
                selectCardsAry.add(cards[i]);
            }
        }
        Card[] result = new Card[selectCardsAry.size()];
        result = selectCardsAry.toArray(result);
        return result;
    }

    public void deleteCards(int[] cards) {
        for (int cardID : cards) {
            cardIsInHand[getIndexOfCard(cardID)] = false;
        }
    }

    public void deleteCards(Card[] cards) {
        for (Card card : cards) {
            cardIsInHand[getIndexOfCard(card)] = false;
        }
    }

    public int[] getDetailCardsID() {
        int[] result = new int[getNumOfHandCards()];
        int i = 0;
        for (int countIndex = 0; countIndex < 13; ++countIndex) {
            if (cardIsInHand[countIndex]) {
                result[i++] = cards[countIndex].getCardID();
            }
        }
        return result;
    }

    public Card[] getDetailCards() {
        Card[] result = new Card[getNumOfHandCards()];
        int countIndex = 0;
        for (int i = 0; i < getNumOfHandCards(); ++i) {
            for (; countIndex < 13; ++countIndex) {
                if (cardIsInHand[countIndex]) {
                    result[i] = cards[countIndex];
                    break;
                }
            }
        }
        return result;
    }

    private boolean isInHand(int cardID) {
        int index = getIndexOfCard(cardID);
        if (index == -1) {
            return false;
        } else {
            return cardIsInHand[index];
        }
    }

    public boolean isInHand(Card card) {
        return isInHand(card.getCardID());
    }

    public int getNumOfHandCards() {
        int totalNum = 0;
        for (boolean b : cardIsInHand) {
            if (b) totalNum++;
        }
        return totalNum;
    }

    public int getNumOfSelectedCards() {
        int num = 0;
        for (boolean b : cardIsSelect) {
            if (b) num++;
        }
        return num;
    }

    private int getIndexOfCard(int cardID) {
        for (int index = 0; index < 13; ++index) {
            if (cards[index].getCardID() == cardID) {
                return index;
            }
        }
        return -1;
    }

    private int getIndexOfCard(Card card) {
        return getIndexOfCard(card.getCardID());
    }

    private void sortCards() {
        for (int i = 0; i < cards.length; i++) {
            int lowIndex = i;
            for (int j = cards.length - 1; j > i; j--) {
                if (cards[j].compareTo(cards[lowIndex]) > 0) {
                    lowIndex = j;
                }
            }
            int tempCardID = cards[i].getCardID();
            cards[i] = Card.getCard(cards[lowIndex].getCardID());
            cards[lowIndex] = Card.getCard(tempCardID);
        }
    }
}
