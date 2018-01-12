package javagames.util;

/**
 * Created by Nielsen on 3/15/2017.
 */
public class BoundingShape {


    //Circle & circle
    public boolean intersect(Vector2f c0, float r0, Vector2f c1, float r1) {
        Vector2f c = c0.sub(c1); //Get the vector between centers by subtracting
        float r = r0 + r1; //Add the radii
        return c.lenSqr() < r * r; //Avoid the sqrt by squaring r
    }

    //Box & box
    public boolean intersect(Vector2f minA, Vector2f maxA, Vector2f minB, Vector2f maxB) {
        if (minA.x > maxB.x || minB.x > maxA.x) //Check if Box A is to the right of Box B and vice-versa
            return false; //If so, they do not overlap
        if (minA.y > maxB.y || minB.y > maxA.y) //Check if Box is above Box B and vice-versa
            return false; //If so, they do not overlap
        return true; //Otherwise, they do overlap
    }

    //Circle & box
    public boolean intersect(Vector2f c, float r, Vector2f min, Vector2f max) {
        float d = 0.0f;
        if (c.x < min.x) d += (c.x - min.x) * (c.x - min.x);
        if (c.x > max.x) d += (c.x - max.x) * (c.x - max.x);
        if (c.y < min.y) d += (c.y - min.y) * (c.y - min.y);
        if (c.y > max.y) d += (c.y - max.y) * (c.y - max.y);
        return d < r * r;
    }



    public boolean pointInCircle(Vector2f p, Vector2f c, float r) {
        Vector2f dist = p.sub(c); //Find distance between point and circle
        return dist.lenSqr() < r * r; //Once again avoid using sqrt
    }

}




