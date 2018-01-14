/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.utils;

import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class VertexAggregator {
    protected int vertexCount;
    protected float[] vertexCoords;
    protected int coordNum ;
    protected int k = 0;
    
    public float[] getVertexCoords() {
        return vertexCoords;
    }
 
    //Если размер превышает, потом проверку сделать
    public VertexAggregator(int vertexCount, int coordNum){
        
        vertexCoords = new float[vertexCount * coordNum ];
        this.coordNum = coordNum;
    }
    
    public void addVertex(Vector3 vertex){
         for(int j = 0; j < coordNum; j++){
                vertexCoords[k] = vertex.values[j];
                k++;
          }
    }
    
    
    public void addVertexes(Vector3[] vertexes){
        for(int i = 0; i < vertexes.length; i++){
            addVertex(vertexes[i]);
        }
    }
}
