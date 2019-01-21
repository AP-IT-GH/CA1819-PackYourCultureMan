package com.ap.pacyourcultureman;

public class Player {
    private int id,skinId;
    private String userName, firstName, lastName, email,jwt;
    private PlayerStats playerStats;
    private PlayerGameStats playerGameStats;
    private boolean admin;
    public Player(int id,String userName, String firstName, String lastName, String email,PlayerStats playerstats,PlayerGameStats playerGameStats,String jwt,int skinId, boolean admin) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.playerStats = playerstats;
        this.playerGameStats = playerGameStats;
        this.jwt = jwt;
        this.skinId = skinId;
        this.admin = admin;
    }
    public String getJwt(){
        return jwt;
    }
    public PlayerGameStats getPlayerGameStats() {return playerGameStats;}
    public PlayerStats getPlayerStats() {return playerStats;}

    public int getId() {
        return id;
    }

    public int getSkinId() {
        return skinId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPlayerGameStats(PlayerGameStats playerGameStats) {
        this.playerGameStats = playerGameStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public void setSkinId(int skinId) {
        this.skinId = skinId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return admin;
    }
}
