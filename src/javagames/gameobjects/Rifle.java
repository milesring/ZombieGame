package javagames.gameobjects;

public class Rifle extends Weapon {
    public Rifle(){
        super(30, 0.1f, 3,2);
    }

    public String toString(){
        return "rifle";
    }

    public float barrelAngle(){
        return (float)Math.toRadians(20);
    }

    public float barrelLoc(){
        return 0.070f;
    }

    public String magazineType(){
        return "/effects/rifle_magazine.png";
    }
}
