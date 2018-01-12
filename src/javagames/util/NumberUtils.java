package javagames.util;


import java.util.Random;

public class NumberUtils {

    Random rand;
    public NumberUtils(){
        rand = new Random();
    }

    //returns random integer from min to max
    public int randomInt(int min, int max){

        return rand.nextInt(max)+min;
    }

    //returns random float from min to max
    public float randomFloat(float min, float max){

        return rand.nextFloat() * (max - min) + min;
    }

    //clamps integer
    public int clampInt(int min, int max, int val){
        if(val > max){
            return max;
        }
        else if(val < min){
            return min;
        }
        else{
            return val;
        }
    }

    //clamps float
    public float clampFloat(float min, float max, float val){
        if(val > max){
            return max;
        }
        else if(val < min){
            return min;
        }
        else{
            return val;
        }
    }
}
