package javagames.gameobjects;

import javagames.util.Vector2f;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Ground extends SpriteObject {

    public Ground(Vector2f p){
        super();
        position = p;
        init();
    }

    public void init(){
        try {
            sprite = ImageIO.read(this.getClass().getResource("/ground/testgrass.png"));
            sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 1);
        }
        catch (IOException e){
            e.printStackTrace();
            sprite = null;
        }
    }

}
