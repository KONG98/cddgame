package com.softwaretraining.cddgame.player.sorter;

import com.softwaretraining.cddgame.player.Card;

interface IConcreteSorter {

    int[] sort(int[] cardsArray);

    Card[] sort(Card[] cardsArray);

    void nextSorter();

    String toString();

}
