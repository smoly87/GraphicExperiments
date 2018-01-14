/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import engine.mesh.Mesh;
import java.util.ArrayList;
import javax.media.opengl.GL4;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import opengl.engine.SceneObject;
import opengl.scenes.utils.VertexAggregator;

/**
 *
 * @author Andrey
 */
public class Grid extends SceneObject{ 
   


    
    protected VertexAggregator vertexAggregator;
    protected VertexAggregator vertexNormalsAggregator;
    protected VertexAggregator vertexTextureAggregator;
    protected int N = 20;
    public Grid(GL4 gl, int N) {
        super(gl);
        this.optVertexNormals = true;
        this.optLoadModelFile = false;
        this.vertexCoordsNum = 3;
        this.N = N;
        
        
    }
    
    @Override
    protected void createMesh(){
        mesh = new Mesh();
        
             
       
        ArrayList<Vector3> textureCoordsList = new ArrayList<Vector3>();
        ArrayList<Vector3> vertexCoordsList = new ArrayList<Vector3>();
        
      
        float h = 1.0f / (float) N;
       
        float v = 0;
       
        /*
        Сама сетка N*N
        В квадрате 2 треугольника по 3 вершины, у каждой вершины по 3 координаты*/
        int vertexCount = N * N * 3 * 2;

       
        vertexAggregator = new VertexAggregator(vertexCount, 3);
        vertexNormalsAggregator = new VertexAggregator(vertexCount, 3);
        vertexTextureAggregator = new VertexAggregator(vertexCount, 2);
        
        
        for(int i = 0; i < N;i++){
           float u = 0;
           for(int j = 0; j < N;j++){  
              VertexInfo[] quadVertexes =  baseQuad(u, v, h, h);
              for(int k = 0; k < quadVertexes.length; k++){
                  vertexAggregator.addVertex(quadVertexes[k].getVertexCoords());
                  vertexTextureAggregator.addVertex(quadVertexes[k].getTextureCoords());
              } 

              //Квадрат(ячейка сетки) состоит из двух треугольников - нумерация вершин первый - 0,1,2, второй - 3,4,5
   
              Vector3[] tr1Normals = triangleNormalCoords(quadVertexes[0].getVertexCoords(), quadVertexes[1].getVertexCoords(), quadVertexes[2].getVertexCoords());
              Vector3[] tr2Normals = triangleNormalCoords(quadVertexes[3].getVertexCoords(), quadVertexes[4].getVertexCoords(), quadVertexes[5].getVertexCoords());
            
              vertexNormalsAggregator.addVertexes(tr1Normals);
              vertexNormalsAggregator.addVertexes(tr2Normals);
              
           
              
              u += h; 
           }
           v += h; 
        }
         mesh.setVertexesCount(vertexCount);
        mesh.setVertexesCoords(vertexAggregator.getVertexCoords());
        mesh.setTextureCoords(vertexTextureAggregator.getVertexCoords());
        mesh.setNormalsCoords(vertexNormalsAggregator.getVertexCoords());
       
    }
   
    
    protected float f(float u, float v){
        return 0;
    }
    
    //Функция отображения из параметров uv в абсолютную систему координат
    protected VertexInfo surfacePointToXYZ(float u, float v){
       VertexInfo res = new VertexInfo();
       res.setVertexCoords(new Vector3(u, v, 0f));
       res.setTextureCoords(new Vector3(u, v, 0f));
       return res;
    }
    
    
    
    protected  VertexInfo[] baseQuad(float u, float v, float hu, float hv){
        
        return  new VertexInfo[]{
            surfacePointToXYZ(u, v),
            surfacePointToXYZ(u + hu, v ),
            surfacePointToXYZ(u + hu, v + hv),
           
            surfacePointToXYZ(u + hu, v + hv),
            surfacePointToXYZ(u, v + hv),
            surfacePointToXYZ(u , v )
            
        };
    }
    
    protected  Vector3[] triangleTextureCoords(Vector3 p0, Vector3 p1, Vector3 p2){
        return  new Vector3[]{
           p0, p1, p2
        };
        
    }
    
    protected  Vector3[] triangleNormalCoords(Vector3 p0, Vector3 p1, Vector3 p2){
        Vector3 n0 = (p1.minus(p0)).cross( p2.minus(p0));
        Vector3 n1 = (p0.minus(p1)).cross( p2.minus(p1));
        Vector3 n2 = (p0.minus(p2)).cross( p1.minus(p2));
        
        return  new Vector3[]{
           n0.normalise(), n0.normalise(), n0.normalise()
        };
        
    }
    
   
    
}
