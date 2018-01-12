package javagames.gameobjects;


public class Handgun extends Weapon {

    public Handgun(){

        super(13, 0.0f, 1, 1);
    }

    public String toString(){
        return "handgun";
    }


    public float barrelAngle(){
        return (float)Math.toRadians(35);
    }

    public float barrelLoc(){
        return 0.05f;
    }

    public String magazineType(){
        return "/effects/handgun_magazine.png";
    }


}
