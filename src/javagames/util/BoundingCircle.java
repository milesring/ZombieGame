package javagames.util;

/**
 * Created by Nielsen on 3/15/2017.
 */
public class BoundingCircle extends BoundingShape {

    Vector2f radius;
    Vector2f center;

    public BoundingCircle(Vector2f r, Vector2f c){
        radius = r;
        center = c;
    }

    public Vector2f getRadius(){return radius;}
    public Vector2f getCenter(){return center;}
    public void updateCenter(Vector2f c){center = c;}
}
