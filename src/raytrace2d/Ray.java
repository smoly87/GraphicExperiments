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
public class Ray {
    protected Vector3 posTo;
    protected Vector3 posFrom;

    public Vector3 getPosFrom() {
        return posFrom;
    }

    public void setPosFrom(Vector3 posFrom) {
        this.posFrom = posFrom;
    }
    protected Vector3 dir;
    protected float t;

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public Vector3 getPosTo() {
        return posTo;
    }

    public Vector3 getDir() {
        return dir;
    }
    
    protected void recalcT(){
         t = (posFrom.values[0] - 0)/dir.values[0];
    }
    
    public Ray(Vector3 posFrom,  Vector3 dir) {
        this.posFrom = posFrom;
        this.dir = dir;
        recalcT();
    }
    
    public Ray(Vector3 posFrom, Vector3 posTo, Vector3 dir) {
        this.posTo = posTo;
        this.posFrom = posFrom;
        this.dir = dir;
        recalcT();
       
    }
}
