package game;

import javagames.gameobjects.*;
import javagames.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;


public class Game extends SimpleFramework {
    private boolean inGame;
    private float charSpeed;
    private Point point;
    private LinkedList<Bullet> bullets;
    private Vector2f updateTime;
    private boolean drawCollision;
    private LinkedList<LinkedList<? extends SpriteObject>> gameObjects;
    private LinkedList<Player> player;
    private LinkedList<Decal> decals;
    private LinkedList<Wall> walls;
    private LinkedList<Rock> rocks;
    private LinkedList<Ground> ground;
    private LinkedList<Enemy> enemies;
    private LinkedList<Magazine> magazines;
    private int numEnemies;
    private float wallThickness;
    private NumberUtils numberUtils;
    private int rockCount;
    private int clickedRock;
    private int level;
    private int kills;
    private BufferedImage walkSheet;
    private BufferedImage idleSheet;
    private BufferedImage attackSheet;

    private Game() {
        appBackground = Color.WHITE;
        appBorder = Color.LIGHT_GRAY;
        appFont = new Font("Courier New", Font.PLAIN, 14);
        appBorderScale = 1.0f;
        appFPSColor = Color.BLACK;
        appWidth = 1280;
        appHeight = 1024;
        appMaintainRatio = true;
        appSleep = 10L;
        appTitle = "Zombies";
        appWorldWidth = 2.0f;
        appWorldHeight = 2.0f;
        viewFPS = true;
    }

    @Override
    protected void initialize() {
        //used a vector2f to have a time containing object with different fields
        updateTime = new Vector2f();
        gameObjects = new LinkedList<>();
        ground = new LinkedList<>();
        rocks = new LinkedList<>();
        walls = new LinkedList<>();
        player = new LinkedList<>();
        player.add(new Player());
        enemies = new LinkedList<>();
        magazines = new LinkedList<>();
        decals = new LinkedList<>();
        bullets = new LinkedList<>();

        inGame = false;
        level = 1;
        numberUtils = new NumberUtils();
        drawCollision = false;
        wallThickness = 0.15f;
        clickedRock = -1;

        //loading sprite sheets for enemies
        //large amounts of enemies eat up memory if sheet loaded in
        //sprite class.
        try {
            idleSheet = ImageIO.read(this.getClass().getResource("/Top_Down_Zombie/idle/idlesheet.png"));
            attackSheet = ImageIO.read(this.getClass().getResource("/Top_Down_Zombie/attack/attacksheet.png"));
            walkSheet = ImageIO.read(this.getClass().getResource("/Top_Down_Zombie/move/movesheet.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
            idleSheet = null;
            attackSheet = null;
            walkSheet = null;
        }

        initGround();
        initWalls();
        reset();
        super.initialize();

        //indexing according to rendering hierarchy
        //ground = index 0
        gameObjects.add(ground);
        //decals = index 1
        gameObjects.add(decals);
        //magazine = index 2
        gameObjects.add(magazines);
        //enemies = index 3
        gameObjects.add(enemies);
        //rocks = index 4
        gameObjects.add(rocks);
        //bullets = index 5
        gameObjects.add(bullets);
        //player = index 6
        gameObjects.add(player);
        //walls = index 7
        gameObjects.add(walls);
    }
    protected void initGround(){

        //CREATING GROUND TEXTURE
        //top left quad
        ground.add(new Ground(new Vector2f(-0.45f, 0.45f)));

        //top right quad
       ground.add(new Ground(new Vector2f(0.45f, 0.45f)));

        //bottom right quad
       ground.add(new Ground(new Vector2f(0.45f, -0.45f)));

        //bottom left quad
        ground.add(new Ground(new Vector2f(-0.45f, -0.45f)));
    }
    protected void initWalls(){
        Wall wall;
        //right wall
        wall = new Wall(new Vector2f(wallThickness, appWorldHeight), new Vector2f(appWorldWidth/2, 0.0f));
        walls.add(wall);

        //top wall
        wall = new Wall(new Vector2f(appWorldWidth-wallThickness, wallThickness), new Vector2f(0.0f, appWorldHeight/2));
        wall.setAngle((float)Math.toRadians(270));
        walls.add(wall);

        //left wall
        wall = new Wall(new Vector2f(wallThickness, appWorldHeight), new Vector2f(-appWorldWidth/2,0.0f));
        wall.setAngle((float)Math.toRadians(180));
        walls.add(wall);

        //bottom wall
        wall = new Wall(new Vector2f(appWorldWidth-wallThickness, wallThickness), new Vector2f(0.0f, -appWorldHeight/2));
        wall.setAngle((float)Math.toRadians(90));
        walls.add(wall);

    }
    protected void initRocks(){
        rockCount = 1;
        //CREATING ROCKS
        for(int i=0;i<rockCount;i++){
            rocks.add(new Rock(new Vector2f(numberUtils.randomFloat(-0.75f, 0.75f), numberUtils.randomFloat(-0.75f, 0.75f)), new Vector2f(0.0f, 0.1f)));
        }
    }
    protected void initPlayer(){

        charSpeed = 20.0f;
        //INITIAL PLAYER POSITION
        player.get(0).position.x = 0.0f;
        player.get(0).position.y = -0.75f;
        player.get(0).getEquippedWeapon().reload();

    }
    protected void initEnemies(){
        numEnemies = level * 4;
        //create zombies
        for(int i=0;i<numEnemies;i++) {
            enemies.add(new Enemy(new Vector2f(numberUtils.randomFloat(-0.75f, 0.75f), numberUtils.randomFloat(-0.25f,0.75f)),
                    idleSheet,walkSheet,attackSheet));
        }
    }

    protected void reset(){
        rocks.clear();
        enemies.clear();
        bullets.clear();
        magazines.clear();
        decals.clear();
        initRocks();
        initEnemies();
        initPlayer();
        player.get(0).reset();
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        point = mouse.getPosition();

        if(inGame) {
            updateTime.w += delta;

            //player attack
            if (mouse.buttonDownOnce(1)) {
                if (player.get(0).getEquippedWeapon().getAmmo() > 0 && updateTime.w > player.get(0).getEquippedWeapon().getRof()) {
                    player.get(0).shoot();
                    bullets.add(new Bullet(player.get(0).getEquippedWeapon().createSpread(player.get(0).getAngle()), player.get(0).position.x, player.get(0).position.y,
                            player.get(0).getEquippedWeapon().barrelAngle(), player.get(0).getEquippedWeapon().barrelLoc()));
                    updateTime.w = 0.0f;
                }
            }

            if(mouse.buttonDown(1) && !player.get(0).getEquippedWeapon().toString().equals("handgun")){
                if (player.get(0).getEquippedWeapon().getAmmo() > 0 && updateTime.w > player.get(0).getEquippedWeapon().getRof()) {
                    player.get(0).shoot();
                    bullets.add(new Bullet(player.get(0).getEquippedWeapon().createSpread(player.get(0).getAngle()), player.get(0).position.x, player.get(0).position.y,
                            player.get(0).getEquippedWeapon().barrelAngle(), player.get(0).getEquippedWeapon().barrelLoc()));
                    updateTime.w = 0.0f;
                }
            }

            //move rocks
            if (mouse.buttonDownOnce(3)) {
                for (int i = 0; i < rocks.size(); i++) {
                    BoundingCircle rock = rocks.get(i).getBoundingCircle();
                    if (rock.pointInCircle(getWorldMousePosition(), rock.getCenter(), rock.getRadius().y - rock.getRadius().x)) {
                        if (clickedRock != -1) {
                            rocks.get(clickedRock).position = getWorldMousePosition();
                            clickedRock = -1;
                        } else {
                            clickedRock = i;
                        }
                    }
                }
            }

            //change weapon
            if(mouse.getNotches()!=0){
                player.get(0).cycleWeapon(mouse.getNotches());
            }

            //player movement
            //up and left
            if (keyboard.keyDown(KeyEvent.VK_W) && keyboard.keyDown(KeyEvent.VK_A)) {
                player.get(0).isWalking = true;
                player.get(0).velocity = new Vector2f(-charSpeed / 2 * delta, charSpeed / 2 * delta);
            }
            //up and right
            else if (keyboard.keyDown(KeyEvent.VK_W) && keyboard.keyDown(KeyEvent.VK_D)) {
                player.get(0).isWalking = true;
                player.get(0).velocity = new Vector2f(charSpeed / 2 * delta, charSpeed / 2 * delta);
            }
            //down and left
            else if (keyboard.keyDown(KeyEvent.VK_S) && keyboard.keyDown(KeyEvent.VK_A)) {
                player.get(0).isWalking = true;
                player.get(0).velocity = new Vector2f(-charSpeed / 2 * delta, -charSpeed / 2 * delta);
            }
            //down and right
            else if (keyboard.keyDown(KeyEvent.VK_S) && keyboard.keyDown(KeyEvent.VK_D)) {
                player.get(0).isWalking = true;
                player.get(0).velocity = new Vector2f(charSpeed / 2 * delta, -charSpeed / 2 * delta);
            }
            //no diagonals
            else {
                //up
                if (keyboard.keyDown(KeyEvent.VK_W)) {
                    player.get(0).isWalking = true;
                    player.get(0).velocity.y = charSpeed * delta;
                }
                //down
                else if (keyboard.keyDown(KeyEvent.VK_S)) {
                    player.get(0).isWalking = true;
                    player.get(0).velocity.y = -charSpeed * delta;
                } else {
                    player.get(0).velocity.y = 0.0f;
                }
                //left
                if (keyboard.keyDown(KeyEvent.VK_A)) {
                    player.get(0).isWalking = true;
                    player.get(0).velocity.x = -charSpeed * delta;
                }
                //right
                else if (keyboard.keyDown(KeyEvent.VK_D)) {
                    player.get(0).isWalking = true;
                    player.get(0).velocity.x = charSpeed * delta;
                } else {
                    player.get(0).velocity.x = 0.0f;
                }
            }

            //reload weapon
            if (keyboard.keyDownOnce(KeyEvent.VK_R)) {
                player.get(0).reload();
            }

            //draw bounding shapes
            if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
                drawCollision = !drawCollision;
            }

            if (keyboard.keyDownOnce(KeyEvent.VK_F1)) {
                viewFPS = !viewFPS;
            }
        }
        if(!inGame) {
            if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
                inGame = true;
            }
        }
    }

    @Override
    protected void updateObjects(float delta) {
        updateTime.y += delta;
        if(inGame) {
            //health check
            if(player.get(0).health<=0){
                inGame = false;
                clickedRock = -1;
                level = 1;
                kills = 0;
                player.get(0).health = 100;
                reset();
            }

            //checking if player is idle
            if (player.get(0).velocity.x == 0.0f && player.get(0).velocity.y == 0.0f) {
                player.get(0).isWalking = false;
            }

            //adding magazine creation
            if(player.get(0).isReloading && player.get(0).playerFrame == player.get(0).getmagdropFrame() && !player.get(0).droppedMag){
                magazines.add(new Magazine(player.get(0).position, player.get(0).getAngle(),player.get(0).getEquippedWeapon().magazineType()));
                player.get(0).droppedMag = true;
            }

            //checking for rock moving
            if (clickedRock != -1) {
                rocks.get(clickedRock).position = getWorldMousePosition();
            }

            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).updateWorld(delta, player.get(0).position);

                //checking death and world bounds
                if(enemies.get(i).isDead || enemies.get(i).position.x > 1.0f ||
                        enemies.get(i).position.x < -1.0f || enemies.get(i).position.y > 1.0f ||
                        enemies.get(i).position.y < -1.0f){
                    if(enemies.get(i).isDead){
                        //adding at index 0 insures that blood will be rendered before other decals(aka beneath them)
                        decals.add(0,new Decal(enemies.get(i).position, enemies.get(i).getAngle(), enemies.get(i).getSprite()));
                    }
                    enemies.remove(i);
                }
            }

            for (int i = 0; i < rocks.size(); i++) {
                rocks.get(i).updateWorld(delta);
            }
            for (int i = 0; i < walls.size(); i++) {
                walls.get(i).updateWorld(delta);
            }
            for (int i = 0; i < magazines.size(); i++) {
                magazines.get(i).updateWorld(delta);
                if(magazines.get(i).velocity.x == 0.0f && magazines.get(i).velocity.y == 0.0f){
                    decals.add(new Decal(magazines.get(i).position, magazines.get(i).getAngle(), magazines.get(i).getSprite()));
                    magazines.remove(i);
                }
            }
            if(enemies.isEmpty()){
                updateTime.x+=delta;
                if(updateTime.x > 2.0f) {
                    level++;
                    clickedRock = -1;
                    updateTime.x = 0.0f;
                    inGame = false;
                    reset();
                }
            }
            super.updateObjects(delta);
            checkCollisions(delta);
        }
    }

    private void checkCollisions(float delta){
        BoundingCircle bc = player.get(0).getBoundingCircle();
        player.get(0).updateWorld(delta, getReverseViewportTransform().mul(new Vector2f(point.x, point.y)));

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            BoundingCircle enemy = enemies.get(i).getBoundingCircle();
            //player
            if (bc.intersect(bc.getCenter(), bc.getRadius().y - bc.getRadius().x, enemy.getCenter(), enemy.getRadius().y - enemy.getRadius().x)) {

                //Useful for not allowing players/zombies to clip through each other.
                //creates wild glitches with high amounts of enemies (level 40+)
                    /*
                    player.get(0).position.x -= player.get(0).velocity.x * delta;
                    player.get(0).position.y -= player.get(0).velocity.y * delta;
                    enemies.get(i).position.x -= enemies.get(i).velocity.x * delta;
                    enemies.get(i).position.y -= enemies.get(i).velocity.y * delta;
                    */

                if (!enemies.get(i).isAttacking && !enemies.get(i).isDying) {
                    enemies.get(i).attack();
                }
                //ensures that the zombie only attacks once per animation cycle
                if (enemies.get(i).isAttacking && enemies.get(i).enemyFrame == 6 && !enemies.get(i).hitPlayer) {
                    enemies.get(i).hitPlayer = true;
                    player.get(0).health -= 25;
                }
            }

            //rocks
            if (!rocks.isEmpty()) {
                for (int j = 0; j < rocks.size(); j++) {
                    BoundingCircle rock = rocks.get(j).getBoundingCircle();
                    if (enemy.intersect(enemy.getCenter(), enemy.getRadius().y - enemy.getRadius().x, rock.getCenter(), rock.getRadius().y - rock.getRadius().x)) {
                        enemies.get(i).position.x -= enemies.get(i).velocity.x * delta;
                        enemies.get(i).position.y -= enemies.get(i).velocity.y * delta;
                    }
                }
            }

            //Checking wall collisions
            if (!walls.isEmpty()) {
                for (int j = 0; j < walls.size(); j++) {
                    BoundingBox box = walls.get(j).getBoundingBox();
                    if (enemy.intersect(enemy.getCenter(), enemy.getRadius().y - enemy.getRadius().x, box.getMin(), box.getMax())) {
                        enemies.get(i).position.x -= enemies.get(i).velocity.x * delta;
                        enemies.get(i).position.y -= enemies.get(i).velocity.y * delta;
                    }
                }
            }

            //doesn't allow zombies to clip through each other
            //doesn't work well with medium amounts of enemies
                /*
                //other enemies
                for(int j=0;j<enemies.size();j++){
                    if(j==i){
                        continue;
                    }
                    BoundingCircle enemy2 = enemies.get(j).getBoundingCircle();

                    if (enemy.intersect(enemy.getCenter(), enemy.getRadius().y - enemy.getRadius().x, enemy2.getCenter(), enemy2.getRadius().y - enemy2.getRadius().x)) {
                        enemies.get(j).position.x -= enemies.get(j).velocity.x * delta;
                        enemies.get(j).position.y -= enemies.get(j).velocity.y * delta;
                        enemies.get(i).position.x -= enemies.get(i).velocity.x * delta;
                        enemies.get(i).position.y -= enemies.get(i).velocity.y * delta;
                    }
                }
                */
        }
        //Player collision w/ walls
        for (int i = 0; i < walls.size(); i++) {
            BoundingBox bb = walls.get(i).getBoundingBox();
            if (bc.intersect(bc.getCenter(), bc.getRadius().y - bc.getRadius().x, bb.getMin(), bb.getMax())) {
                player.get(0).position.x -= player.get(0).velocity.x * delta;
                player.get(0).position.y -= player.get(0).velocity.y * delta;
            }
        }

        //rock collisions
        if (!rocks.isEmpty()) {
            for (int i = 0; i < rocks.size(); i++) {
                BoundingCircle rock = rocks.get(i).getBoundingCircle();

                //with player
                if (bc.intersect(bc.getCenter(), bc.getRadius().y - bc.getRadius().x, rock.getCenter(), rock.getRadius().y - rock.getRadius().x)) {
                    player.get(0).position.x -= player.get(0).velocity.x * delta;
                    player.get(0).position.y -= player.get(0).velocity.y * delta;

                    /*
                    //for pushing rocks with player
                    if(player.get(0).velocity.x == 0.0f){
                        rocks.get(i).velocity.x = 0.0f;
                    }
                    else if(player.get(0).velocity.x > 0) {
                        rocks.get(i).velocity.x = 0.5f;
                    }
                    else{
                        rocks.get(i).velocity.x = -0.5f;
                    }

                    if(player.get(0).velocity.y == 0.0f){
                        rocks.get(i).velocity.y = 0.0f;
                    }
                    else if(player.get(0).velocity.y > 0) {
                        rocks.get(i).velocity.y = 0.5f;
                    }
                    else{
                        rocks.get(i).velocity.y = -0.5f;
                    }
                    rocks.get(i).acceleration = 0.5f;
                    */
                }

                //Only needed if player can move rocks by pushing
                /*
                //with walls
                for(int j = 0; j<walls.size(); j++){
                    BoundingBox box = walls.get(j).getBoundingBox();
                    if (rock.intersect(rock.getCenter(), rock.getRadius().y - rock.getRadius().x, box.getMin(), box.getMax())) {
                        rocks.get(i).position.x -= rocks.get(i).velocity.x * delta;
                        rocks.get(i).position.y -= rocks.get(i).velocity.y * delta;
                    }

                }

                //with other rocks
                for(int j=0;j<rocks.size();j++){
                    if(j==i){
                        continue;
                    }
                    BoundingCircle rock2 = rocks.get(j).getBoundingCircle();
                    if (rock.intersect(rock2.getCenter(), rock2.getRadius().y - rock2.getRadius().x, rock.getCenter(), rock.getRadius().y - rock.getRadius().x)){
                        rocks.get(i).position.x -= rocks.get(i).velocity.x * delta;
                        rocks.get(i).position.y -= rocks.get(i).velocity.y * delta;
                    }
                }
                */
            }
        }

        //bullet collisions
        if (!bullets.isEmpty()) {
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).updateWorld(delta);
                BoundingCircle bullet = bullets.get(i).getBoundingCircle();

                //Checking world boundries
                //not needed with walls
                    /*
                    if (bullets.get(i).position.x > 1.0f || bullets.get(i).position.x < -1.0f ||
                            bullets.get(i).position.y > 1.0f || bullets.get(i).position.y < -1.0f) {
                        bullets.remove(i);
                    }
                    */

                //Checking wall collisions
                if (!walls.isEmpty()) {
                    for (int j = 0; j < walls.size(); j++) {
                        BoundingBox box = walls.get(j).getBoundingBox();
                        if (bullet.intersect(bullet.getCenter(), bullet.getRadius().y - bullet.getRadius().x, box.getMin(), box.getMax())) {
                            //checking again accounts for bullet hitting two walls at once, i.e. inside corner of square
                            if (!bullets.isEmpty()) {
                                bullets.remove(i);
                            }
                        }
                    }
                }

                //Checking enemy collisions
                if (!enemies.isEmpty()) {
                    for (int j = 0; j < enemies.size(); j++) {
                        BoundingCircle enemy = enemies.get(j).getBoundingCircle();
                        if (bullet.intersect(bullet.getCenter(), bullet.getRadius().y - bullet.getRadius().x, enemy.getCenter(), enemy.getRadius().y - enemy.getRadius().x)) {
                            //checking again accounts for bullet hitting two enemies at once
                            if (!(i>bullets.size()-1)) {
                                bullets.remove(i);
                            }
                            if (!enemies.isEmpty()) {
                                kills++;
                                enemies.get(j).die();
                            }
                        }
                    }
                }

                //Checking rock collisions
                if (!rocks.isEmpty()) {
                    for (int j = 0; j < rocks.size(); j++) {
                        BoundingCircle rock = rocks.get(j).getBoundingCircle();
                        if (bullet.intersect(bullet.getCenter(), bullet.getRadius().y - bullet.getRadius().x, rock.getCenter(), rock.getRadius().y - rock.getRadius().x)) {
                            //checking again accounts for bullet hitting two rocks at once
                            if (!(i>bullets.size()-1)) {
                                bullets.remove(i);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void render(Graphics g) {

        if(inGame) {
            //ground
            for (int i=0;i<gameObjects.size();i++){
                renderList(gameObjects.get(i), g);
            }
            //player
            player.get(0).render(g, getViewportTransform());

            //draws bounding shapes of objects.
            if (drawCollision) {
               renderCollisions(g);
            }
            drawUI(g);
        }
        else{
           drawMenu(g);
        }
        super.render(g);
    }

    private void renderCollisions(Graphics g){
        g.setColor(Color.RED);
        Matrix3x3f viewport = getViewportTransform();

        //Player
        Vector2f pos = getViewportTransform().mul(player.get(0).position);
        Vector2f radius = getViewportTransform().mul(player.get(0).getBoundingCircle().getRadius());
        float radiusL = radius.x - radius.y;
        g.drawOval((int) pos.x - (int) radiusL, (int) pos.y - (int) radiusL, (int) radiusL * 2, (int) radiusL * 2);

        //Enemies
        for (int i = 0; i < enemies.size(); i++) {
            Vector2f zompos = viewport.mul(enemies.get(i).position);
            Vector2f zomradius = viewport.mul(enemies.get(i).getBoundingCircle().getRadius());
            float zomradiusLen = zomradius.x - zomradius.y;
            g.drawOval((int) zompos.x - (int) zomradiusLen, (int) zompos.y - (int) zomradiusLen, (int) zomradiusLen * 2, (int) zomradiusLen * 2);
        }

        //bullet circles
        for (int i = 0; i < bullets.size(); i++) {
            Vector2f bulletpos = viewport.mul(bullets.get(i).position);
            Vector2f bulletradius = viewport.mul(bullets.get(i).getBoundingCircle().getRadius());
            float bulletradiusLen = bulletradius.x - bulletradius.y;
            g.drawOval((int) bulletpos.x - (int) bulletradiusLen, (int) bulletpos.y - (int) bulletradiusLen, (int) bulletradiusLen * 2, (int) bulletradiusLen * 2);
        }

        //Wall boxes
        for (int i = 0; i < walls.size(); i++) {
            BoundingBox wall = walls.get(i).getBoundingBox();
            Vector2f start = viewport.mul(new Vector2f(wall.getMin().x, wall.getMax().y));
            Vector2f max = viewport.mul(wall.getMax());
            Vector2f min = viewport.mul(wall.getMin());
            g.drawRect((int) start.x, (int) start.y,
                    (int) max.x - (int) min.x, (int) min.y - (int) max.y);
        }

        //Rocks
        for (int i = 0; i < rocks.size(); i++) {
            BoundingCircle rock = rocks.get(i).getBoundingCircle();
            Vector2f rockpos = viewport.mul(rocks.get(i).position);
            Vector2f rockradius = viewport.mul(rock.getRadius());
            float rockradiusL = rockradius.x - rockradius.y;
            g.drawOval((int) rockpos.x - (int) rockradiusL, (int) rockpos.y - (int) rockradiusL, (int) rockradiusL * 2, (int) rockradiusL * 2);
        }
    }
    private void drawUI(Graphics g){
        String ammoString;
        String healthString;
        g.setColor(new Color(0.0f,0.0f,0.0f,0.6f));
        g.fillRoundRect((int)getViewportTransform().mul(player.get(0).position).x-8, (int)getViewportTransform().mul(player.get(0).position).y+25, 50, 20,15,15);
        g.fillRoundRect((int)getViewportTransform().mul(player.get(0).position).x-55, (int)getViewportTransform().mul(player.get(0).position).y-35, 42, 20,15,15);
        g.setColor(Color.GREEN);
        g.setFont(new Font(appFont.getFamily(), appFont.getStyle(), 15));
        //draw health status
        if(player.get(0).health<100){
            healthString = " "+player.get(0).health + "%";
        }
        else{
            healthString = player.get(0).health + "%";
        }
        g.drawString(healthString, (int)getViewportTransform().mul(player.get(0).position).x-50,
                (int)getViewportTransform().mul(player.get(0).position).y-20);

        //draw ammo status
        if(player.get(0).getEquippedWeapon().getAmmo()<10){
            ammoString = " "+player.get(0).getEquippedWeapon().getAmmo() + "/" + player.get(0).getEquippedWeapon().getCapacity();
        }else {
            ammoString = player.get(0).getEquippedWeapon().getAmmo() + "/" + player.get(0).getEquippedWeapon().getCapacity();
        }
        g.drawString(ammoString,(int) getViewportTransform().mul(player.get(0).position).x - 5,
                (int) getViewportTransform().mul(player.get(0).position).y + 40);


        g.setFont(appFont);
        g.setColor(new Color(0.0f,0.0f,0.0f,0.6f));
        g.fillRoundRect((int)getViewportTransform().mul(new Vector2f(-0.25f,0.0f)).x, (int)(canvas.getHeight()*0.01),
                (int)getViewportTransform().mul(new Vector2f(0.25f,0.0f)).x-(int)getViewportTransform().mul(new Vector2f(-0.25f,0.0f)).x,30,15,15);

        g.setColor(Color.GREEN);
        g.drawString("Kills: "+kills, (int)getViewportTransform().mul(new Vector2f(0.0f,0.0f)).x, (int)(canvas.getHeight()*0.025));
        g.drawString("Level: "+level,(int)getViewportTransform().mul(new Vector2f(-0.20f,0.0f)).x, (int)(canvas.getHeight()*0.025));

        g.setColor(Color.black);

        g.drawString("Elapsed time: "+updateTime.y, 100,100);

    }
    private void drawMenu(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        g.setColor(Color.red);
        g.setFont(new Font(appFont.getFamily(), appFont.getStyle(), 20));
        g.drawString("Level "+level,canvas.getWidth()/2,canvas.getHeight()/2);
        g.drawString("Press space to start", canvas.getWidth()/2, canvas.getHeight()/2+20);
        g.setFont(appFont);
        g.drawString("Kills: "+kills, 25, 20);
    }
    private <T extends SpriteObject> void renderList(LinkedList<T> list, Graphics g){
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).render(g, getViewportTransform());
            }
        }
    }


    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new Game());
    }
}
