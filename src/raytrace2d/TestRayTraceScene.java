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
        this.addSurface(new RayTraceSurfaceSphere(0, 0, 1));
        
        double ang = -Math.PI/4;
        this.addRay(new Ray(new Vector3(-1, 1, 0), new Vector3((float)Math.cos(ang), (float)Math.sin(ang), 0)));
        
        ArrayList<RaysSource> res = this.calculate(1);
        
      
        
    }
    
    public RaysSource getResult(){
          RaysSource firstGen = res.get(0);
          return firstGen;
    }
}
