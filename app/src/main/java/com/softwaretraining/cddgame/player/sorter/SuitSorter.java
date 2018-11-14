package com.softwaretraining.cddgame.player.sorter;

import com.softwaretraining.cddgame.player.Card;

import static com.softwaretraining.cddgame.player.Card.id2card;

public class SuitSorter implements IConcreteSorter {

    private final Sorter sorter;

    SuitSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    @Override
    public Card[] sort(Card[] cardsArray) {
        Card[] cards = cardsArray.clone();
        for (int i = 0; i < cardsArray.length; i++) {
            int lowIndex = i;
            for (int j = cardsArray.length - 1; j > i; j--) {
                if (cards[j].compareTo(cards[lowIndex], toString()) > 0) {
                    lowIndex = j;
                }
            }
            int tempCardID = cards[i].getCardID();
            cards[i] = Card.getCard(cards[lowIndex].getCardID());
            cards[lowIndex] = Card.getCard(tempCardID);
        }
        return cards;
    }

    @Override
    public int[] sort(int[] cardsArray) {
        int[] cards = cardsArray.clone();
        for (int i = 0; i < cardsArray.length; i++) {
            int lowIndex = i;
            for (int j = cardsArray.length - 1; j > i; j--) {
                if (id2card(cards[j]).compareTo(id2card(cards[lowIndex]), toString()) > 0) {
                    lowIndex = j;
                }
            }
            int tempCardID = cards[i];
            cards[i] = cards[lowIndex];
            cards[lowIndex] = tempCardID;
        }
        return cards;
    }

    @Override
    public void nextSorter() {
        sorter.setConcreteSorter(sorter.getPatternSorter());
    }

    @Override
    public String toString() {
        return "suit";
    }

}
