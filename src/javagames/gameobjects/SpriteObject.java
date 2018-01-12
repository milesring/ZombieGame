package javagames.gameobjects;


import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteObject{
    protected BufferedImage sprite;

    public Vector2f position;
    public Vector2f velocity;
    public float acceleration;
    protected float rotation;
    protected float angle;
    protected int time;

    public SpriteObject(){
        init();

    }

    private void init(){
        acceleration = 0.0f;
        position = new Vector2f(0.0f, 0.0f);
        velocity = new Vector2f(0.0f, 0.0f);
        rotation = 2.5f;
        angle = (float)Math.toRadians(0);

        time = 0;


    }


    public float getAngle(){
        return angle;
    }

    public void setAngle(float a){ angle = a;}

    public BufferedImage scaleWithGraphics(Object hintValue, BufferedImage sprite, int scale) {
        BufferedImage image = new BufferedImage(sprite.getWidth() / scale,
                sprite.getHeight() / scale, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
        g2d.drawImage(sprite, 0, 0, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        return image;
    }



    public void updateWorld(float delta){
        position = position.add(velocity.mul(delta));

    }

    public void render(Graphics g, Matrix3x3f viewport){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = createTransform(position, angle, viewport);
        g2d.drawImage(sprite, transform, null);

    }


    public AffineTransform createTransform(Vector2f position, float angle, Matrix3x3f viewport){
        Vector2f screen = viewport.mul(position);
        AffineTransform transform = AffineTransform.getTranslateInstance(screen.x, screen.y);
        transform.rotate(angle);
        transform.translate(-sprite.getWidth()/2, -sprite.getHeight()/2);
        return transform;
    }

    public BufferedImage getSprite(){
        return sprite;
    }

}

