package com.softwaretraining.cddgame.player;

import java.util.HashMap;

public class CardPattern {

    private static final HashMap<Card[], cardPattern> cardPatternHashMap = new HashMap<>();

    public static cardPattern match(Card[] cards) {
        Card[] card = sortByFace(cards);
        cardPattern pattern = cardPatternHashMap.get(card);
        if (pattern == null) {
            cardPatternHashMap.put(card, analyzePattern(card));
            return cardPatternHashMap.get(card);
        } else {
            return pattern;
        }
    }

    private static cardPattern analyzePattern(Card[] cards) {
        if (isFlushStraight(cards)) return cardPattern.FlushStraight;
        else if (isFourWithOneCard(cards)) return cardPattern.FourWithOneCard;
        else if (isTripleWithPair(cards)) return cardPattern.TripleWithPair;
        else if (isFlush(cards)) return cardPattern.Flush;
        else if (isStraight(cards)) return cardPattern.Straight;
        else if (isPass(cards)) return cardPattern.Pass;
        else if (isSingle(cards)) return cardPattern.Single;
        else if (isPair(cards)) return cardPattern.Pair;
        else if (isTriple(cards)) return cardPattern.Triple;
        else return cardPattern.Invalid;
    }

    private static boolean isPass(Card[] card) {
        return card.length == 0;
    }

    private static boolean isSingle(Card[] card) {
        return card.length == 1;
    }

    private static boolean isPair(Card[] card) {
        return card.length == 2 && card[0].getFace() == card[1].getFace();
    }

    private static boolean isTriple(Card[] card) {
        return card.length == 3 && card[0].getFace() == card[1].getFace() && card[0].getFace() == card[2].getFace();
    }

    private static boolean isStraight(Card[] card) {
        if (card.length == 5) {
            int base = card[0].getFace().ordinal();
            for (int i = 1; i < 5; ++i) {
                if (card[i].getFace().ordinal() != base - i) return false;
            }
            return true;
        } else return false;
    }

    private static boolean isFlush(Card[] card) {
        if (card.length == 5) {
            Card.Suit s = card[0].getSuit();
            for (int i = 1; i < 5; ++i) {
                if (card[i].getSuit() != s) return false;
            }
            return true;
        } else return false;
    }

    private static boolean isTripleWithPair(Card[] card) {
        if (card.length == 5) {
            Card.Face[] p = new Card.Face[2];
            int[] num = new int[2];
            p[0] = card[0].getFace();
            num[0] = 1;
            if (classifyCard(card, p, num)) return false;
            return (num[0] == 2 && num[1] == 3) || (num[0] == 3 && num[1] == 2);
        } else return false;
    }

    private static boolean isFourWithOneCard(Card[] card) {
        if (card.length == 5) {
            Card.Face[] p = new Card.Face[2];
            int[] num = new int[2];
            p[0] = card[0].getFace();
            num[0] = 1;
            if (classifyCard(card, p, num)) return false;
            return (num[0] == 1 && num[1] == 4) || (num[0] == 4 && num[1] == 1);
        } else return false;

    }

    private static boolean classifyCard(Card[] card, Card.Face[] p, int[] num) {
        for (int i = 1; i < 5; ++i) {
            if (card[i].getFace() == p[0]) {
                num[0]++;
            } else if (p[1] == null) {
                p[1] = card[i].getFace();
                num[1] = 1;
            } else if (card[i].getFace() == p[1]) {
                num[1]++;
            } else return true;
        }
        return false;
    }

    private static boolean isFlushStraight(Card[] card) {
        return isFlush(card) && isStraight(card);
    }

    private static Card[] sortByFace(Card[] cardsArray) {
        Card[] cards = cardsArray.clone();
        for (int i = 0; i < cardsArray.length; i++) {
            int lowIndex = i;
            for (int j = cardsArray.length - 1; j > i; j--) {
                if (cards[j].compareTo(cards[lowIndex]) > 0) {
                    lowIndex = j;
                }
            }
            int tempCardID = cards[i].getCardID();
            cards[i] = Card.getCard(cards[lowIndex].getCardID());
            cards[lowIndex] = Card.getCard(tempCardID);
        }
        return cards;
    }

    public enum cardPattern {
        Invalid(0),
        Pass(0),
        Single(10),
        Pair(30),
        Triple(50),
        Straight(70),
        Flush(90),
        TripleWithPair(110),
        FourWithOneCard(130),
        FlushStraight(150),;

        private final int priority;

        cardPattern(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }

    }

}
