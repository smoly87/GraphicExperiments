/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace2d;

import java.util.LinkedList;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class RaysSource {
    protected LinkedList<Ray> reflectedRays;
    protected LinkedList<Ray> refractedRays;
    protected LinkedList<Ray> normalRays;

    public LinkedList<Ray> getNormalRays() {
        return normalRays;
    }

    public void setNormalRays(LinkedList<Ray> normalRays) {
        this.normalRays = normalRays;
    }
    
    public RaysSource() {
        refractedRays = new LinkedList<>();
        reflectedRays = new LinkedList<>();
        normalRays = new LinkedList<>();
    }

    public LinkedList<Ray> getReflectedRays() {
        return reflectedRays;
    }

    public void setReflectedRays(LinkedList<Ray> reflectedRays) {
        this.reflectedRays = reflectedRays;
    }

    public LinkedList<Ray> getRefractedRays() {
        return refractedRays;
    }

    public void setRefractedRays(LinkedList<Ray> refractedRays) {
        this.refractedRays = refractedRays;
    }
    

 
    
}
