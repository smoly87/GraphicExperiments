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
    protected float refractKoofEnv = 1.0f;

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
    
    
    protected Ray calcReflectedRay( IntersectResult res, Ray ray){
        float nKoof = Vector3.dot(ray.dir, res.getNormal());
        Vector3 reflRayDir = ray.dir.minus(res.getNormal().multiply(2 * nKoof));

        Ray reflectedRay = new Ray(res.getPos(), reflRayDir);
        if(res.refractToEnv){
            ray.setRefractKoof(refractKoofEnv); 
        } else{
            ray.setRefractKoof(refractKoofEnv);
        }
        return reflectedRay;
    }
    
    
     protected Ray calcRefractedRay( IntersectResult res, Ray ray, RayTraceSurface surface){
        /*float nKoof = Vector3.dot(ray.dir, res.getNormal());
        Vector3 reflRayDir = ray.dir.minus(res.getNormal().multiply(2 * nKoof));*/
        Vector3 n = res.getNormal();
        Vector3 I = ray.getDir();
        float dKoof = Vector3.dot(I, n);
        Vector3 d = n.multiply(dKoof)
                     .minus(I);
        
        float n1;
        float n2;
        
        if(!res.refractToEnv){
            n1 = refractKoofEnv;
            n2 = surface.getRefractKoof();
        } else{
            n2 = refractKoofEnv;
            n1 = surface.getRefractKoof();
        }
        
        Vector3 dR = d.multiply(n1/n2);
        Vector3 refractedRayDir = n.multiply(dKoof).minus(dR);

        Ray reflectedRay = new Ray(res.getPos(), refractedRayDir);
        
        if(res.refractToEnv){
            ray.setRefractKoof(refractKoofEnv); 
        } else{
            ray.setRefractKoof(ray.getRefractKoof());
        }
        
        return reflectedRay;
    }
    
    protected void calcSurfaceRays(RaysSource resRays, LinkedList<Ray> curRays,  RayTraceSurface surface, boolean calcRefl){
        for(Ray ray:curRays){
           IntersectResult intersecRes =  surface.calculateIntersection(ray);
           if(intersecRes != null){
               resRays.getNormalRays().add(new Ray(intersecRes.getPos(), intersecRes.getNormal()));
               ray.posTo = intersecRes.getPos();
               
               if(calcRefl) resRays.getReflectedRays().add(calcReflectedRay(intersecRes, ray));
               resRays.getRefractedRays().add(calcRefractedRay(intersecRes, ray, surface));
           }
        }
    }
    
    public ArrayList<RaysSource> calculate(int iterCount){
        ArrayList<RaysSource> raysGens = new ArrayList<>();
        RaysSource curRays = srcRays;
        
        for(int i=0;i<iterCount;i++){
           RaysSource curRes = new RaysSource();
           for(RayTraceSurface surface: surfaces){
               calcSurfaceRays(curRes, curRays.getRefractedRays(), surface, false);
            //   calcSurfaceRays(curRes, curRays.getReflectedRays(), surface, true);
           }
           curRays = curRes;
           raysGens.add(curRes);
        }
        
        return raysGens;
    }
    
}
