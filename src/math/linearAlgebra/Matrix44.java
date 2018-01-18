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
public class Matrix44 extends Matrix{

    public Matrix44(){
        super(4, 4);
    }
    
    public Matrix44(Vector vector, boolean isColumn) {
        super(vector, isColumn);
    }

    public Matrix44(float[][] values) {
        super(values);
    }

    public Matrix33 getMatrix33(){
        
        return new Matrix33(this.getRange(0, 0, 2, 2));
    }
     public Vector3 multiply(Vector3 p){
       return  multiply(p.toVector4()).toVector3();
    }
}
