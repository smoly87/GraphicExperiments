/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.transformMatricies4;

import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector;
import sun.management.counter.Units;

/**
 *
 * @author Andrey
 */
public class MatrixTranslation extends MatrixUnit{
    public MatrixTranslation(Vector vecMove){
        super();
        
        changeMoveVector(vecMove);
    }
    
    public void changeMoveVector(Vector vecMove){
        for(int i = 0; i < 3; i++){
            //Последний столбец отвечает за сдвиг в пространстве
            values[i][3] = vecMove.values[i];
        }
    }
    
    /**
     * Inverse Matrix
     * @return 
     */
    @Override
    public MatrixTranslation inverse(){
        Vector T = this.getColumn(3);
        T = T.chageSign();
        T.values[3] = 1.0f;
        MatrixTranslation M = new MatrixTranslation(T);
        return M;
    }
}
