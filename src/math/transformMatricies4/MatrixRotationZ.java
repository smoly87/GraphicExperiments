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
public class MatrixRotationZ extends Matrix44{

    /**
     * @param a rotation angle in radians
     */
    public MatrixRotationZ(float a){
        super();
        values = new float[][]{
            {cos(a), -sin(a), 0, 0 },
            {sin(a), cos(a), 0, 0 },
            {0, 0, 1, 0 },
            {0, 0, 0, 1 }
        };
    }
}
