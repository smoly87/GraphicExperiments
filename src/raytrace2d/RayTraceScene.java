/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace2d;

import java.util.ArrayList;
import java.util.LinkedList;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class RayTraceScene {
    protected ArrayList<RayTraceSurface> surfaces;
    protected RaysSource srcRays;

    public RaysSource getSrcRays() {
        return srcRays;
    }
    
    
    public RayTraceScene() {
        surfaces = new ArrayList<>();
        srcRays = new RaysSource();
    }
    
    public void addSurface(RayTraceSurface surface){
        surfaces.add(surface);
    }
    
    public void addRay(Ray ray){
        srcRays.getRefractedRays().add(ray);
    }
    
    
    
    protected void calcSurfaceRays(RaysSource resRays, LinkedList<Ray> curRays,  RayTraceSurface surface){
        for(Ray ray:curRays){
           IntersectResult res =  surface.calculateIntersection(ray);
           if(res != null){
               
               ray.posTo = res.getPos();
               
               Vector3 n2 = res.getNormal().normalise().multiply(2);
               Vector3 reflRay = ray.dir.minus(n2);
               
               Ray reflectedRay = new Ray(res.getPos(),  reflRay);
               resRays.getRefractedRays().add(reflectedRay);
           }
        }
    }
    
    public ArrayList<RaysSource> calculate(int iterCount){
        ArrayList<RaysSource> raysGens = new ArrayList<>();
        RaysSource curRays = srcRays;
        
        for(int i=0;i<iterCount;i++){
           RaysSource curRes = new RaysSource();
           for(RayTraceSurface surface: surfaces){
               calcSurfaceRays(curRes, curRays.getRefractedRays(), surface);
               calcSurfaceRays(curRes, curRays.getReflectedRays(), surface);
           }
           curRays = curRes;
           raysGens.add(curRes);
        }
        
        return raysGens;
    }
    
}
