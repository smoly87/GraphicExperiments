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
public class RayTraceSurfaceSphere extends RayTraceSurface{


    protected float x0;
    protected float y0; 
    protected float R; 
    
    public RayTraceSurfaceSphere(float x0, float y0, float R) {
        this.x0 = x0;
        this.y0 = y0;
        this.R = R;
    }
    
    
    protected Vector3 calcPosByT(Ray ray, float t){
        Vector3 r = new Vector3(ray.dir.values[0]*t + ray.posFrom.values[0], ray.dir.values[1]*t + ray.posFrom.values[1], 0);
        return r;
    }
    
    protected Vector3 calcNormal(Vector3 pos){
        return new Vector3( pos.values[0] - x0,  pos.values[1] - y0, 0.0f);
    }
    
    @Override
    public IntersectResult calculateIntersection(Ray ray) {
       float x1 =   ray.posFrom.values[0];
       float y1 =   ray.posFrom.values[1];
       float m = ray.dir.values[0];
       float n = ray.dir.values[1];
       
       float c1 = (x1 - x0);
       float c2 = (y1 - y0);
       
       float a = (n*n + m*m);
       float b = 2*(m*c1+n*c2);
       float c = (c1*c1 + c2*c2) - R*R;
       
       float D = b*b-4*a*c;
       
       //What to do if it exactly zero?
       if(D >=0){
           D = (float)Math.sqrt(D);
           float t = (-b - D)/(2*a);
           float t1 = t;
           
           Vector3 intersectPos = calcPosByT(ray, t);
           
           Vector3 dirI = intersectPos.minus(ray.posFrom);
           float dirV = Vector3.dot(dirI,  ray.dir);
           boolean refractToEnv = false;
           if(dirV < 0){
               t = (-b + D)/(2*a);
               intersectPos = calcPosByT(ray, t);
               refractToEnv = true;
               
           }
          /* Vector3 intersectPos1 = calcPosByT(ray, t);
           Vector3 vecPos = calcPosByT(ray, ray.t);
           
           
           System.out.println(intersectPos1);*/
           IntersectResult res =  new IntersectResult(intersectPos, calcNormal(intersectPos), t, refractToEnv);
           return res;
                   
       } else{
           return null;
       }
       
       
    }
    
}
