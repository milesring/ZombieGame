package javagames.gameobjects;

import javagames.util.NumberUtils;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Magazine extends SpriteObject {
    float speed;
    float updateRate;
    float timeElasped;
    NumberUtils numberUtils;

    /**
     *
     * creates magazine object at Vector position, angle, and of specific weapon type
     * @param p
     * @param playerAngle
     * @param file
     */
    public Magazine(Vector2f p, float playerAngle, String file){
        numberUtils = new NumberUtils();
        acceleration = 10.0f;
        speed = 10.0f;
        position = p;
        rotation = numberUtils.randomFloat(-3.0f, 3.0f);
        updateRate = 0.4f;
        timeElasped = 0.0f;
        angle = (float)Math.toRadians(numberUtils.randomFloat(-180.0f, 180.0f));

        //initial velocity
        velocity.x=speed*(float)Math.sin(playerAngle-Math.toRadians(90));
        velocity.y=speed*(float)Math.cos(playerAngle-Math.toRadians(90));

        createSprite(file);

    }

    private void createSprite(String file){
        try {
            sprite = ImageIO.read(this.getClass().getResource(file));
        }
        catch(IOException e){
            e.printStackTrace();
            sprite = null;
        }
    }

    public void updateWorld(float delta){
        timeElasped+=delta;

        //update rotation and acceleration
        if(timeElasped>updateRate) {
            rotation *= 0.3f;
            acceleration*= 0.99f;
            timeElasped = 0.0f;
        }

        //rotate and update velocity
        angle += rotation * delta;
        velocity=velocity.mul(acceleration);
        velocity=velocity.mul(delta);

        super.updateWorld(delta);
    }

}
