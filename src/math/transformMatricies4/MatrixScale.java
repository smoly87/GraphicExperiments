/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.transformMatricies4;

import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class MatrixScale extends MatrixUnit{
     public MatrixScale(Vector3 vecMove){
        super();
        //TODO:? Если 0 значения ?
        for(int i = 0; i < 3; i++){
            //Диагональные элементы - это коэф. масштабирования соотв. координаты 
            values[i][i] = vecMove.values[i];
        }
    }
}
