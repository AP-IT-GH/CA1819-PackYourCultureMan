package com.ap.pacyourcultureman;

public class Player {
    int id;
    String userName, firstName, lastName, email,jwt;
    PlayerStats playerStats;
    int highestScore, totalScore, totalFailed, totalSuccess, totalLost;
    int currentScore = 0;
    public Player(int id,String userName, String firstName, String lastName, String email,PlayerStats playerstats,String jwt) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.playerStats = playerstats;
        this.jwt = jwt;
    }
}
