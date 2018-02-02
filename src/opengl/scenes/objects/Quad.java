/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import engine.mesh.Mesh;
import javax.media.opengl.GL4;
import opengl.engine.SceneObject;

/**
 *
 * @author Andrey
 */
public class Quad extends SceneObject{
     public Quad(GL4 gl) {
        super(gl);
        this.optVertexNormals = false;
        this.optVertexNormals = false;
        this.optLoadModelFile = false;
        this.vertexCoordsNum = 3;
        
        
    }

    @Override
    protected void createMesh(){
        mesh = new Mesh();
        mesh.setVertexCoordsNum(vertexCoordsNum);
       mesh.setVertexesCount(6);
        //mesh.setIndexesCount(3);
        
        mesh.setVertexesCoords(new float[]{
            -0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
             -0.5f, 0.5f, 0.0f,
             
             -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
              0.5f, 0.5f, 0.0f,
            
        });
        
        mesh.setTextureCoords(new float[]{
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
             
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
           
            
          
            
        });
    }
}
