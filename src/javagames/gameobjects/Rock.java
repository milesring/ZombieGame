package javagames.gameobjects;

import javagames.util.BoundingCircle;
import javagames.util.Vector2f;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Rock extends SpriteObject {
    private BoundingCircle boundingCircle;

    public Rock(Vector2f p, Vector2f r){
        acceleration = 0.0f;
        position = p;
        boundingCircle = new BoundingCircle(r, p);
        createRock();

    }


    public void createRock(){
        velocity.x = velocity.y = 0.0f;

        try {
            sprite = ImageIO.read(this.getClass().getResource("/rocks/rock.png"));
            sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 1);
        }
        catch (IOException e){
            e.printStackTrace();
            sprite = null;
        }
    }

    public BoundingCircle getBoundingCircle(){return boundingCircle;}

    public void updateWorld(float delta){
        boundingCircle.updateCenter(position);

        /*//Used for rock movement from player/other rocks
        if(acceleration < 0.001f){
            acceleration = 0.0f;
        }
        else
        {
            acceleration /= 1.1;
        }
        velocity = velocity.mul(acceleration);
        */
        super.updateWorld(delta);

    }

}
