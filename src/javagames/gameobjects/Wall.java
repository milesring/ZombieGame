package javagames.gameobjects;

import javagames.util.BoundingBox;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


public class Wall extends SpriteObject {

    private BoundingBox boundingBox;
    private Vector2f dimensions;
    public Wall(Vector2f d, Vector2f p){
        position = p;
        dimensions = d;
        boundingBox = new BoundingBox(dimensions, position);
        createWall();

    }


    public void createWall(){
        try {
           sprite = ImageIO.read(this.getClass().getResource("/ground/grass00.png"));
           sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 1);
        }
        catch (IOException e){
            e.printStackTrace();
            sprite = null;
        }

    }

    public BoundingBox getBoundingBox(){return boundingBox;}


}
