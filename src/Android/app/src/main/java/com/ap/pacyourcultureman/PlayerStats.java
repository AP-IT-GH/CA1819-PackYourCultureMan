package com.ap.pacyourcultureman;

public class PlayerStats {
    int highestScore, totalScore, totalFailed, totalSuccess, totalLost;
    int currentScore = 0;
    public PlayerStats(int highestScore, int totalScore, int totalFailed, int totalSuccess, int totalLost) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalFailed = totalFailed;
        this.totalSuccess = totalSuccess;
        this.totalLost = totalLost;
    }
}
