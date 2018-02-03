/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.linearAlgebra;

/**
 *
 * @author Andrey
 */
public class Vector3 extends Vector{

    public Vector3(){
        super(3);
    }
    
    public Vector3(float x, float y, float z){
        super();
        values = new float[]{x, y, z};
    }
    
    public Vector3(float[] value) {
        super();
        values = new float[3];
        if(value.length != 3){
            float[] res = new float[3];
            System.arraycopy(value, 0, res, 0, value.length);
            if(value.length < 3){
                for(int i = value.length - 1; i < 3; i++){
                    value[i] = 0;
                }
            }
        } else {
            this.values = value;
        }
        
    }
    
    public Vector4 toVector4(){
        return new Vector4(this.values[0], this.values[1], this.values[2], 1.0f);
    }
    
    public Vector3 plus(Vector3 b){
       
        Vector3 res = new Vector3();
        for(int i = 0; i < 3; i++){
            res.values[i] = this.values[i] + b.values[i];
        }
        return res;
    }
    
    public Vector3 minus(Vector3 b){
       
        Vector3 res = new Vector3();
        for(int i = 0; i < 3; i++){
            res.values[i] = this.values[i] - b.values[i];
        }
        return res;
    }
    
   public Vector3 multiply(float scalar){
       
        Vector3 res = new Vector3();
        for(int i = 0; i < 3; i++){
            res.values[i] = this.values[i] * scalar;
        }
        return res;
    }
    
    public Vector3 normalise(){
        int N = getSize();
        Vector3 res = new Vector3();
        float vecLen = vectorLength();
        for(int i = 0; i < N; i++){
            res.values[i] = this.values[i] / vecLen ;
        }
        return res;
    }
    public  Vector3 cross(Vector b){
        return cross(this, b);
    }
    
   
    
    public static Vector3 cross(Vector a, Vector b){
         Vector3 res = new Vector3();
         res.values[0] = a.values[1] * b.values[2] - b.values[1] * a.values[2];
         res.values[1] = a.values[2] * b.values[0] - b.values[2] * a.values[0];
         res.values[2] = a.values[0] * b.values[1] - b.values[0] * a.values[1];
         return res;
    }
    
   
    
  
    
}
