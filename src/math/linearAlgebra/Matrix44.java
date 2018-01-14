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

    
}
