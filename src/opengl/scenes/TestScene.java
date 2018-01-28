/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import java.util.Map;
import opengl.engine.Scene;
import  javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.SceneCalculations;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;
import opengl.scenes.objects.CompShaderFiction;
import opengl.scenes.objects.Cube;
import opengl.scenes.objects.Cylinder;
import opengl.scenes.objects.Grid;
import opengl.scenes.objects.Quad;
import opengl.scenes.objects.Skybox;
import opengl.scenes.objects.Sphere;
import opengl.scenes.objects.VectorField;
/**
 *
 * @author Andrey
 */
public class TestScene extends Scene{
  
    public int[]  fboEnvMap = new int[1];
    protected int t;
    protected double startTime = 0;

    public TestScene(GL4 gl) throws LoadResourseException {
        super(gl);
        this.lightPosition = new Vector3(3.0f, 0.0f, 3.0f);
        this.camRotVec = new Vector3();
        this.cameraPosVector = new Vector3(1.0f, 0.0f, 0.0f);
      
        startTime = System.currentTimeMillis();
     
        
    }
   @Override
   public void display(GLAutoDrawable glad){
         super.display(glad);
         double R = 3;
         double t = System.currentTimeMillis() - startTime;
         double w = 0.2;
         
        // lightPosition = new Vector3((float)Math.cos(w * t), 0, (float)Math.sin(w * t));
         /*SceneObject light = sceneObjects.get("light");
         light.clearModelMatrix();
        light.bodyTranlate(lightPosition);*/
   }
     
    
     protected void renderEnviromentMap(){
         gl.glGenFramebuffers(1, fboEnvMap, 0);
     }
     
     protected void  loadHeadModel(){
           SceneObject africanHead;
           africanHead = new SceneObject(this.gl);
           africanHead.setOptBumpMapping(false);
           africanHead.setBumpMappingTextureFile("african_head_nm_tangent.tga");
           africanHead.setTextureFile("african_head_diffuse.tga");
           africanHead.setModelFile("african_head.obj");
           
           /*
           try{
               africanHead.addShaderProgram(africanHead.getShadersFilePath() + "normalVisualiser/", true, "NormVisualiser");
           } catch(LoadResourseException e){
               System.err.println("Load normal visualiser error");
               System.err.println(e.getMessage());
           }
           */
           
           africanHead.init();
           africanHead.bodyTranlate(new Vector3(0.0f, 0.0f,-2.0f));
           sceneObjects.put("head", africanHead);
        
     }
      protected void  loadHeadModel2(){
           SceneObject africanHead;
           africanHead = new SceneObject(this.gl);
           africanHead.setTextureFile("uv_checker large.png");
           africanHead.setModelFile("sphere.obj");
           africanHead.init();
            africanHead.bodyTranlate(new Vector3(1.0f, 0.0f, -5.0f));
           sceneObjects.put("head2", africanHead);
        
      }
      
      
    protected SceneObject createSphere(Vector3 pos,float scale, String textFile){     
        SceneObject sphere = new Sphere(this.gl);
        sphere.setTextureFile(textFile);

        sphere.init();
        sphere.bodyScale(scale);
        sphere.bodyTranlate(pos);
        return sphere;
    }  
    
    protected void  loadLightVisualiser(){
            
      
          // plane.bodyRotateX(-(float)Math.PI/2 );
           sceneObjects.put("light", createSphere(lightPosition,1.0f, "uv_checker large.png"));
        
     }

     protected void  loadCube(){
           SceneObject plane;
           plane = new Cube(this.gl);
           plane.setTextureFile("uv_checker large.png");
           //plane.setModelFile("floor.obj");
           /*  try{
               plane.addShaderProgram(plane.getShadersFilePath() + "normalVisualiser/", true, "NormVisualiser");
           } catch(LoadResourseException e){
               System.err.println("Load normal visualiser error");
               System.err.println(e.getMessage());
           }*/
           plane.init();
          plane.bodyScale(0.05f);
           plane.bodyTranlate(lightPosition);
          // plane.bodyRotateX(-(float)Math.PI/2 );
           sceneObjects.put("cube", plane);
        
     }
      protected void  loadQuad(){
           SceneObject plane;
           plane = new Quad(this.gl);
           plane.setTextureFile("uv_checker large.png");
           //plane.setModelFile("floor.obj");
           /*  try{
               plane.addShaderProgram(plane.getShadersFilePath() + "normalVisualiser/", true, "NormVisualiser");
           } catch(LoadResourseException e){
               System.err.println("Load normal visualiser error");
               System.err.println(e.getMessage());
           }*/
           plane.init();
        //  plane.bodyScale(0.5f);
          // plane.bodyTranlate(lightPosition);
          // plane.bodyRotateX(-(float)Math.PI/2 );
           sceneObjects.put("cube", plane);
        
     }
      protected void  loadSkybox(){
           
           SceneObject skybox = new Skybox(this.gl);
           //plane.setTextureFile("uv_checker large.png");
           //plane.setModelFile("floor.obj");
           skybox.init();   
           skybox.bodyTranlate(cameraPosVector);
        
            skybox.bodyScale(20f);
          // plane.bodyRotateX(-(float)Math.PI/2 );
           sceneObjects.put("skybox", skybox);
        
     }
     
     protected void loadGrid(){
         /*Grid item = new Grid(gl, 1);
         item.setTextureFile("uv_checker large.png");
         item.init();   
         
         sceneObjects.put("grid", item);
         
         Mesh mesh = item.getMesh();
         VectorField vecField = new VectorField(gl);
         
         vecField.addVector(new Vector3(1, 0, 2), new Vector3(0, 1, 0), new Vector3(0.5f, 0.5f, 0.5f));
         
         vecField.setVertexes(mesh.getVertexesCoords());
         vecField.setVertexData(mesh.getNormalsCoords());//getNormalsCoords*/
         //vecField.init();
       // loadLightVisualiser();
       SceneObject sph =  createSphere(new Vector3(0.0f, 0.0f,6.0f),0.2f, "uv_checker large.jpg");
         sceneObjects.put("sph", sph);
        loadHeadModel();
       
        // sceneObjects.put("vec_field", vecField);
        //loadQuad();
        initScreen();
         /*CompShaderFiction shad = new CompShaderFiction(gl);
         shad.setTextureFile("uv_checker large.png");
         shad.init();
         sceneObjects.put("comp_shader", shad);*/
         
     }
      
     protected void renderTriangle(){
     }
     
     protected void initScreen(){
           SceneObject screenObj = new SceneObject(this.gl);
           screenObj = new Quad(this.gl);
           screenObj.setTextureFile("uv_checker large.png");
           screenObj.setShaderProgName("color_map");
           screenObj.init();
           screenObj.bodyTranlate(new Vector3(1.0f, 0.0f, -2.0f));
           screenObj.bodyScale(2.0f);
           sceneObjects.put("screen", screenObj);
     }
     
     public void init() throws LoadResourseException{
         this.cameraPosVector = new Vector3(0f, 0.0f, 1.0f);
         super.init();
       //  Vector3 upVec = new Vector3(0, 1, 0);
       //  this.viewMatrix = SceneCalculations.lookAt( cameraPosVector, new Vector3(0,0, -2.0f) , upVec );
         //this.loadHeadModel();
         this.loadGrid();
       //  this.loadSphere();
        // this.loadCube();
             //    System.out.println(cameraRot);
       //this.loadCube();
      // this.loadSkybox();
        // loadHeadModel2();
       // this.loadPlane();
         
     }
     
    
}