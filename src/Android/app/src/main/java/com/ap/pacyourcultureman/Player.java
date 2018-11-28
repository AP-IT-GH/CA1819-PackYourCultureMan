package com.ap.pacyourcultureman;

public class Player {
    int id,skinId;
    String userName, firstName, lastName, email,jwt;
    PlayerStats playerStats;
    PlayerGameStats playerGameStats;
    int highestScore, totalScore, totalFailed, totalSuccess, totalLost;
    int currentScore = 0;
    public Player(int id,String userName, String firstName, String lastName, String email,PlayerStats playerstats,PlayerGameStats playerGameStats,String jwt,int skinId) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.playerStats = playerstats;
        this.playerGameStats = playerGameStats;
        this.jwt = jwt;
        this.skinId = skinId;

    }
    public String getJwt(){
        return jwt;
    }
}
