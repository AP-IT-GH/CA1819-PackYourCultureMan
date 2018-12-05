package com.ap.pacyourcultureman;
public class Street {

    private int id;
    private Double latA,lonA,latB,lonB;

    //constructor
    public Street(int id ,double latA, double lonA,double latB, double lonB){
        this.id = id;
        this.latA = latA;
        this.latB = latB;
        this.lonA = lonA;

        this.lonB = lonB;
    }


// getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatA() {
        return latA;
    }

    public void setLatA(Double latA) {
        this.latA = latA;
    }

    public Double getLonA() {
        return lonA;
    }

    public void setLonA(Double lonA) {
        this.lonA = lonA;
    }

    public Double getLatB() {
        return latB;
    }

    public void setLatB(Double latB) {
        this.latB = latB;
    }

    public Double getLonB() {
        return lonB;
    }

    public void setLonB(Double lonB) {
        this.lonB = lonB;
    }

}
