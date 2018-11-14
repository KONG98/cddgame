package com.softwaretraining.cddgame.player;

public class Rule {

    private static Card[] lastCard = null;
    private static CardPattern.cardPattern lastCardPattern = null;
    private static int turns = 1;

    public static boolean isLegal(Card[] card) {
        return matchCardPattern(card) && matchLastPattern(card);
    }

    public static boolean isAllowPass() {
        return !(lastCard == null || lastCardPattern == null || turns == 4);
    }

    public static boolean matchCardPattern(Card[] card) {
        return CardPattern.match(card) != CardPattern.cardPattern.Invalid;
    }

    private static boolean matchLastPattern(Card[] card) {
        CardPattern.cardPattern pattern = CardPattern.match(card);
        if (lastCardPattern == null || turns == 4 || lastCardPattern == CardPattern.cardPattern.Pass) {
            return card.length != 0;
        } else if (pattern == CardPattern.cardPattern.Pass) {
            return true;
        } else if (lastCard.length == 5 && card.length == 5 && pattern.ordinal() > lastCardPattern.ordinal()) {
            return true;
        } else if (lastCardPattern != pattern) {
            return false;
        } else if (pattern == CardPattern.cardPattern.Single) {
            return compareFirstCard(card);
        } else if (pattern == CardPattern.cardPattern.Pair) {
            if (card[0].getFace().ordinal() > lastCard[0].getFace().ordinal()) {
                return true;
            } else if (card[0].getFace().ordinal() == lastCard[0].getFace().ordinal()) {
                if (card[0].getSuit().ordinal() > lastCard[0].getSuit().ordinal() && card[0].getSuit().ordinal() > lastCard[1].getSuit().ordinal()
                        || card[1].getSuit().ordinal() > lastCard[0].getSuit().ordinal() && card[1].getSuit().ordinal() > lastCard[1].getSuit().ordinal()) {
                    return true;
                } else return false;
            } else return false;
        } else if (pattern == CardPattern.cardPattern.Triple) {
            return compareFirstCard(card);
        } else if (pattern == CardPattern.cardPattern.Straight) {
            return compareFirstCard(card);
        } else if (pattern == CardPattern.cardPattern.Flush) {
            return compareFirstCard(card);
        } else if (pattern == CardPattern.cardPattern.FlushStraight) {
            return compareFirstCard(card);
        } else if (pattern == CardPattern.cardPattern.TripleWithPair || pattern == CardPattern.cardPattern.FourWithOneCard) {
            Card thisBiggestCard = getBiggestCard(card);
            Card lastBiggestCard = getBiggestCard(lastCard);
            if (thisBiggestCard.getFace().ordinal() > lastBiggestCard.getFace().ordinal()) {
                return true;
            } else if (thisBiggestCard.getFace().ordinal() == lastBiggestCard.getFace().ordinal()) {
                if (thisBiggestCard.getSuit().ordinal() > lastBiggestCard.getSuit().ordinal()) {
                    return true;
                } else return false;
            } else return false;
        } else if (pattern == CardPattern.cardPattern.Invalid) {
            return false;
        } else {

            System.out.println("Error occur : not match pattern error");
            return false;
        }
    }

    private static boolean compareFirstCard(Card[] card) {
        if (card[0].getFace().ordinal() > lastCard[0].getFace().ordinal()) {
            return true;
        } else if (card[0].getFace().ordinal() == lastCard[0].getFace().ordinal()) {
            if (card[0].getSuit().ordinal() > lastCard[0].getSuit().ordinal()) {
                return true;
            } else return false;
        } else return false;
    }

    private static Card getBiggestCard(Card[] card) {

        Card.Face[] p = new Card.Face[2];
        int[] num = new int[2];
        p[0] = card[0].getFace();
        num[0] = 1;
        for (int i = 1; i < 5; ++i) {
            if (card[i].getFace() == p[0]) {
                num[0]++;
            } else if (p[1] == null) {
                p[1] = card[i].getFace();
                num[1] = 1;
            } else if (card[i].getFace() == p[1]) {
                num[1]++;
            }
        }
        if (num[0] > num[1]) {
            return card[0];
        } else {
            int i;
            for (i = 1; i < 5; ++i) {
                if (card[i].getFace() == p[1]) break;
            }
            return card[i];
        }
    }

    public static void initializeRule() {
        lastCard = null;
        lastCardPattern = null;
        turns = 1;
    }

    public static void setLastPlayCard(Card[] card) {
        if (CardPattern.match(card) == CardPattern.cardPattern.Pass) {
            if (turns < 4) turns++;
        } else {
            lastCard = card.clone();
            lastCardPattern = CardPattern.match(card);
            turns = 1;
        }
    }

    public static int[] getPlayerGainScore(int[] restCards) {

        if (restCards.length < 4) {
            System.out.println("Index out of range.");
            return null;
        }

        int[] cardScore = new int[4];
        for (int i = 0; i < 4; ++i) {
            if (restCards[i] == 13) {
                cardScore[i] = 4 * restCards[i];
            } else if (restCards[i] >= 10 && restCards[i] < 13) {
                cardScore[i] = 3 * restCards[i];
            } else if (restCards[i] >= 8 && restCards[i] < 10) {
                cardScore[i] = 2 * restCards[i];
            } else {
                cardScore[i] = restCards[i];
            }
        }

        int[] score = new int[4];
        score[0] = cardScore[1] + cardScore[2] + cardScore[3] - 3 * cardScore[0];
        score[1] = cardScore[0] + cardScore[2] + cardScore[3] - 3 * cardScore[1];
        score[2] = cardScore[1] + cardScore[0] + cardScore[3] - 3 * cardScore[2];
        score[3] = cardScore[1] + cardScore[2] + cardScore[0] - 3 * cardScore[3];

        return score;
    }

}
