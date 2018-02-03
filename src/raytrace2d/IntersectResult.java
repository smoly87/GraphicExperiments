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
public class IntersectResult {
    protected Vector3 pos;
    protected Vector3 normal;
    protected boolean refractToEnv;
    protected float t;

    public float getT() {
        return t;
    }


    public Vector3 getPos() {
        return pos;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public IntersectResult(Vector3 pos, Vector3 normal, float t, boolean refractToEnv) {
        this.pos = pos;
        this.normal = normal;
        this.t = t;
        this.refractToEnv = refractToEnv;
    }
}
