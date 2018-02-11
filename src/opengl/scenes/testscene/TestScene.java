/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.testscene;
import opengl.engine.DepthMapPass;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
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
import opengl.engine.ShaderProgOptions;
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

    protected ColorMapPass colorMapPass;
    protected DepthMapPass depthMapPass;
    
    protected boolean optColorMap;
    public int[]  fboEnvMap = new int[1];
    protected int t;
    protected double startTime = 0;
    
    protected Matrix projectionMatrixOrtho;
    
    @Override
    public void performRenderPasses() {
       if(optColorMapping)this.performColorBuffRenderPass();
       super.performRenderPasses();
    }

 
    
    protected void performColorBuffRenderPass(){
        
        sceneObjects.get("screen").setOptRenderEnabled(false);
        
        Matrix lightView = getLightMVP();
        lightMVP = new ViewTransformations(lightView, lightPosition).getViewMatrix();
        this.setStandartRenderVariables(colorMapPass);
        colorMapPass.setViewMatrix(lightMVP);
        
       
        colorMapPass.render();

         sceneObjects.get("screen").setOptRenderEnabled(true);
    }

    public TestScene(GL4 gl) throws LoadResourseException {
        super(gl);
        this.lightPosition = new Vector3(0.0f, 0.0f, 3.0f);
        this.camRotVec = new Vector3();
        this.cameraPosVector = new Vector3(0.0f, 0.0f, 3.0f);
      
        startTime = System.currentTimeMillis();
        
        optColorMapping = false;
        optShadowMapping = true;
        
        colorMapPass = new ColorMapPass(this);
        depthMapPass = new DepthMapPass(this);
        renderPassStandart = new RenderPassMain(this);
        RenderPassMain mainRender = (RenderPassMain)renderPassStandart;
        
        mainRender.setOptColorMapping(optColorMapping);
        mainRender.setOptShadowMapping(optShadowMapping);
        projectionMatrixOrtho = SceneCalculations.calcProjOrtoMatrix(1024, 768, fieldOfView, Zfar, Znear);
        
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
     
    
     
     protected void  loadHeadModel() throws LoadResourseException{
           SceneObject africanHead;
           africanHead = new SceneObject(this.gl);
           africanHead.setOptBumpMapping(true);
           africanHead.setBumpMappingTextureFile("african_head_nm_tangent.tga");
           africanHead.setTextureFile("african_head_diffuse.tga");
           africanHead.setModelFile("african_head.obj");
           //africanHead.addShaderProgram("normalVisualiser", new ShaderProgOptions(true));
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
           
    protected SceneObject createSphere(Vector3 pos,float scale, String textFile){     
        SceneObject sphere = new Sphere(this.gl);
        sphere.setTextureFile(textFile);

        sphere.init();
        sphere.bodyScale(scale);
        sphere.bodyTranlate(pos);
        return sphere;
    }  

     protected void initSphere(){
         
        SceneObject sphere = createSphere(new Vector3(0.0f, 0.0f, 2.0f), 0.1f, "uv_checker large.png");
         sceneObjects.put("sphere", sphere);
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
     
   
     
     public void init() throws LoadResourseException{
         this.cameraPosVector = new Vector3(0f, 0.0f, 4.0f);
         super.init();
          this.loadHeadModel();
         initSphere();
         initScreen();
      
         
     }
     
    
}