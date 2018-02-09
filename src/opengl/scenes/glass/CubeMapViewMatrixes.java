/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.glass;

import javax.media.opengl.GL;
import math.linearAlgebra.Matrix;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixRotationZ;

/**
 *
 * @author Andrey
 */
public class CubeMapViewMatrixes {
    
    public  Matrix getViewForFace(int faceId){
        float  PI = (float)Math.PI;
        
        Matrix M = null;
        
        switch (faceId){
            case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X:
                  M = new MatrixRotationY(PI / 2);
                break;   
               
            case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X:
               M = new MatrixRotationY(-PI / 2);
                break;
            case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y:
                M = new MatrixRotationX(PI / 2);
                break;
            case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y:
                M = new MatrixRotationX(-PI / 2);
                break;    
            case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z:
               M = new MatrixRotationX(0.0f );
                break; 
            case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z:
                M = new MatrixRotationY(PI);
                break;     
        }
        
        return M;
    }
}
