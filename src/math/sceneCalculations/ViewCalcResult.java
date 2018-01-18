/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math.sceneCalculations;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class ViewCalcResult {
    protected Matrix viewMatrix;
    protected Matrix translationMatrix;

    public Matrix getTranslationMatrix() {
        return translationMatrix;
    }
    protected Vector3 posVectorCurBasis;

    public Matrix getViewMatrix() {
        return viewMatrix;
    }

    public Vector3 getPosVectorCurBasis() {
        return posVectorCurBasis;
    }

    public ViewCalcResult(Matrix viewMatrix, Vector3 posVectorCurBasis, Matrix translationMatrix) {
        this.viewMatrix = viewMatrix;
        this.translationMatrix =  translationMatrix;
        this.posVectorCurBasis = posVectorCurBasis;
    }
}
