package com.ap.pacyourcultureman;

public class HighscoreProfile {
    private int highestScore;
    private int totalScore;
    private int totalFailed;
    private int totalSuccess;
    private int totalLost;
    private String username;
    public HighscoreProfile(String username,int highestScore, int totalScore, int totalFailed, int totalSuccess, int totalLost) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalFailed = totalFailed;
        this.totalSuccess = totalSuccess;
        this.totalLost = totalLost;
        this.username = username;
    }
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
