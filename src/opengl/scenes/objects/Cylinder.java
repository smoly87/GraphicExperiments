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
public class Cylinder extends Grid{
    public Cylinder(GL4 gl) {
        super(gl, 20);      
    }
    
   /* protected Vector3 surfacePointToXYZ(float u, float v){
       //return new Vector3(u, v, f(u, v));
      return new Vector3((float)Math.cos(Math.PI * 2 * v), (float)Math.sin(Math.PI * 2 * v), u);
    }*/
}
