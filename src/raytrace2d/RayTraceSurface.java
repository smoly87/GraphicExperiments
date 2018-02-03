/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace2d;

import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public abstract class RayTraceSurface {
     public abstract IntersectResult calculateIntersection(Ray ray);
     protected float refractKoof ;   

    public float getRefractKoof() {
        return refractKoof;
    }

    public void setRefractKoof(float refractKoof) {
        this.refractKoof = refractKoof;
    }
     
}
