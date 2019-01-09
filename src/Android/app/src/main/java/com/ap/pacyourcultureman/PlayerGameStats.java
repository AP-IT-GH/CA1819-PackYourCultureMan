package com.ap.pacyourcultureman;

public class PlayerGameStats {
    private int lifePoints,rifle,freezeGun,pushBackGun,coins;
    public PlayerGameStats(int lifePoints,int rifle,int freezeGun,int pushBackGun,int coins){
        this.lifePoints = lifePoints;
        this.freezeGun = freezeGun;
        this.rifle = rifle;
        this.pushBackGun = pushBackGun;
        this.coins = coins;
    }
    public int getLifePoints(){
        return lifePoints;
    }
    public int getRifle(){
        return rifle;
    }
    public int getFreezeGun(){
        return freezeGun;
    }
    public int getPushBackGun(){
        return pushBackGun;
    }
    public int getCoins(){
        return coins;
    }

    public void setFreezeGun(int freezeGun) {
        this.freezeGun = freezeGun;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void setPushBackGun(int pushBackGun) {
        this.pushBackGun = pushBackGun;
    }

    public void setRifle(int rifle) {
        this.rifle = rifle;
    }
    public void setCoins(int coins){
        this.coins = coins;
    }
}
