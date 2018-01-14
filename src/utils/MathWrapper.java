/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;
import  java.lang.Math;
/**
 *
 * @author Andrey
 */
public class MathWrapper {
    public static float cos(float alpha){
        return (float)Math.cos(alpha);
    }
    
    public static float sin(float alpha){
        return (float)Math.sin(alpha);
    }
    
    public static float tan(float alpha){
        return (float)Math.tan(alpha);
    }
    
    
    public static float arccos(float cosVal){
        return (float)Math.acos(cosVal);
    }
}
