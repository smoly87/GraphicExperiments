/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.raytracing2d;
import opengl.scenes.testscene.*;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import java.util.LinkedList;
import java.util.Map;
import opengl.engine.Scene;
import  javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.SceneCalculations;
import math.sceneCalculations.ViewTransformations;
import opengl.engine.GLSLProgramObject;
import opengl.engine.RenderPass;
import opengl.engine.SceneObject;
import opengl.scenes.objects.CompShaderFiction;
import opengl.scenes.objects.Cube;
import opengl.scenes.objects.Cylinder;
import opengl.scenes.objects.Grid;
import opengl.scenes.objects.Polygon2d;
import opengl.scenes.objects.Quad;
import opengl.scenes.objects.Skybox;
import opengl.scenes.objects.Sphere;
import opengl.scenes.objects.VectorField;
import raytrace2d.Ray;
import raytrace2d.TestRayTraceScene;
/**
 *
 * @author Andrey
 */
public class RayTracing2dScene extends Scene{

    protected int startTime;
    public RayTracing2dScene(GL4 gl) throws LoadResourseException {
        super(gl);
        this.lightPosition = new Vector3(3.0f, 0.0f, 3.0f);
        this.camRotVec = new Vector3();
        this.cameraPosVector = new Vector3(0.0f, 0.0f, 3.0f);
      

        
        optColorMapping = true;
        optShadowMapping = false;
        
        
        
    }

 
     protected void  loadHeadModel(){
           SceneObject africanHead;
           africanHead = new SceneObject(this.gl);
           africanHead.setOptBumpMapping(false);
           africanHead.setBumpMappingTextureFile("african_head_nm_tangent.tga");
           africanHead.setTextureFile("african_head_diffuse.tga");
           africanHead.setModelFile("african_head.obj");
           
           africanHead.init();
           africanHead.bodyTranlate(new Vector3(0.0f, 0.0f,-2.0f));
           sceneObjects.put("head", africanHead);
        
     }
    
     
     protected void createLense(){
         Polygon2d lense  = new Polygon2d(gl);
         
         float pi = (float)Math.PI;
         float h = 0.1f; 
         
         double R = 1.0;
         
         for(float ang=0; ang < 2*pi; ang+=h){
             lense.addVector(new Vector3((float)(R*Math.cos(ang)),  (float)(R*Math.sin(ang)), 0.0f));
         }
         
         
         lense.init();
         sceneObjects.put("Lense", lense);
     }
     
     protected void drawRaysList(VectorField vecField, LinkedList<Ray> raysList, Vector3 color){
         raysList.forEach((ray) -> {
             vecField.addVector(ray.getPosFrom(), ray.getPosTo().minus(ray.getPosFrom()), color);
        });
       
     }

     protected void initVecField(){
         VectorField vecField = new VectorField(gl);
         
         TestRayTraceScene rayTraceCalc = new TestRayTraceScene();
         rayTraceCalc.init();
         rayTraceCalc.getSrcRays();
         //rayTraceCalc.getSrcRays();
         
         drawRaysList(vecField, rayTraceCalc.getSrcRays().getRefractedRays(), new Vector3(0.0f, 1.0f, 0.0f));
         drawRaysList(vecField, rayTraceCalc.getResult().getRefractedRays(), new Vector3(1.0f, 0.0f, 0.0f));
         drawRaysList(vecField, rayTraceCalc.getResult().getReflectedRays(), new Vector3(0.5f, 0.5f, 0.5f));
         drawRaysList(vecField, rayTraceCalc.getResult().getNormalRays(), new Vector3(0.0f, 0.0f, 1.0f));
        
         rayTraceCalc.getResult().getNormalRays().forEach((ray) -> {
             vecField.addVector(ray.getPosFrom(), ray.getPosTo().minus(ray.getPosFrom()).multiply(-1.0f), new Vector3(0.0f, 0.0f, 1.0f));
         });
         vecField.init();
         
         sceneObjects.put("VectorField", vecField);
     }
     
     public void init() throws LoadResourseException{
         this.cameraPosVector = new Vector3(0f, 0.0f, 4.0f);
         super.init();
 createLense();
         this.loadHeadModel();
         initVecField();
        
         
     }
     
    
}