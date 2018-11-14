package com.softwaretraining.cddgame.player.sorter;

import com.softwaretraining.cddgame.player.Card;
import com.softwaretraining.cddgame.player.CardPattern;
import com.softwaretraining.cddgame.player.Rule;

import java.util.ArrayList;
import java.util.Collections;

public class PatternSorter implements IConcreteSorter {

    private final Sorter sorter;

    PatternSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    @Override
    public Card[] sort(Card[] cards) {
        ArrayList<Card> sortedCardsArray = new ArrayList<>();
        ArrayList<Card> cardsArray = new ArrayList<>();

        Collections.addAll(cardsArray, cards);

        while (cardsArray.size() != 0) {
            ArrayList<Card[]> validCardPattern = getValidCardPattern(cardsArray.toArray(new Card[cardsArray.size()]));
            Card[] priorCards = getPriorCardPattern(validCardPattern);
            for (Card card : priorCards) {
                sortedCardsArray.add(card);
                cardsArray.remove(card);
            }
        }

        Card[] resultSortedCards = new Card[sortedCardsArray.size()];
        resultSortedCards = sortedCardsArray.toArray(resultSortedCards);

        return resultSortedCards;
    }

    @Override
    public int[] sort(int[] cardsArray) {
        Card[] cards = new Card[cardsArray.length];
        for (int i = 0; i < cardsArray.length; ++i) {
            cards[i] = Card.id2card(cardsArray[i]);
        }
        cards = sort(cards);
        int[] resultCards = new int[cardsArray.length];
        for (int i = 0; i < cardsArray.length; ++i) {
            resultCards[i] = Card.card2id(cards[i]);
        }
        return resultCards;
    }

    @Override
    public void nextSorter() {
        sorter.setConcreteSorter(sorter.getFaceSorter());
    }

    @Override
    public String toString() {
        return "pattern";
    }

    private ArrayList<Card[]> getValidCardPattern(Card[] cardsArray) {
        ArrayList<Card[]> resultValidCardPatternSet = new ArrayList<>();
        ArrayList<Card> testCardPattern = new ArrayList<>();

        for (int i1 = cardsArray.length - 1; i1 >= 0; --i1) {
            testCardPattern.add(cardsArray[i1]);
            for (int i2 = i1 - 1; i2 >= 0; --i2) {
                testCardPattern.add(cardsArray[i2]);
                for (int i3 = i2 - 1; i3 >= 0; --i3) {
                    testCardPattern.add(cardsArray[i3]);
                    for (int i4 = i3 - 1; i4 >= 0; --i4) {
                        testCardPattern.add(cardsArray[i4]);
                        for (int i5 = i4 - 1; i5 >= 0; --i5) {
                            testCardPattern.add(cardsArray[i5]);
                            if (Rule.matchCardPattern(testCardPattern.toArray(new Card[testCardPattern.size()])))
                                resultValidCardPatternSet.add(testCardPattern.toArray(new Card[testCardPattern.size()]));
                            testCardPattern.remove(cardsArray[i5]);
                        }
                        if (Rule.matchCardPattern(testCardPattern.toArray(new Card[testCardPattern.size()])))
                            resultValidCardPatternSet.add(testCardPattern.toArray(new Card[testCardPattern.size()]));
                        testCardPattern.remove(cardsArray[i4]);
                    }
                    if (Rule.matchCardPattern(testCardPattern.toArray(new Card[testCardPattern.size()])))
                        resultValidCardPatternSet.add(testCardPattern.toArray(new Card[testCardPattern.size()]));
                    testCardPattern.remove(cardsArray[i3]);
                }
                if (Rule.matchCardPattern(testCardPattern.toArray(new Card[testCardPattern.size()])))
                    resultValidCardPatternSet.add(testCardPattern.toArray(new Card[testCardPattern.size()]));
                testCardPattern.remove(cardsArray[i2]);
            }
            if (Rule.matchCardPattern(testCardPattern.toArray(new Card[testCardPattern.size()])))
                resultValidCardPatternSet.add(testCardPattern.toArray(new Card[testCardPattern.size()]));
            testCardPattern.remove(cardsArray[i1]);
        }

        return resultValidCardPatternSet;
    }

    private Card[] getPriorCardPattern(ArrayList<Card[]> validCardPatternArray) {
        Card[] priorCard = validCardPatternArray.get(0);
        int priority = 0;

        for (Card[] cards : validCardPatternArray) {
            int prior = getCardsPriority(cards);
            if (prior > priority) {
                priorCard = cards;
                priority = prior;
            }
        }

        return priorCard;
    }

    private int getCardsPriority(Card[] cards) {
        int priority = 0;

        CardPattern.cardPattern pattern = CardPattern.match(cards);
        Card biggestCard;

        priority += pattern.getPriority();
        biggestCard = getBiggestCard(cards);

        priority += biggestCard.getFace().ordinal();

        return priority;
    }

    private Card getBiggestCard(Card[] cards) {
        Card biggestCard = cards[0];
        for (Card card : cards) {
            if (card.compareTo(biggestCard) > 0) {
                biggestCard = card;
            }
        }
        return biggestCard;
    }

}
