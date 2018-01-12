package javagames.util;

/**
 * Created by Nielsen on 3/15/2017.
 */
public class BoundingBox extends BoundingShape {

protected Vector2f dimensions;
protected Vector2f point;
protected Vector2f min, max;
    public BoundingBox(Vector2f d, Vector2f p){
        this.dimensions = d;
        this.point = p;

        max = new Vector2f(p.x+d.x/2, p.y+d.y/2);
        min = new Vector2f(p.x-d.x/2, p.y-d.y/2);
    }

    public Vector2f getMin(){return min;}
    public Vector2f getMax(){return max;}
    public Vector2f getPoint(){return point;}
    public Vector2f getDimensions(){return dimensions;}
}
