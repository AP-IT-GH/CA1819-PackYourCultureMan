package com.ap.pacyourcultureman;

public class Player {
    int id;
    String userName, firstName, lastName, email;
    int highestScore, totalScore, totalFailed, totalSuccess, totalLost;
    int currentScore = 0;
    public Player(int id,String userName, String firstName, String lastName, String email, int highestScore, int totalScore, int totalFailed, int totalSuccess, int totalLost) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalFailed = totalFailed;
        this.totalSuccess = totalSuccess;
        this.totalLost = totalLost;
    }
}
