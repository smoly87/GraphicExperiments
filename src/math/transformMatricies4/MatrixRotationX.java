/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.transformMatricies4;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import static utils.MathWrapper.*;
/**
 *
 * @author Andrey
 */
public class MatrixRotationX extends Matrix44{

    /**
     * @param a rotation angle in radians
     */
    public MatrixRotationX(float a){
        super();
        values = new float[][]{
            {1, 0, 0, 0 },
            {0, cos(a), -sin(a), 0 },         
            {0, sin(a), cos(a), 0 },
            {0, 0, 0, 1 }
        };
    }
}
