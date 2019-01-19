package com.ap.pacyourcultureman;

public class HighscoreProfile {
    private int highestScore;
    private int totalScore;
    private int totalFailed;
    private int totalSuccess;
    private int totalLost;
    private int ranking;
    private String username;
    public HighscoreProfile(int ranking,String username,int highestScore, int totalScore, int totalFailed, int totalSuccess, int totalLost) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalFailed = totalFailed;
        this.totalSuccess = totalSuccess;
        this.totalLost = totalLost;
        this.username = username;
        this.ranking = ranking;
    }
    public int getRanking(){return ranking;}
    public String getUsername(){return username;}

    public int getHighestScore() {
        return highestScore;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public int getTotalLost() {
        return totalLost;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setRanking(int ranking){this.ranking = ranking;}

    public void setUsername(String username){this.username = username;}

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setTotalLost(int totalLost) {
        this.totalLost = totalLost;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }
}
