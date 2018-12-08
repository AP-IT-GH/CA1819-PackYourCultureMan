package com.ap.pacyourcultureman;

public class VisitedSight {

    private int id,buildingId,userId;
    private boolean isChecked;

    //constructor voor get
    public VisitedSight(int id,int buildingId,int userId,boolean isChecked) {
        this.id = id;
        this.buildingId = buildingId;
        this.userId = userId;
        this.isChecked = isChecked;
    }

    //constructor voor put
    public VisitedSight(int buildingId,boolean isChecked) {
        this.buildingId = buildingId;
        this.isChecked = isChecked;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}