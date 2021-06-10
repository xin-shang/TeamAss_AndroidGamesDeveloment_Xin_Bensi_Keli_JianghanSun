package com.mygdx.game.sprites;

/**
 * @author Xin Shang
 * */
public class TankStatus {
    private String name;
    private int health;
    private float moveFreq;
    private float fireFreq;
    private boolean canFire;

    public TankStatus(String name, int health, float moveFreq, float fireFreq, boolean canFire) {
        this.name = name;
        this.health = health;
        this.moveFreq = moveFreq;
        this.fireFreq = fireFreq;
        this.canFire = canFire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getMoveFreq() {
        return moveFreq;
    }

    public void setMoveFreq(float moveFreq) {
        this.moveFreq = moveFreq;
    }

    public float getFireFreq() {
        return fireFreq;
    }

    public void setFireFreq(float fireFreq) {
        this.fireFreq = fireFreq;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }
}
