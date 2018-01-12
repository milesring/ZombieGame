package javagames.gameobjects;


import javagames.util.NumberUtils;

public class Weapon {

    int capacity;
    int ammo;
    float rof;
    float spread;
    int weaponType;
    NumberUtils numberUtils;

    public Weapon(int c, float r, float s, int t){
        capacity = c;
        rof = r;
        spread = s;
        weaponType = t;
        numberUtils = new NumberUtils();

    }

    /**
     *
     * returns total capacity of weapon
     * @return
     */
    public int getCapacity(){return capacity;}

    /**
     *
     * returns current ammo in weapon
     * @return
     */
    public int getAmmo(){return ammo;}

    /**
     *
     * returns weapon rate of fire
     * @return
     */
    public float getRof(){return rof;}

    /**
     *
     * returns spread value of weapon
     * @return
     */
    public float getSpread(){return spread;}

    /**
     *
     * returns weaponType, not sure where I wanted to use this
     * @return
     */
    public int getweaponType(){return weaponType;}

    /**
     *
     * creates a random value within the spread value of weapon
     * @param angle
     * @return
     */
    public float createSpread(float angle){
        return numberUtils.randomFloat(angle-(float)Math.toRadians(spread), angle+(float)Math.toRadians(spread));
    }
    public void reload(){
        ammo = capacity;
    }

    public String toString(){
        return "weapon";
    }

    /**
     *
     * useful for bullet creation location
     * @return
     */
    public float barrelAngle(){
        return (float)Math.toRadians(0);
    }

    /**
     *
     * useful for bullet creation location
     * @return
     */
    public float barrelLoc(){
        return 0.0f;
    }

    public String magazineType(){
        return "";
    }
}
