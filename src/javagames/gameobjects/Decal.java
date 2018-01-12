package javagames.gameobjects;


import javagames.util.Vector2f;

import java.awt.image.BufferedImage;

public class Decal extends SpriteObject {

    public Decal(Vector2f p, float a, BufferedImage s){
        sprite = s;
        position = p;
        angle = a;
    }

    //no need to update
    public void updateWorld(float delta){

    }
}
