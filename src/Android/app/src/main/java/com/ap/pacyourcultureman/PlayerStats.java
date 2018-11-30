package com.ap.pacyourcultureman;

public class PlayerStats {
    private int highestScore, totalScore, totalFailed, totalSuccess, totalLost;
    private int currentScore = 0;
    public PlayerStats(int highestScore, int totalScore, int totalFailed, int totalSuccess, int totalLost) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalFailed = totalFailed;
        this.totalSuccess = totalSuccess;
        this.totalLost = totalLost;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getCurrentScore() {
        return currentScore;
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

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

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
