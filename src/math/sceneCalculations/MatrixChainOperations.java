/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.sceneCalculations;

import java.util.LinkedList;
import math.linearAlgebra.Matrix;

/**
 *
 * @author Andrey
 */
public class MatrixChainOperations {
    protected LinkedList<Matrix> operations;
    public MatrixChainOperations() {
        operations = new LinkedList<>();
    }
        
    public void add(Matrix operator){
        operations.add(operator);
    }
    /**
     * Result of multiplying all matricies
     */
    public Matrix getResult(){
        Matrix A = operations.poll();
        while (operations.size() > 0) {
            Matrix B = operations.poll();
            A = A.multiply(B);          
        }
        return A;
    }
    
}
