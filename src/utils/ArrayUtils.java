/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.util.LinkedList;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class ArrayUtils {
    public static float[] linkedListToFloatArray(LinkedList<Float> list){
         float[] ret = new float[list.size()];
         int i = 0;
         for (float e : list)  {
             ret[i++] = e;
         }
         return ret;
    }
    
    public static int[] linkedListToIntArray(LinkedList<Integer> list){
         int[] ret = new int[list.size()];
         int i = 0;
         for (int e : list)  {
             ret[i++] = e;
         }
         return ret;
    }
    
    public static float[] vectorListToArray(Vector[] vectorList){
         float[] ret = new float[vectorList.length * vectorList[0].values.length];
         int k = 0;
         for (int i = 0; i < vectorList.length; i++)  {
             float[] values = vectorList[i].values;
             for(int j = 0; j < values.length; j++){
                 ret[k] = vectorList[i].values[j];
                 k++;
             }
             
         }
         return ret;
    }  
    
    public static float[] vectorListToArray(LinkedList<Vector> vectorsList){
        int vecSize = vectorsList.peek().getSize();
        float[] res = new float[vectorsList.size() * vecSize];
        int offset = 0;
        
        for(Vector vec: vectorsList){
            for(int i = 0; i < vecSize; i++){
                res[offset + i] = vec.values[i];
            }
            offset += vecSize;
        }
        
        return res;
    }
    
    public static float[] vectorListToArray(LinkedList<Vector> vectorsList, Vector startPointVector){
        int vecSize = vectorsList.peek().getSize();
        float[] res = new float[vectorsList.size() * vecSize * 2];
        int offset = 0;
        
        for(Vector vec: vectorsList){
            for(int i = 0; i < vecSize; i++){
                res[offset + i] = startPointVector.values[i];
            }
            
            for(int i = 0; i < vecSize; i++){
                res[offset + vecSize + i] = vec.values[i];
            }
            offset += 2 * vecSize;
        }
        
        return res;
    }  
}
