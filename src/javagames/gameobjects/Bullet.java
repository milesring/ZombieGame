package javagames.gameobjects;

import javagames.util.BoundingCircle;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Bullet extends SpriteObject {
     float speed;
     BoundingCircle boundingCircle;


    public Bullet(float angle, float x, float y, float ba, float bl){
        super();
        init();
        this.angle = angle;
        speed = 2.0f;


        //creates position at tip of gun
        //using ba(barrel angle) and bl (barrel length) stored in weapon class
        position.x = x+(float)Math.sin(angle+ba)*bl;
        position.y = y+(float)Math.cos(angle+ba)*bl;
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.005f),position);
        velocity.x = speed*(float)Math.sin(this.angle);
        velocity.y = speed*(float)Math.cos(this.angle);




    }

    private void init(){
        try {
           sprite = ImageIO.read(this.getClass().getResource("/effects/tracer.png"));
           sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 3);
        }
        catch (IOException e){
            e.printStackTrace();
           sprite = null;
        }

    }

    public void updateWorld(float delta){
        position = position.add(velocity.mul(delta));
        boundingCircle.updateCenter(position);
    }

    public BoundingCircle getBoundingCircle(){
        return boundingCircle;
    }




}
