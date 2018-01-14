/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import engine.mesh.Mesh;
import javax.media.opengl.GL4;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector3;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixRotationZ;
import math.transformMatricies4.MatrixTranslation;
import opengl.engine.SceneObject;
import opengl.scenes.utils.VertexAggregator;

/**
 *
 * @author Andrey
 */
public class Cube extends SceneObject{
     public Cube(GL4 gl) {
        super(gl);
        this.optVertexNormals = true;
        this.optLoadModelFile = false;
        this.vertexCoordsNum = 3;
        
        
    }

    protected Vector3[]  transformVertexes(Matrix M, Vector3[] vertexes){
        Vector3[] res = new Vector3[vertexes.length];
        for(int i = 0; i < vertexes.length; i++){
            res[i] = M.multiply(vertexes[i]);
        }
        return res;
    }
     
    @Override
    protected void createMesh(){
       MatrixRotationX Rx = new MatrixRotationX((float)Math.PI / 2);
       MatrixRotationY Ry = new MatrixRotationY((float)Math.PI / 2);
       Matrix Ry2 = Ry.multiply(Ry); // Rotate on Pi
       
       MatrixRotationX RxNeg = new MatrixRotationX(-(float)Math.PI / 2);
       MatrixRotationY RyNeg  = new MatrixRotationY(-(float)Math.PI / 2);
       
       MatrixTranslation Tr = new MatrixTranslation(new Vector3(0.0f, 0, 1.0f)); 
       mesh = new Mesh();
        
       mesh.setVertexesCount(6 * 6);
        //mesh.setIndexesCount(3);
       
       //6 граней по 2 треугольника = 6 вершин/грань
       VertexAggregator vertexAggregator = new VertexAggregator(6 * 6, 3);
       
       Vector3[] frontFace = new Vector3[]{
            new Vector3( -1.0f, -1.0f, 0.0f),
            new Vector3( 1.0f, 1.0f, 0.0f),
            new Vector3( -1.0f, 1.0f, 0.0f),
            
            new Vector3( -1.0f, -1.0f, 0.0f),
            new Vector3( 1.0f, -1.0f, 0.0f),
            new Vector3(1.0f, 1.0f, 0.0f) 
       };
       
       
       VertexAggregator textureCoordsAggregator = new VertexAggregator(6 * 6, 3);
       Vector3[] textureCoords = new Vector3[]{
            new Vector3( 0.0f, 0.0f, 0.0f),
            new Vector3( 1.0f, 1.0f, 0.0f),
            new Vector3( 0.0f, 1.0f, 0.0f),
            
            new Vector3( 0.0f, 0.0f, 0.0f),
            new Vector3( 1.0f, 0.0f, 0.0f),
            
            new Vector3( 1.0f, 1.0f, 0.0f),
            
       };
       
       //Изначально грань в 0, так удобнее для дальнейших преобразований
       Vector3[] frontFaceF = transformVertexes(Tr, frontFace);
       vertexAggregator.addVertexes(frontFaceF);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr,textureCoords));
               
       //Backface
       Tr.changeMoveVector(new Vector3(0.0f, 0, -1.0f));
       Vector3[] backFace = transformVertexes(Tr.multiply(Ry2), frontFace);
       vertexAggregator.addVertexes(backFace);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr.multiply(Ry2),textureCoords));
       
       
       Tr.changeMoveVector(new Vector3(1.0f, 0, 0));
       Vector3[] rightFace =  transformVertexes(Tr.multiply(Ry), frontFace);
       vertexAggregator.addVertexes(rightFace);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr.multiply(Ry),textureCoords));
     
       
       
       Tr.changeMoveVector(new Vector3(-1.0f, 0, 0));
       Vector3[] leftFace =  transformVertexes(Tr.multiply(RyNeg), frontFace);
       vertexAggregator.addVertexes(leftFace);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr.multiply(RyNeg),textureCoords));
     
       
       
       Tr.changeMoveVector(new Vector3(0.0f, 1.0f, 0));
       Vector3[] topFace =  transformVertexes(Tr.multiply(RxNeg), frontFace);
       vertexAggregator.addVertexes(topFace);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr.multiply(RxNeg),textureCoords));
       
       
       Tr.changeMoveVector(new Vector3(0.0f, -1.0f, 0));
       Vector3[] bottomFace =  transformVertexes(Tr.multiply(Rx), frontFace);
       vertexAggregator.addVertexes(bottomFace);
       textureCoordsAggregator.addVertexes(transformVertexes(Tr.multiply(Rx),textureCoords));
       
       mesh.setVertexesCoords(vertexAggregator.getVertexCoords());
       
       
       //Текстурные координаты одинаковы для всех граней
      /* for(int i = 0; i < 6; i++){
           textureCoordsAggregator.addVertexes(textureCoords);
       }*/
      
       
       
      
       mesh.setTextureCoords(textureCoordsAggregator.getVertexCoords());
      
      
    }
}
