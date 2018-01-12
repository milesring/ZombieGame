package javagames.gameobjects;

import javagames.util.BoundingCircle;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;


public class Player extends SpriteObject {
    public boolean isWalking;
    public boolean isShooting;
    public boolean isReloading;
    public boolean droppedMag;

    protected BufferedImage playerSprite;
    protected BufferedImage feetSprite;

    float time;
    float frameDelay;
    public int playerFrame;
    int footFrame;
    public int health;
    protected BufferedImage[] feetidle;
    protected BufferedImage[] feetwalk;
    private LinkedList<LinkedList <BufferedImage[]>> weaponAnimations;

    protected Weapon equippedWeapon;
    protected LinkedList<Weapon> inventory;

    protected BoundingCircle boundingCircle;
    protected int selectedWeapon;
    public Player(){
        super();
        weaponAnimations = new LinkedList<>();
        isShooting = false;
        isWalking = false;
        isReloading = false;
        droppedMag = false;
        time = 0.0f;

        feetidle = new BufferedImage[1];
        feetwalk = new BufferedImage[20];

        frameDelay = 0.04f;
        playerFrame = 0;
        footFrame = 0;
        inventory = new LinkedList<>();
        health = 100;
        selectedWeapon = 0;

        createSprite();
        createhandgunAnims();
        createrifleAnims();

        inventory.add(new Handgun());
        inventory.add(new Rifle());
        equippedWeapon = inventory.get(selectedWeapon);
        playerSprite = weaponAnimations.get(selectedWeapon).get(0)[0];
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.05f), position);
    }

    /**
     *
     * creates player foot sprites
     */
    private void createSprite(){
        //create feetidle animations
        for(int i=0;i<feetidle.length;i++){
            try {
                feetidle[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/feet/idle/survivor-idle_" +i+".png"));
                feetidle[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, feetidle[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                feetidle[i] = null;
            }
        }

        //create feetwalk animations
        for(int i=0;i<feetwalk.length;i++){
            try {
                feetwalk[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/feet/walk/survivor-walk_" +i+".png"));
                feetwalk[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, feetwalk[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                feetwalk[i] = null;
            }
        }
        feetSprite = feetidle[0];
    }

    /**
     *
     * Creates handgun specific animations
     */
    private void createhandgunAnims(){
        BufferedImage[] handgunIdle;
        BufferedImage[] handgunShoot;
        BufferedImage[] handgunWalk;
        BufferedImage[] handgunReload;
        LinkedList<BufferedImage[]> handgunAnims;

        //animation order indexing
        //0 = idle
        //1 = shoot
        //2 = walk
        //3 = reload
        handgunAnims = new LinkedList<>();
        handgunIdle = new BufferedImage[20];
        handgunShoot = new BufferedImage[3];
        handgunWalk = new BufferedImage[20];
        handgunReload = new BufferedImage[15];


        //create idle animations
        for(int i=0;i<handgunIdle.length;i++){
            try {
                handgunIdle[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_" +i+".png"));
                handgunIdle[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR,  handgunIdle[i], 4);
            }
            catch (IOException e){
                e.printStackTrace();
                handgunIdle[i] = null;
            }
        }

        //create handgunShoot animations
        for(int i=0;i<handgunShoot.length;i++){
            try {
                handgunShoot[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/handgun/shoot/survivor-shoot_handgun_" +i+".png"));
                handgunShoot[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, handgunShoot[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                handgunShoot[i] = null;
            }
        }

        //create walking animations
        for(int i=0;i<handgunWalk.length;i++){
            try {
                handgunWalk[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/handgun/move/survivor-move_handgun_" +i+".png"));
                handgunWalk[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, handgunWalk[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                handgunWalk[i] = null;
            }
        }

        //create reload animations
        for(int i=0;i<handgunReload.length;i++){
            try {
                handgunReload[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/handgun/reload/survivor-reload_handgun_" +i+".png"));
                handgunReload[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, handgunReload[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                handgunReload[i] = null;
            }
        }

        handgunAnims.add(handgunIdle);
        handgunAnims.add(handgunShoot);
        handgunAnims.add(handgunWalk);
        handgunAnims.add(handgunReload);
        weaponAnimations.add(handgunAnims);
    }

    /**
     *
     * Creates rifle specific animations
     */
    private void createrifleAnims(){
        BufferedImage[] rifleIdle;
        BufferedImage[] rifleShoot;
        BufferedImage[] rifleWalk;
        BufferedImage[] rifleReload;
        LinkedList<BufferedImage[]> rifleAnims;

        //animation order indexing
        //0 = idle
        //1 = shoot
        //2 = walk
        //3 = reload
        rifleAnims = new LinkedList<>();
        rifleIdle = new BufferedImage[20];
        rifleShoot = new BufferedImage[3];
        rifleWalk = new BufferedImage[20];
        rifleReload = new BufferedImage[20];


        //create idle animations
        for(int i=0;i<rifleIdle.length;i++){
            try {
                rifleIdle[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/rifle/idle/survivor-idle_rifle_" +i+".png"));
                rifleIdle[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR,  rifleIdle[i], 4);
            }
            catch (IOException e){
                e.printStackTrace();
                rifleIdle[i] = null;
            }
        }

        //create rifleShoot animations
        for(int i=0;i<rifleShoot.length;i++){
            try {
                rifleShoot[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/rifle/shoot/survivor-shoot_rifle_" +i+".png"));
                rifleShoot[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, rifleShoot[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                rifleShoot[i] = null;
            }
        }

        //create walking animations
        for(int i=0;i<rifleWalk.length;i++){
            try {
                rifleWalk[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/rifle/move/survivor-move_rifle_" +i+".png"));
                rifleWalk[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, rifleWalk[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                rifleWalk[i] = null;
            }
        }

        //create reload animations
        for(int i=0;i<rifleReload.length;i++){
            try {
                rifleReload[i] = ImageIO.read(this.getClass().getResource("/Top_Down_Survivor/rifle/reload/survivor-reload_rifle_" +i+".png"));
                rifleReload[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, rifleReload[i],4);
            }
            catch (IOException e){
                e.printStackTrace();
                rifleReload[i] = null;
            }
        }

        rifleAnims.add(rifleIdle);
        rifleAnims.add(rifleShoot);
        rifleAnims.add(rifleWalk);
        rifleAnims.add(rifleReload);
        weaponAnimations.add(rifleAnims);
    }

    /**
     *
     * changes weapon selection based on scroll wheel
     * (dir is irrelevant due to only 2 weapons currently)
     * @param dir
     */
    public void cycleWeapon(int dir){
        if(dir>0){
            selectedWeapon++;
            if(selectedWeapon > inventory.size()-1){
                selectedWeapon = 0;
            }
        }
        else{
            selectedWeapon--;
            if(selectedWeapon < 0){
                selectedWeapon = inventory.size()-1;
            }
        }
        equippedWeapon = inventory.get(selectedWeapon);

    }

    /**
     *
     * returns equipped weapon
     * @return
     */
    public Weapon getEquippedWeapon(){
        return equippedWeapon;
    }

    /**
     *
     * initiates player shooting animation
     */
    public void shoot(){
        equippedWeapon.ammo--;
        playerFrame = 0;
        isShooting = true;
        playerSprite = weaponAnimations.get(selectedWeapon).get(1)[playerFrame];
    }

    /**
     *
     * initiates player reloading animation
     */
    public void reload(){
        playerFrame = 0;
        isShooting = false;
        isReloading = true;
        playerSprite = weaponAnimations.get(selectedWeapon).get(3)[playerFrame];
    }

    /**
     *
     * resets player for starting new level
     */
    public void reset(){
        angle = (float)Math.toRadians(0);
        getEquippedWeapon().reload();
        isReloading = false;
        playerFrame = 0;

    }

    public void updateWorld(float delta, Vector2f mouse) {
        super.updateWorld(delta);
        //move bounding circle with player
        boundingCircle.updateCenter(position);

        //track timee passed for animations
        time+=delta;

        //set upper body animation
        if(time > frameDelay) {
            time=0.0f;
            if (isShooting) {
                playerSprite = weaponAnimations.get(selectedWeapon).get(1)[playerFrame++];
                if (playerFrame > weaponAnimations.get(selectedWeapon).get(1).length-1) {
                    isShooting = false;
                    playerFrame = 0;
                }
            }
            else if(isReloading){
                playerSprite = weaponAnimations.get(selectedWeapon).get(3)[playerFrame++];

                if(playerFrame > weaponAnimations.get(selectedWeapon).get(3).length-1){
                    isReloading = false;
                    playerFrame = 0;
                    getEquippedWeapon().reload();
                    droppedMag = false;
                }
            }
            else if(isWalking){
                playerSprite = weaponAnimations.get(selectedWeapon).get(2)[playerFrame++];
                if (playerFrame > weaponAnimations.get(selectedWeapon).get(2).length-1) {
                    playerFrame = 0;
                }

            }
            else{
                playerSprite = weaponAnimations.get(selectedWeapon).get(0)[playerFrame++];
                if (playerFrame > weaponAnimations.get(selectedWeapon).get(0).length-1) {
                    playerFrame = 0;
                }
            }

            //walking animation
            if(isWalking){
                //animates feet
                if (footFrame > 19) {
                    footFrame = 0;
                }
                feetSprite = feetwalk[footFrame++];
            }
            else{
                feetSprite = feetidle[0];
            }
        }


        //estimated angle direction to face based off player and mouse position
        float estAngle = (float)Math.toDegrees(Math.atan2(mouse.x-position.x, mouse.y-position.y));


        if(Math.toDegrees(angle) > estAngle-1 && Math.toDegrees(angle) < estAngle+1){
            //do nothing, avoids jittery sprite
        }
        //follows correct spinning so the player doesnt do a 360 for no reason
        else if(estAngle > 90 && estAngle <= 180 && Math.toDegrees(angle) < -90){
            angle -= rotation * delta;
            if (Math.toDegrees(angle) < -180){
                angle = angle + (float)Math.toRadians(360);
            }
        }
        else if(estAngle < -90 && Math.toDegrees(angle) > 90){
            angle += rotation * delta;
            if(Math.toDegrees(angle) > 180 ){
                angle = angle - (float)Math.toRadians(360);
            }
        }
        else if(Math.toDegrees(angle) < estAngle){
            angle += rotation * delta;

        }
        else if(Math.toDegrees(angle) > estAngle){
            angle -= rotation * delta;
        }

    }
    public void render(Graphics g, Matrix3x3f viewport){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform ptransform = createTransform(position, angle, viewport, playerSprite);
        AffineTransform ftransform = createTransform(position, angle, viewport, feetSprite);
        g2d.drawImage(feetSprite, ftransform, null);
        g2d.drawImage(playerSprite, ptransform, null);
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

    /**
     *
     * used so the player only reloads once and only 1 magazine is created
     * @return
     */
    public int getmagdropFrame(){
        return weaponAnimations.get(selectedWeapon).get(3).length/2;
    }
}
