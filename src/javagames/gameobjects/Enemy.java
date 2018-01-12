package javagames.gameobjects;

import javagames.util.BoundingCircle;
import javagames.util.Matrix3x3f;
import javagames.util.NumberUtils;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends SpriteObject {
    public boolean isWalking;
    public boolean isAttacking;
    public boolean isDying;
    public boolean isDead;
    public boolean hitPlayer;

    protected BufferedImage enemySprite;

    float attackTime;
    float time;
    float frameDelay;
    float moveSpeed;
    public int enemyFrame;

    protected BufferedImage[] idle;
    protected BufferedImage[] attack;
    protected BufferedImage[] walk;
    protected BufferedImage death;


    protected BoundingCircle boundingCircle;

    NumberUtils numberUtils;

    public Enemy(Vector2f p, BufferedImage i, BufferedImage w, BufferedImage a){
        super();
        numberUtils = new NumberUtils();
        position = p;
        isAttacking = false;
        isWalking = false;
        hitPlayer = false;
        isDead = false;
        isDying = false;
        time = 0.0f;
        attackTime = 0.0f;
        idle = new BufferedImage[17];
        attack = new BufferedImage[9];
        walk = new BufferedImage[17];
        frameDelay = 0.04f;
        enemyFrame = 0;

        moveSpeed = numberUtils.randomFloat(5.0f,15.0f);
        createSprite(i,w,a);
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.05f), position);
    }

    /**
     *
     * creates sprite based off of preloaded sprite sheets in main class
     * @param idleSheet
     * @param walkSheet
     * @param attackSheet
     */
    private void createSprite(BufferedImage idleSheet, BufferedImage walkSheet, BufferedImage attackSheet){
        for(int i=0;i<=2;i++){
            for(int j=0;j<6;j++) {
                if(i*6+j>16){
                    break;
                }
                idle[i*6+j] = idleSheet.getSubimage(j * 222, i * 241, 222, 241);
                idle[i*6+j] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, idle[i*6+j],4);
            }
        }

        //create attack animations
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++) {
                attack[i * 3 + j] = attackSheet.getSubimage(j * 294, i * 318, 294, 318);
                attack[i * 3 + j] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, attack[i * 3 + j], 4);
            }
        }

        //create walking animations
        for(int i=0;i<=2;i++){
            for(int j=0;j<6;j++) {
                if(i*6+j>16){
                    break;
                }
                walk[i*6+j] = walkSheet.getSubimage(j * 311, i * 288, 311, 288);
                walk[i*6+j] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, walk[i*6+j],4);
            }
        }

        //death sprite
        try{
            death = ImageIO.read(this.getClass().getResource("/effects/blood.png"));
            death = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR,death,4);
        }
        catch (IOException e){
            e.printStackTrace();
            death = null;
        }

        enemySprite = idle[0];
    }

    /**
     *
     * initiates attack animation
     */
    public void attack(){
        enemyFrame = 0;
        isAttacking = true;
        hitPlayer = false;
        sprite = attack[enemyFrame];
    }

    /**
     *
     * initiates dying sequence
     */
    public void die(){
        enemyFrame = 0;
        sprite = death;
        isDead = true;
        isAttacking = false;
        isWalking = false;
        //moves boundingcircle to avoid death sprite blocking bullets
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.0f), new Vector2f(2.0f, 2.0f));

    }

    public void updateWorld(float delta, Vector2f player) {
        super.updateWorld(delta);

        //moves toward player
            if (player.x < position.x && player.y < position.y) {
                velocity = new Vector2f(-moveSpeed / 2 * delta, -moveSpeed / 2 * delta);
                isWalking = true;
            } else if (player.x < position.x && player.y > position.y) {
                velocity = new Vector2f(-moveSpeed / 2 * delta, moveSpeed / 2 * delta);
                isWalking = true;
            } else if (player.x > position.x && player.y < position.y) {
                velocity = new Vector2f(moveSpeed / 2 * delta, -moveSpeed / 2 * delta);
                isWalking = true;
            } else if (player.x > position.x && player.y > position.y) {
                velocity = new Vector2f(moveSpeed / 2 * delta, moveSpeed / 2 * delta);
                isWalking = true;
            } else {

                if (player.x < position.x) {
                    velocity.x = -moveSpeed * delta;
                    isWalking = true;
                } else if (player.x > position.x) {
                    velocity.x = moveSpeed * delta;
                    isWalking = true;
                }

                if (player.y < position.y) {
                    velocity.y = -moveSpeed * delta;
                    isWalking = true;
                } else if (player.y > position.y) {
                    velocity.y = moveSpeed * delta;
                    isWalking = true;
                }
            }

        //idle state
        if(velocity.x == 0.0f && velocity.y == 0.0f){
            isWalking = false;
        }
        boundingCircle.updateCenter(position);
        time+=delta;

        //animates zombie
        if(time > frameDelay) {
            time=0.0f;

            if(isAttacking){
                if(enemyFrame > 8){
                    enemyFrame = 0;
                    isAttacking = false;
                }
                enemySprite = attack[enemyFrame++];
            }
            else if (isWalking) {
                if (enemyFrame > 16) {
                    enemyFrame = 0;
                }
                enemySprite = walk[enemyFrame++];
            }else {
               if(enemyFrame > 16){
                   enemyFrame = 0;
               }
               enemySprite = idle[enemyFrame++];
            }
        }

        //rotates zombie to face player
        float estAngle = (float) Math.toDegrees(Math.atan2(player.x - position.x, player.y - position.y));

        if (Math.toDegrees(angle) > estAngle - 1 && Math.toDegrees(angle) < estAngle + 1) {
            //do nothing, avoids jittery sprite
        } else if (estAngle > 90 && estAngle <= 180 && Math.toDegrees(angle) < -90) {
            angle -= rotation * delta;
            if (Math.toDegrees(angle) < -180) {
                angle = angle + (float) Math.toRadians(360);
            }
        } else if (estAngle < -90 && Math.toDegrees(angle) > 90) {
            angle += rotation * delta;
            if (Math.toDegrees(angle) > 180) {
                angle = angle - (float) Math.toRadians(360);
            }
        } else if (Math.toDegrees(angle) < estAngle) {
            angle += rotation * delta;

        } else if (Math.toDegrees(angle) > estAngle) {
            angle -= rotation * delta;
        }
    }

    public void render(Graphics g, Matrix3x3f viewport){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = createTransform(position, angle, viewport, enemySprite);
        g2d.drawImage(enemySprite, transform, null);
    }

    public AffineTransform createTransform(Vector2f position, float angle, Matrix3x3f viewport, BufferedImage sp){
        Vector2f screen = viewport.mul(position);
        AffineTransform transform = AffineTransform.getTranslateInstance(screen.x, screen.y);
        transform.rotate(angle);
        transform.translate(-sp.getWidth()/2, -sp.getHeight()/2);
        return transform;
    }

    public BoundingCircle getBoundingCircle(){
        return boundingCircle;
    }

}
