package com.softwaretraining.cddgame.player.sorter;

import com.softwaretraining.cddgame.player.Card;

public class Sorter {

    private static Sorter INSTANCE = null;

    private final IConcreteSorter faceSorter;
    private final IConcreteSorter suitSorter;
    private final IConcreteSorter patternSorter;

    private IConcreteSorter concreteSorter;

    private Sorter() {
        faceSorter = new FaceSorter(this);
        suitSorter = new SuitSorter(this);
        patternSorter = new PatternSorter(this);

        concreteSorter = faceSorter;
    }

    public static Sorter getInstance() {
        if (INSTANCE == null) INSTANCE = new Sorter();
        return INSTANCE;
    }

    public void initSorter() {
        setConcreteSorter(this.getFaceSorter());
    }

    public void changeSorter() {
        concreteSorter.nextSorter();
    }

    public String getCurrentSorter() {
        return concreteSorter.toString();
    }

    public int[] sort(int[] cardsArray) {
        return concreteSorter.sort(cardsArray);
    }

    public Card[] sort(Card[] cardsArray) {
        return concreteSorter.sort(cardsArray);
    }

    void setConcreteSorter(IConcreteSorter concreteSorter) {
        this.concreteSorter = concreteSorter;
    }

    IConcreteSorter getFaceSorter() {
        return faceSorter;
    }

    IConcreteSorter getSuitSorter() {
        return suitSorter;
    }

    IConcreteSorter getPatternSorter() {
        return patternSorter;
    }

}
