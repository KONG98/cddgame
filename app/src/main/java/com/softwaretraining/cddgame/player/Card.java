package com.softwaretraining.cddgame.player;

import java.util.HashMap;

public class Card implements Comparable {

    @SuppressWarnings("unchecked")
    private static final HashMap<Integer, Card> cardMap = new HashMap(52);

    private Suit suit;
    private Face face;

    private Card(int cardID) {
        setSuit(cardID);
        setFace(cardID);
    }

    public static Card getCard(int id) {
        Card card = cardMap.get(id);
        if (card == null) {
            cardMap.put(id, new Card(id));
            return cardMap.get(id);
        } else {
            return card;
        }
    }

    public static Card getCard(Suit suit, Face face) {
        return getCard(card2id(suit, face));
    }

    private static void setCard(int id) {
        Card card = cardMap.get(id);
        if (card == null) {
            card = new Card(id);
            cardMap.put(id, card);
        }
    }

    private static void setCard(Suit suit, Face face) {
        setCard(card2id(suit, face));
    }

    public static Card id2card(int cardID) {
        return getCard(cardID);
    }

    public static int card2id(Card card) {
        return card2id(card.getSuit(), card.getFace());
    }

    private static int card2id(Suit suit, Face face) {
        return suit.ordinal() * 13 + face.ordinal();
    }

    public int getCardID() {
        return card2id(suit, face);
    }

    public Suit getSuit() {
        return suit;
    }

    private void setSuit(int id) {
        suit = Suit.values()[id / 13];
    }

    public Face getFace() {
        return face;
    }

    private void setFace(int id) {
        face = Face.values()[id % 13];
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!(o instanceof Card)) {
            throw new ClassCastException("Object can not cast to Card");
        }
        Card obj = (Card) o;

        if (this.getFace() == obj.getFace()) {
            if (this.getSuit() == obj.getSuit()) {
                return 0;
            } else if (this.getSuit().ordinal() > obj.getSuit().ordinal()) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.getFace().ordinal() > obj.getFace().ordinal()) {
            return 1;
        } else {
            return -1;
        }
    }

    private int compareToByFace(Card o) {
        return compareTo(o);
    }

    private int compareToBySuit(Card o) {
        if (o == null) {
            throw new NullPointerException();
        }

        if (this.getSuit() == o.getSuit()) {
            if (this.getFace() == o.getFace()) {
                return 0;
            } else if (this.getFace().ordinal() > o.getFace().ordinal()) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.getSuit().ordinal() > o.getSuit().ordinal()) {
            return 1;
        } else {
            return -1;
        }
    }

    public int compareTo(Card o, String compareBy) {
        switch (compareBy) {
            case "face":
                return compareToByFace(o);
            case "suit":
                return compareToBySuit(o);
            default:
                return compareTo(o);
        }
    }

    public enum Suit {
        Diamond, Club, Heart, Spade
    }

    public enum Face {
        NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9, NUM10, J, Q, K, A, NUM2
    }
}
