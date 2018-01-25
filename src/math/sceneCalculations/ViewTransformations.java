/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math.sceneCalculations;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector3;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixTranslation;
import math.transformMatricies4.MatrixUnit;

/**
 *
 * @author Andrey
 */
public class ViewTransformations {
    protected Matrix viewMatrix;
    protected MatrixTranslation translationMatrix;
    protected Vector3 posVectorCurBasis;
    
    public Matrix getTranslationMatrix() {
        return translationMatrix;
    }
    
    public Matrix getInvTranslationMatrix() {
        return translationMatrix.inverse();
    }
    

    /**
     * This method 
     * cancels translation 
     * then apply transformation, 
     * next step is recalculate posBector to new basis
     * and after all this move basis to 
     * 
     * @param U 
     */
    public Matrix applyTransform(Matrix U){
        /*Matrix Tinv = getTranslationMatrix();
        viewMatrix = Tinv.multiply(viewMatrix);*/
        Matrix UInv =  U.inverse();
        viewMatrix = UInv.multiply(viewMatrix);
        posVectorCurBasis = UInv.multiply(posVectorCurBasis.toVector4()).toVector3();
        this.translationMatrix =  new MatrixTranslation(posVectorCurBasis);
        //viewMatrix = viewMatrix.multiply(U.transpose());
        Matrix res = this.getViewMatrix();
        return res;
    }
    
    /**
     * This method is used to apply relative movement 
     * @return 
     */
    public Matrix applyTranslation(Vector3 deltaVec){
        posVectorCurBasis = posVectorCurBasis.plus(deltaVec);
        this.translationMatrix =  new MatrixTranslation(posVectorCurBasis);
        return this.getViewMatrix();
    }
    
    public Matrix applyIncRotationAroundAbsY(float yAngle){
        /*Matrix Tinv = getTranslationMatrix();
        viewMatrix = viewMatrix.multiply(Tinv);*/
         
        Vector3 basisAngles = SceneCalculations.basisAngles(viewMatrix.transpose(), new MatrixUnit());
        float axisAngle = basisAngles.values[1];
       
        
        Matrix MCancelXRot = new MatrixRotationX(axisAngle);
        viewMatrix = MCancelXRot.multiply(viewMatrix);
        
            basisAngles = SceneCalculations.basisAngles(viewMatrix.transpose(), new MatrixUnit());
         float axisAngleN = basisAngles.values[1];
        
        
        Matrix YRotMatr = new MatrixRotationY(yAngle).inverse();
        viewMatrix =  YRotMatr.multiply(viewMatrix);
        
        //Return X rotation
        Matrix XRot = new MatrixRotationX(axisAngle).inverse();
        viewMatrix = XRot.multiply(viewMatrix);
 
        //System.out.println("Was" + posVectorCurBasis.toString());
        posVectorCurBasis = YRotMatr.multiply(posVectorCurBasis.toVector4()).toVector3();
        //System.out.println(posVectorCurBasis.toString());
        
        this.translationMatrix =  new MatrixTranslation(posVectorCurBasis);
        return viewMatrix;
    }
    
    public Matrix getViewMatrix() {
        Matrix U = getInvTranslationMatrix().multiply(viewMatrix);
        return U;
    }

    public Vector3 getPosVectorCurBasis() {
        return posVectorCurBasis;
    }

    public void setPosVectorByAbsBasis(){
        
    }
    
    public ViewTransformations(Matrix viewMatrix, Vector3 posVectorAbsBasis) {
        this.viewMatrix = viewMatrix.transpose();
        posVectorCurBasis = this.viewMatrix
                                      .multiply(posVectorAbsBasis.toVector4()).toVector3();
        
        this.translationMatrix =  new MatrixTranslation(posVectorCurBasis);
        this.posVectorCurBasis = posVectorCurBasis;
    }
}
