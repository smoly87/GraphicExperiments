/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.sceneCalculations;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix33;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixTranslation;
import math.transformMatricies4.MatrixUnit;
import utils.MathWrapper;
import static utils.MathWrapper.*;

/**
 *
 * @author Andrey
 */
public class SceneCalculations {
    
    /**
     * Calculate projection matrix
     * @param screenWidth
     * @param screenHeight
     * @param fieldOfView Угол обзора в градусах
     * @param Zfar Максимальная координата по Z
     * @param Znear Минимальная координата по z
     * @return Проекционная матрица
     */
    public static Matrix calcProjMatrix(int screenWidth, int screenHeight, float fieldOfView, float Zfar, float Znear){
              Matrix projectionMatrix = new Matrix();
        float acepectRatio = (float)screenWidth / (float)screenHeight;
        float tanHalfFov = tan( (float) Math.toRadians((float)fieldOfView / 2) );
        

        float Zm =   Zfar - Znear ;
        float Zp =   Znear + Zfar ;
        
        projectionMatrix = new MatrixUnit();
        projectionMatrix.values[0][0] = (1.0f / (tanHalfFov)) / acepectRatio;
        projectionMatrix.values[1][1] = 1.0f / (tanHalfFov);
        
        projectionMatrix.values[2][2] = (- Zp/Zm)  ;
        projectionMatrix.values[2][3] = -1.0f ;
        
        projectionMatrix.values[3][2] = -2.0f * (Zfar * Znear) / Zm ;
        
       
        projectionMatrix.values[3][3] = 0.0f ;

        return projectionMatrix;
    }
   
    public static Matrix lookAt(Vector3 eye, Vector3 center, Vector3 up){
        if(up == null) up = new Vector3(0, 1, 0);
        //Поворот базиса камеры
        Vector z = eye.minus(center );
         z =       z.normalise();
        Vector x = Vector3.cross(up, z).normalise();
        Vector y = Vector3.cross(z, x).normalise();

       
        Matrix M = new MatrixUnit();
        
        M.setColumn(x, 0);
        M.setColumn(y, 1);
        M.setColumn(z, 2);
        
       
        
        return  M;
         
    }
    
    /**
     * 
     * @return Angles between old and new basis axis by pairs.(x, x')m (y,y'), (z,z')
     */
    public static Vector3 basisAngles(Matrix newBasis, Matrix oldBasis){
        Matrix R = newBasis.transpose().multiply(oldBasis);
        Vector3 res = new Vector3();
        
        for(int i=0;i<3;i++){
            res.values[i] = arccos(R.values[i][i])/(float)Math.PI;
        }
        
        return res;
    }
    
    /**
     * 
     * @param camView
     * @param camPosVector This parameter is in old basis. It will be recounted to a new basis.
     * @return 
     */
    public static ViewCalcResult calcViewMatrix(Matrix camView, Vector3 camPosVector){
        Matrix camTrans = new MatrixUnit();
        Vector3 camPosNewBasis = camView.transpose().multiply(camPosVector.chageSign().toVector3(), 0);
        camTrans.setColumn(camPosVector.chageSign(), 3);
        camView = camView.transpose().multiply(camTrans);
        
        return new ViewCalcResult(camView, camPosNewBasis.chageSign().toVector3(), camTrans) ;
    }
    
   /* public Vector3 basisAngles(){
    }
    
   /* public static Vector getLookAtVectorByEulerAngles(Vector camRotVec){
        
    }*/
    
    public static Vector getViewDirByAngles(float yaw, float pitch){
        Vector r = new Vector3();
        r.values[1] = cos(pitch); //Ось y
        r.values[0] = sin(pitch) * cos(yaw); //Ось x
        r.values[2] = sin(pitch) * sin(yaw); //Ось z
        
        return r;
    }
    
   
    public static Matrix calcCameraMatrix(Vector cameraPosVector, Vector camRotVec){
        Matrix M = new Matrix44();
       
        
        Matrix MR = new Matrix();
        //Vector camPosVecD = cameraPosVector.minus(viewDir);
        Matrix44 matRotY = new MatrixRotationY(camRotVec.values[1]);
        Matrix44 matRotX = new MatrixRotationX(camRotVec.values[0]);
       // M = matRotY.multiply(matRotX).inverse();
        Matrix MT = new MatrixTranslation(cameraPosVector);
        
        M = MT;
        //M.setColumn(cameraPosVector.multiply(-1.0f), 3);
              //
      
        /*System.out.println("viewDir:" + viewDir);
        System.out.println("CameraPos:" + cameraPosVector);
        System.out.println("camRotVec:" + camRotVec);*/
     
        
        return M;
    }

    /**
     * 
     * @param w Screen width
     * @param h Screen height
     * @return 
     */
    public static Matrix calcViewPortMatrix(int w, int h){
        Matrix M = new MatrixUnit();
        M.values[0][0] = w/2;
        M.values[0][3] = w/2;
        
        M.values[1][1] =h/2;
        M.values[1][3] = h/2;
        
        
        //For z-bufffer. Resolution of z-buffer
        int d = 1;
        M.values[2][2] = d/2;
        M.values[2][3] = d/2;
        return M;
    }
}
