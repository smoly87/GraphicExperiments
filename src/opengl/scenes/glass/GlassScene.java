/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.glass;
import opengl.scenes.glass.*;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.media.opengl.GL;
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
import opengl.scenes.objects.Quad;
import opengl.scenes.objects.Skybox;
import opengl.scenes.objects.Sphere;
import opengl.scenes.objects.VectorField;
/**
 *
 * @author Andrey
 */
public class GlassScene extends Scene{

    protected ColorMapPass colorMapPass;
    
    
    protected boolean optColorMap;
   
    protected int t;
    protected double startTime = 0;
    
    protected Matrix projectionMatrixOrtho;
    
    protected CubeMapViewMatrixes cubeMatrixes;
    
    protected int boxNum = 0;
    protected int dbgCurFace;
    protected Vector3 glassObjPos;
    
   
    
    @Override
    public void performRenderPasses() {
       if(optColorMapping)this.performColorBuffRenderPass();
       
       this.performStandartRenderPass();
    }

   
    protected void performColorBuffRenderPass(){
        
        SceneObject glass = sceneObjects.get("glass");
        boolean renderFlag = glass.isOptRenderEnabled();
        
        glass.setOptRenderEnabled(false);
                
        this.setStandartRenderVariables(colorMapPass);
       //  
        //Pay attention to FOV and possibly orthogonal projection
        colorMapPass.bindFbo();
        for(int i = 0; i < 6; i++){
           ////////////  gl.glClear(gl.GL_DEPTH_BUFFER_BIT); 
            int faceId = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i;//GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i;
            Matrix faceMatr = cubeMatrixes.getViewForFace(faceId);
            faceMatr = new ViewTransformations(faceMatr, glassObjPos).getViewMatrix();
            colorMapPass.setViewMatrix(faceMatr);
            colorMapPass.setProjectionMatrix(projectionMatrixOrtho);
            colorMapPass.setFaceId(faceId);
            colorMapPass.render();
        
           
        }
         colorMapPass.unBindFbo();
        glass.setOptRenderEnabled(renderFlag);
    }

    public GlassScene(GL4 gl) throws LoadResourseException {
        super(gl);
        this.lightPosition = new Vector3(3.0f, 0.0f, 3.0f);
        this.camRotVec = new Vector3();
        this.cameraPosVector = new Vector3(0.0f, 0.0f, 3.0f);
      
      
        startTime = System.currentTimeMillis();
        
        optColorMapping = true;
        optShadowMapping = false;
        
        colorMapPass = new ColorMapPass(this);

        renderPassStandart = new RenderPassMain(this);
        RenderPassMain mainRender = (RenderPassMain)renderPassStandart;
        
        mainRender.setOptColorMapping(optColorMapping);
        mainRender.setOptShadowMapping(optShadowMapping);
        projectionMatrixOrtho = SceneCalculations.calcProjMatrix(1024, 768, 90, Zfar, Znear);
        
        cubeMatrixes = new CubeMapViewMatrixes();
    }
   @Override
   public void display(GLAutoDrawable glad){
         super.display(glad);
         double R = 3;
         double t = System.currentTimeMillis() - startTime;
         double w = 0.2;
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
         protected SceneObject  loadBox(String boxName, String textureName, Vector3 pos){

           SceneObject body = new SceneObject(this.gl);
           body.setTextureFile(textureName);
           body.setModelFile("quad.obj");
           
           body.init();
           body.bodyTranlate(pos);
           sceneObjects.put(boxName, body);
           
           return body;
        
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
        sceneObjects.put("light", createSphere(lightPosition,1.0f, "uv_checker large.png"));
     }

     protected void  loadCube(){
           SceneObject plane;
           plane = new Cube(this.gl);
           plane.setTextureFile("uv_checker large.png");
           plane.init();
           plane.bodyScale(0.05f);
           plane.bodyTranlate(lightPosition);
           sceneObjects.put("cube", plane);
        
     }
      protected void  loadQuad(){
           SceneObject plane;
           plane = new Quad(this.gl);
           plane.setTextureFile("african_head_diffuse.tga");
           plane.init();
               plane.bodyTranlate(new Vector3(0.0f, 0.0f, -2.0f));
           plane.bodyScale(2.0f);
           sceneObjects.put("cube", plane);
        
     }
        
     
    
     protected void initScreen(){
           SceneObject screenObj = new SceneObject(this.gl);
           screenObj = new Quad(this.gl);
           screenObj.setTextureFile("uv_checker large.png");
           screenObj.setShaderProgName("ColorMap");
           screenObj.init();
           screenObj.bodyTranlate(new Vector3(1.0f, 0.0f, -2.0f));
           screenObj.bodyScale(2.0f);
           sceneObjects.put("screen", screenObj);
     }
     
     
     protected SceneObject addBox(Vector3 pos, String texture){
         SceneObject box = loadBox("box_" + boxNum, texture , pos );
         box.bodyScale(2.0f);
         boxNum++;
         //box.bodyRotateZ((float)Math.PI);
         return box;
     }
     
     protected void addSidesBoxes(){
         addBox(new Vector3(0.0f, 0.5f,-2.0f),"uv_checker large_flip.jpg");
         
         addBox(new Vector3(2.0f, 0.5f, 0.0f), "uv_checker large_flip_R.jpg").bodyRotateY(-(float)Math.PI/2);
         addBox(new Vector3(-2.0f, 0.5f, 0.0f),"uv_checker large_flip_L.jpg").bodyRotateY((float)Math.PI/2);
         //addBox(new Vector3(0.0f, 0.0f,-2.0f)).bodyRotateY((float)Math.PI/2);
     }
     
     public void init() throws LoadResourseException{
         this.cameraPosVector = new Vector3(0f, 0.0f, 4.0f);
         glassObjPos = new Vector3(0.0f, 0.2f, 0.0f);
         super.init();
         //loadCube();
         SceneObject floor = loadBox("floor", "floor_diffuse.tga", new Vector3(0.0f, -0.5f,0.0f));
         floor.bodyRotateX(-(float)Math.PI/2.0f);
         floor.bodyScale(4.0f);
         addSidesBoxes();
        createGlass();
      // this.loadSkybox();
       //  initScreen();
         //this.loadHeadModel();
       //  this.loadQuad();

     }

     protected void createGlass(){
        SceneObject sphere = new SceneObject(this.gl);
        sphere.setTextureFile("uv_checker large.jpg");
        
        sphere.setModelFile("african_head.obj");
        //sphere.optLoadTexture = false;
        sphere.setShaderProgName("glass");
        sphere.init();
        sphere.bodyScale(0.5f);
        sphere.bodyTranlate(glassObjPos);
        sceneObjects.put("glass", sphere);
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
    @Override
    public void processKeyPress(KeyEvent e) {
        super.processKeyPress(e); //To change body of generated methods, choose Tools | Templates.
 
        /*float factor;
         factor = e.isShiftDown() ? 10 : 1;*/
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F10:
                  sceneObjects.get("glass").setOptRenderEnabled(false);
                
                 Matrix faceMatr = cubeMatrixes.getViewForFace(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + dbgCurFace);
                 viewTrasnform = new ViewTransformations(faceMatr, glassObjPos);
                 recalcViewMatrix();
                 dbgCurFace++;
                 if(dbgCurFace > 5) dbgCurFace = 0;
                 System.out.println(">" + dbgCurFace);
                break;
        }
    }
     
    
}