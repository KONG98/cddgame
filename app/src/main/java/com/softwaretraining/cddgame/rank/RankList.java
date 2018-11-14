package com.softwaretraining.cddgame.rank;

class RankList {

    private String seqNumber;
    private String username;
    private String score;

    public RankList(String seqNumber, String username, String score) {
        this.seqNumber = seqNumber;
        this.username = username;
        this.score = score;
    }

    public String getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(String seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
