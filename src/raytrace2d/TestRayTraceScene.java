/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace2d;

import java.util.ArrayList;
import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class TestRayTraceScene extends RayTraceScene{
    protected ArrayList<RaysSource> res ;
    
    public void init(){
        RayTraceSurface sphere = new RayTraceSurfaceSphere(0, 0, 1);
        sphere.setRefractKoof(1.8f);
        this.addSurface(sphere);
        
        double ang = -Math.PI/3;
        this.addRay(new Ray(new Vector3(-1, 1, 0), new Vector3((float)Math.cos(ang), (float)Math.sin(ang), 0)));
        
        res = this.calculate(1);
        
      
        
    }
    
    public RaysSource getResult(){
          RaysSource firstGen = res.get(0);
          return firstGen;
    }
}
