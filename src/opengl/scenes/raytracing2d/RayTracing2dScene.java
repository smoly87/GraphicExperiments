/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.raytracing2d;
import engine.Vector;
import opengl.scenes.testscene.*;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import engine.utils.common.MathUtils;

import java.util.ArrayList;
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
import opengl.scenes.objects.GraphAnimated2d;
import opengl.scenes.objects.GraphAnimated3d;
import opengl.scenes.objects.Grid;
import opengl.scenes.objects.Polygon2d;
import opengl.scenes.objects.Quad;
import opengl.scenes.objects.Skybox;
import opengl.scenes.objects.Sphere;
import opengl.scenes.objects.VectorField;
import raytrace2d.Ray;
import raytrace2d.RaysSource;
import raytrace2d.TestRayTraceScene;
import tasks.membrane.ReferenceWave2dSolution;
import tasks.membrane.waveEquation2d;
import tasks.waveequation.waveEquation1d;
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
         
         double R = 0.5;
         
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

     protected void drawRaysGen( VectorField vecField, RaysSource rayCalsRes){
         
         drawRaysList(vecField, rayCalsRes.getRefractedRays(), new Vector3(1.0f, 0.0f, 0.0f));
         drawRaysList(vecField, rayCalsRes.getReflectedRays(), new Vector3(0.5f, 0.5f, 0.5f));
         drawRaysList(vecField, rayCalsRes.getNormalRays(), new Vector3(0.0f, 0.0f, 1.0f));
        
         rayCalsRes.getNormalRays().forEach((ray) -> {
             vecField.addVector(ray.getPosFrom(), ray.getPosTo().minus(ray.getPosFrom()).multiply(-1.0f), new Vector3(0.0f, 0.0f, 1.0f));
         });
     }
     
     protected void initVecField(){
         VectorField vecField = new VectorField(gl);
         
         TestRayTraceScene rayTraceCalc = new TestRayTraceScene();
         rayTraceCalc.init();
         
         drawRaysList(vecField,  rayTraceCalc.getSrcRays().getRefractedRays(), new Vector3(0.0f, 1.0f, 0.0f));
         //rayTraceCalc.getSrcRays();
      
         drawRaysGen(vecField, rayTraceCalc.getResult(1));
         drawRaysGen(vecField, rayTraceCalc.getResult(0));
         vecField.init();
         
         sceneObjects.put("VectorField", vecField);
     }
     
     public void init() throws LoadResourseException{
         this.cameraPosVector = new Vector3(0f, 0.0f, 4.0f);
         super.init();
        /* createLense();
         
         initVecField();*/
        //initGraph2d();
        initGraph3d();
       //this.loadHeadModel();  
         
     }
     
     protected float[] convertMesh(engine.Mesh  mesh){
         ArrayList<Vector> points = mesh.getPoints();
         float[]X = new float[points.size()];
         for(int i = 0; i < points.size(); i++){
             X[i] = (float)points.get(i).getCoordinates()[0];
         }
         return X;
     }
     
     protected void initGraph2d(){
               
         waveEquation1d wv = new waveEquation1d(10, 50);
         double[][]sol = wv.solve();

         GraphAnimated2d graph2d = new GraphAnimated2d(gl, convertMesh(wv.getMesh()),  sol);
         graph2d.init();//??????
         sceneObjects.put("Graph2d", graph2d);
     }
         protected double[][] getRefereneSolution2d(engine.Mesh mesh, int timeSteps){
        ReferenceWave2dSolution refSol = new ReferenceWave2dSolution();
        
        double[] TRange = MathUtils.linSpace(0, 1, timeSteps+1);
        double[][] XRef = refSol.getReferenceSolution(TRange, mesh.getPoints(), 1);
        
        return XRef;
    }
      protected void initGraph3d(){
               int T = 20;
         waveEquation2d wv = new waveEquation2d(10, T);
         double[][]sol = wv.solveAnalytics();
       //  double[][]sol = wv.solve();
          double[][] XRef = getRefereneSolution2d(wv.getMesh(), T);
         //GraphAnimated3d graph3d = new GraphAnimated3d(gl, wv.getMesh(),  XRef);
         
         /*for(int i = 0; i < sol.length; i++){
             for(int j = 0; j < sol[0].length; j++){
                 sol[i][j] = XRef[i][j]-sol[i][j];
             }
         }*/
         
         GraphAnimated3d graph3d = new GraphAnimated3d(gl, wv.getMesh(),  sol);
         graph3d.init();//??????
        // graph3d.bodyRotateX(-(float)Math.PI/2);
         sceneObjects.put("Graph3d", graph3d);
     }
}