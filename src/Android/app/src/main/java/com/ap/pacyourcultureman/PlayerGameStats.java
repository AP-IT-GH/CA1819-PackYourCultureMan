package com.ap.pacyourcultureman;

public class PlayerGameStats {
    private int lifePoints,rifle,freezeGun,pushBackGun;
    public PlayerGameStats(int lifePoints,int rifle,int freezeGun,int pushBackGun){
        this.lifePoints = lifePoints;
        this.freezeGun = freezeGun;
        this.rifle = rifle;
        this.pushBackGun = pushBackGun;
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
}
