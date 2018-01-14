/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.linearAlgebra;

import math.sceneCalculations.MatrixChainOperations;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixRotationZ;

/**
 *
 * @author Andrey
 */
public class Vector {
    public float[] values;
    
    public Vector(){
    }
    
    public Vector(int[] value){
        this.values = new float[value.length];
        for(int i = 0; i < value.length; i++){
            this.values[i] = value[i];
        }
    }
    public Vector(float[] value){
        this.values = value;
    }
    
    public Vector(float[] value, int realSize){
        this.values = values;
    }
    
    public Vector(int size){
        this.values = new float[size];
    }
    
    /**
     * @return Dimension(size) of vector
    */
    public int getSize(){
        return values.length;
    }
    
    /**
     * @return the values
     */
    public float[] getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(float[] values) {
        this.values = values;
    }
     public Vector multiply(float k){
        int N = getSize();
        Vector res = new Vector(N);
        for(int i = 0; i < N; i++){
            res.values[i] = values[i] * k  ;
        }
        return res;
    }
      
    public Vector normalise(){
        int N = getSize();
        Vector res = new Vector(N);
        float vecLen = vectorLength();
        for(int i = 0; i < N; i++){
            res.values[i] = this.values[i] / vecLen ;
        }
        return res;
    }
    
    public float vectorLength(){
        return (float)Math.sqrt(dot(this, this)) ;
    }
    
    public Vector plus(Vector b){
        int N = Math.min(this.getSize(), b.getSize()) ;
        Vector res = new Vector(N);
        for(int i = 0; i < N; i++){
            res.values[i] = this.values[i] + b.values[i];
        }
        return res;
    }
    
    public  Vector chageSign(){
        Vector res = new Vector(this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            res.values[i] = -this.values[i] ;
        }
        return res;
    }
    
    public  Vector minus( Vector b){
        int N = Math.min(this.getSize(), b.getSize()) ;
        Vector res = new Vector(N);
        for(int i = 0; i < N; i++){
            res.values[i] = this.values[i] - b.values[i];
        }
        return res;
    }
    
    public static float dot(Vector a, Vector b){
        int N = Math.min(a.getSize(), b.getSize()) ;
        float sum = 0;
        for(int i = 0; i < N; i++){
            sum += a.values[i] * b.values[i];
        }
        return sum;
    }
    
    public static Matrix getFullRotationMatrix(Vector vecAngles){
        MatrixChainOperations mChain = new MatrixChainOperations();
        mChain.add(new MatrixRotationX(vecAngles.values[0]));
        mChain.add(new MatrixRotationY(vecAngles.values[1]));
        mChain.add(new MatrixRotationZ(vecAngles.values[2]));
        return mChain.getResult();
    }
    
    public static Vector rotate(Vector vecAngles){
        Matrix R = getFullRotationMatrix(vecAngles);
        return R.multiply(vecAngles);
    }

    @Override
    public String toString() {
        int N = this.getSize();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i = 0; i < N; i++){
            sb.append(Float.toString(values[i]));
            if(i < N - 1) sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }
    
    public Vector3 toVector3(){
        Vector3 res;
        int N = this.getSize();
        if(N == 3) {
            res = new Vector3(values);
        }
        else{
            res = new Vector3();
            int NM = Math.min(N, 3);
            for(int i = 0; i < NM; i++){
                res.values[i] = this.values[i];
            }
        }
        return res;
    }
}
