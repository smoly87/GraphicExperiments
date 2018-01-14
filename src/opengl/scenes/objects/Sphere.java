/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import javax.media.opengl.GL4;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class Sphere extends Grid{
    public Sphere(GL4 gl) {
        super(gl, 20);      
    }
    
    protected VertexInfo surfacePointToXYZ(float u, float v){
        VertexInfo res = new VertexInfo();
        
        float sinTeta = (float)Math.sin(2*Math.PI * (0.5- v));
        float phi = (float)Math.PI * 2 * u;
        float z = (float)Math.cos(2*Math.PI * (0.5-v));
        
        res.setVertexCoords(
                new Vector3(
                        sinTeta * (float)Math.cos(phi),  
                        sinTeta *(float)Math.sin(phi), 
                        z
                )
        );
        
        res.setTextureCoords(new Vector3(u, (z + 1) /2, 0));
        
        return res;
    }
  
}
