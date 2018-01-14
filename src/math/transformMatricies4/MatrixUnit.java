/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.transformMatricies4;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;

/**
 *
 * @author Andrey
 */
public class MatrixUnit extends Matrix44{
   public MatrixUnit(){
       super();
       for(int i = 0; i < 4; i++ ){
           values[i][i] = 1;
       }
   }
}
