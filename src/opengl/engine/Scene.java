/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.engine;

import engine.exception.LoadResourseException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_CLAMP_TO_EDGE;
import static javax.media.opengl.GL.GL_DEPTH_COMPONENT16;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_FRAMEBUFFER;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_S;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_T;
import static javax.media.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.SceneCalculations;
import math.sceneCalculations.ViewTransformations;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixRotationZ;
import math.transformMatricies4.MatrixTranslation;
import math.transformMatricies4.MatrixUnit;

/**
 *
 * @author Andrey
 */
public class Scene implements KeyListener{
    protected int screenWidth;
    protected int screenHeight;
    protected float fieldOfView = 180;

    protected boolean optUseProjectionMatrix = true;
    protected boolean optUseViewMatrix = false;
    protected boolean optUseModelMatrix = false;
    protected boolean optDeepTest = true;
    protected boolean optWireFrame = false;
    
    protected boolean optShadowMapping = true;
    protected boolean optColorMapping = false;
    
    protected String assetsFilepath = "/graphicsexperiment/assets/";
    protected String shadersFilePath =  "shaders/"; 
    
    protected Matrix viewMatrix;
    protected Matrix normTransform;
    protected Matrix projectionMatrix;
    
    protected Matrix basisMatr ;
    protected Matrix viewMatrixBearing;
    
    protected Matrix camView ;
    protected Matrix camTrans ;
    
    protected float Zfar = 30.0f;
    protected float Znear = 0.1f;
    
    protected Vector3 cameraPosVector ;
    protected Vector3 camRotVec  ;
    protected Vector3 lightPosition  ;
    protected Vector3 initialCamPos;
    
    protected HashMap<String, SceneObject> sceneObjects;
    protected IntBuffer FBoBuffers;
    protected HashMap<String, GLSLProgramObject>shadersExtPrograms;
    protected IntBuffer textBuffers;
    
    protected ShadersStore shadersStrore;
    protected MainConfig config;
    
    protected GL4 gl;
    
    protected ViewTransformations viewTrasnform ;
    
    protected Matrix lightMVP = new MatrixUnit();
    protected int depthTextureId;
    
    protected FrameBuffer colorBuf;
    protected FrameBuffer shadowBuf;
    
    protected RenderPassStandart renderPassStandart;
    
    public Scene(GL4 gl) throws LoadResourseException{
        sceneObjects = new HashMap<>();
        shadersExtPrograms = new HashMap<>();
        this.gl = gl;
        shadersStrore = ShadersStore.getInstance();
        config = MainConfig.getInstance();
        renderPassStandart = new RenderPassStandart(sceneObjects);
        
    }
    
    protected void initShadowMapProgNew() throws LoadResourseException{
        GLSLProgramObject shaderProg  = shadersStrore.getShaderProgram("ShadowMap", false);
        this.shadersExtPrograms.put("ShadowMap", shaderProg);
        
        shadowBuf = new FrameBuffer(gl, 1024, 768);
        shadowBuf.setBufferPurpose(GL.GL_COLOR_ATTACHMENT0);
        shadowBuf.setTexturePurpose1(GL.GL_RGB);
        shadowBuf.setTexturePurpose2(GL.GL_RGB);
        //shadowBuf.setBufferType(GL_FLOAT);
        shadowBuf.setUseDepthRenderBuffer(true);
        shadowBuf.setUseDrawBuffer(true);
        shadowBuf.init();
    }
    protected void initShadowMapProg() throws LoadResourseException{
        GLSLProgramObject shaderProg  = shadersStrore.getShaderProgram("ShadowMap", false);
        this.shadersExtPrograms.put("ShadowMap", shaderProg);
        
        shadowBuf = new FrameBuffer(gl, 1024, 768);
        shadowBuf.setBufferPurpose(GL.GL_DEPTH_ATTACHMENT);
        shadowBuf.setTexturePurpose1(GL.GL_DEPTH_ATTACHMENT);
        shadowBuf.setTexturePurpose2(GL_DEPTH_COMPONENT);
        shadowBuf.setTextureValueType(GL_FLOAT);
        shadowBuf.setUseDepthRenderBuffer(false);
        shadowBuf.setUseDrawBuffer(false);
        shadowBuf.init();
    }
    
    protected void initColorMapProg() throws LoadResourseException{
       /* GLSLProgramObject shaderProg  = GLProgramBuilder.buildProgram(gl, assetsFilepath + shadersFilePath + "color_map/", true, true, false);
        this.shadersExtPrograms.put("ColorMap", shaderProg);*/
       colorBuf = new FrameBuffer(gl, 1024, 768);
       colorBuf.setBufferPurpose(GL.GL_COLOR_ATTACHMENT0);
       colorBuf.setTexturePurpose1(GL.GL_RGB);
       colorBuf.setTexturePurpose2(GL.GL_RGB);
       colorBuf.setUseDepthRenderBuffer(true); 
       colorBuf.setUseDrawBuffer(true);
       colorBuf.setTextureValueType(GL.GL_UNSIGNED_BYTE);
      colorBuf.init();
    }
    
    /**
     * @return the fieldOfView
     */
    public float getFieldOfView() {
        return fieldOfView;
    }

    /**
     * @param fieldOfView the fieldOfView to set
     */
    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
    protected void setCommonVarsToShaderProgram(GL4 gl, GLSLProgramObject programObject){
        programObject.setUniform(gl, "viewMatrix", this.getViewMatrix());
        programObject.setUniform(gl, "projectionMatrix", projectionMatrix);
        
        programObject.setUniform(gl, "light.position", lightPosition.values, 3);
        programObject.setUniform(gl, "viewDir", cameraPosVector.normalise().values, 3);
       // setLightsProps(gl3, programObject);
    }
    public void init() throws LoadResourseException{
        viewMatrix = new MatrixUnit();
        viewMatrixBearing = new  MatrixUnit();
        basisMatr = new MatrixUnit();
        initialCamPos = this.camRotVec.toVector3();
        
        camView = new MatrixUnit();
        camTrans = new MatrixUnit();
        
        
        calcProjMatrix();
        resetCamera();
        
    
        
        if(optColorMapping){
            initColorMapProg();
        }
        
        if(optShadowMapping){
            initShadowMapProg();
        }
        //calcCameraMatrix();
        //createLight(gl3);
    }
    
    protected Matrix getViewMatrix(){
        return viewMatrix;
//viewMatrix = viewMatrixBearing.;
        /*viewMatrix.values = Arrays.copyOf(viewMatrixBearing.values, viewMatrixBearing.values.length);
        
        Matrix44 matRotY = new MatrixRotationY(camRotVec.values[1]);
        Matrix44 matRotX = new MatrixRotationX(camRotVec.values[0]);
        
        rotMatr = matRotX.multiply(matRotY);*/
        
        /*float[] invCameraCoords = new float[]{-cameraPosVector.values[0], -cameraPosVector.values[1], cameraPosVector.values[2], 1.0f};
        Vector offsetT = rotMatr.multiply(new Vector(invCameraCoords));*/
        /*viewMatrix = rotMatr.multiply(new MatrixTranslation(new Vector3(-cameraPosVector.values[0], -cameraPosVector.values[1], cameraPosVector.values[2])));
        return viewMatrix;*/
    }
    
    protected void calcCameraMatrix(){
        //cameraPosVector = new Vector3(0.0f, 0.0f, -2.f);
       // rotMatr = new MatrixUnit();
       // cameraPosVector = new Vector3(0.0f, 0.0f, -2.f);
        
       // viewMatrixBearing = SceneCalculations.lookAt(new Vector3(2.f, 0.0f, 2.f),  new Vector3(0.0f, 0.0f, 0.f), null);
        //viewMatrix.values = Arrays.copyOf(viewMatrixBearing.values, viewMatrixBearing.values.length);
    }
    
    public void setProjectionMatrix(Matrix projMatr){
        projectionMatrix = projMatr;
    }
    
    
    public void display(GLAutoDrawable glad){
        GL4 gl = glad.getGL().getGL4();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        if(optWireFrame) gl.glPolygonMode( gl.GL_FRONT_AND_BACK, gl.GL_LINE );
        
         if(optDeepTest){
            gl.glEnable(GL4.GL_CULL_FACE);
            gl.glCullFace(GL4.GL_BACK);
            gl.glFrontFace(GL4.GL_CCW);

            gl.glEnable(GL3.GL_DEPTH_TEST);
        }
        
        if(optColorMapping){
          
           // 
           //gl.glDisable(gl.GL_DEPTH_TEST);
           sceneObjects.get("screen").setOptRenderEnabled(false);
           colorBuf.bindFBO();  
           gl.glClear( GL4.GL_COLOR_BUFFER_BIT|gl.GL_DEPTH_BUFFER_BIT);
       
           
           this.renderObjects(gl);
           colorBuf.unbind();
           sceneObjects.get("screen").setOptRenderEnabled(true);
           
        }
        
        
       
        if(optShadowMapping){
            
            sceneObjects.get("screen").setOptRenderEnabled(false);
            renderShadowMap();
            sceneObjects.get("screen").setOptRenderEnabled(true);
        }
   
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT|gl.GL_DEPTH_BUFFER_BIT); //  
        gl.glEnable(GL4.GL_TEXTURE_CUBE_MAP);
        
        renderPassStandart.setProjectionMatrix(projectionMatrix);
        renderPassStandart.setViewMatrix(viewMatrix);
        renderPassStandart.setLightPosition(lightPosition);
        renderPassStandart.setCameraPosVector(cameraPosVector);
        renderPassStandart.render(gl);

        glad.swapBuffers();
     }
    public void calcProjMatrix(){
        if(optUseProjectionMatrix){
            projectionMatrix = SceneCalculations.calcProjMatrix(screenWidth, screenHeight, fieldOfView, Zfar, Znear);
            //projectionMatrix = SceneCalculations.calcProjOrtoMatrix(screenWidth, screenHeight, fieldOfView, Zfar, Znear);
            System.out.println(projectionMatrix); 
            
        } else{
            projectionMatrix = new MatrixUnit();
           
        }
        
    } 
        
   
    
    protected void renderShadowMap(){
 
        Matrix lightView = getLightMVP();
        lightMVP = new ViewTransformations(lightView, lightPosition).getViewMatrix();
        GLSLProgramObject shadowProg = shadersExtPrograms.get("ShadowMap");
       
        shadowBuf.bindFBO();
        
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT| GL4.GL_DEPTH_BUFFER_BIT);
       
         
        
        for (Map.Entry<String, SceneObject> entry : sceneObjects.entrySet()) {
           String key = entry.getKey(); 
           SceneObject sceneObj = entry.getValue();
           if(!sceneObj.isOptRenderEnabled()) continue;
           shadowProg.bind(gl);
             shadowProg.setUniform(gl, "lightMVP", lightMVP);
           
           
           execObjShaderProg(gl, sceneObj, key, shadowProg);
        }
        
        shadowBuf.unbind();
      // shadowProg.unbind(gl);
    }
    
    protected void renderObjects(GL4 gl){
         for (Map.Entry<String, SceneObject> entry : sceneObjects.entrySet()) {
           String key = entry.getKey();
           SceneObject sceneObj = entry.getValue();
           //setObjectPropsFrame(sceneObj, key);
           if(sceneObj.isOptRenderEnabled()) {
               renderObject(gl, sceneObj, key);
           
           }
        }
    }
    
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName){
        
         for (Map.Entry<String, GLSLProgramObject> entry : sceneObj.getShadersPrograms().entrySet()) {
           String key = entry.getKey();
           GLSLProgramObject programObject = entry.getValue();
          programObject.bind(gl);
           if (optColorMapping &&  objName.equals("screen")){
               colorBuf.setTexture(programObject);
           }
           if(optShadowMapping){
              programObject.setUniform(gl, "lightMVP", lightMVP);
              shadowBuf.setTexture(programObject);
           }
           
           execObjShaderProg(gl, sceneObj, key, programObject);
           
         }
    }
    
    protected void execObjShaderProg(GL4 gl, SceneObject sceneObj, String progName, GLSLProgramObject programObject){
        
        sceneObj.setGlobalMatricies(programObject, projectionMatrix, viewMatrix);
        sceneObj.setMaterialPropsToShader(programObject);
        sceneObj.setBodyPropsToShader(programObject);
        // programObject.setUniform(gl, "modelMatrix", modelMatrix);
        setCommonVarsToShaderProgram(gl, programObject);
        //setShadowMapTexture(programObject);
        
        
        sceneObj.display(gl, programObject, progName);
        programObject.unbind(gl);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        /*float factor;
         factor = e.isShiftDown() ? 10 : 1;*/
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1:
                 optWireFrame = !optWireFrame;
                 break;
            case KeyEvent.VK_F2:
                 optDeepTest = !optDeepTest;
                 break;    
            case KeyEvent.VK_LEFT:
                //incCameraRotation(new Vector3(new float[]{0.0f, +0.1f, 0.0f}));
                incCameraRotationY(0.05f);
                break;
            case KeyEvent.VK_RIGHT:
               // incCameraRotation(new Vector3(new float[]{0.0f, -0.1f, 0.0f}));
                incCameraRotationY(-0.05f);
                break;
            case KeyEvent.VK_UP:
                //incCameraRotation(new Vector3(new float[]{+0.1f, 0.0f, 0.0f}));
                incCameraRotationX(0.05f);
                break;
            case KeyEvent.VK_DOWN:
                //incCameraRotation(new Vector3(new float[]{-0.1f, 0.0f, 0.0f}))
                incCameraRotationX(-0.05f);
                break;
            case KeyEvent.VK_W:
                incCameraPosition(new Vector3(new float[]{0.0f, 0.0f, -0.2f}));
                break;
            case KeyEvent.VK_S:
                incCameraPosition(new Vector3(new float[]{0.0f, 0.0f, +0.2f}));
                break;
            case KeyEvent.VK_A:
                incCameraPosition(new Vector3(new float[]{-0.2f, 0.0f, 0.0f}));
                break;
            case KeyEvent.VK_D:
                incCameraPosition(new Vector3(new float[]{+0.2f, 0.0f, 0.0f}));
                break;    
            case KeyEvent.VK_PAGE_UP:
                incCameraPosition(new Vector3(new float[]{0.0f, +0.1f, 0.0f}));
                break;
            case KeyEvent.VK_PAGE_DOWN:
                incCameraPosition(new Vector3(new float[]{0.0f, -0.1f, 0.0f}));
                break;    
                
            case KeyEvent.VK_F5:
                resetCamera();
                break;
            case KeyEvent.VK_F6:
                lookFromLight();
                break;
             case KeyEvent.VK_F7:
                lookFromTop();
                break;    
                    
        }
    }
    
    protected Matrix getLightMVP(){
        Vector3 upVec = new Vector3(0.0f, 1.0f, 0.0f);
        Vector3 center = new Vector3(0.0f, 0.0f, 0.0f);
        Matrix lightMVP = SceneCalculations.lookAt(this.lightPosition, center, upVec);
        return lightMVP;
    }
    
    protected void lookFromLight(){
        camView = getLightMVP();
        viewTrasnform = new ViewTransformations(camView, lightPosition);
       // basisMatr = viewMatrix;
        recalcViewMatrix();
       
        Vector3 lightPosNew =  viewMatrix.multiply(this.lightPosition, 1);
        System.out.println("Light pos is" + lightPosNew.toString());
    }
    
    protected void lookFromTop(){
        cameraPosVector = new Vector3 (0.0f, 7.0f, 0.0f);
        Vector3 upVec = new Vector3(0.0f, 1.0f, 0.0f);
        Vector3 center = new Vector3(0.0f, 0.0f, 0.0f);
        camView = SceneCalculations.lookAt(cameraPosVector, center, upVec);
        viewTrasnform = new ViewTransformations(camView, cameraPosVector);
       // basisMatr = viewMatrix;
       recalcViewMatrix();

    }
    protected void resetCamera(){
        viewTrasnform = new ViewTransformations( new MatrixUnit(), initialCamPos);
        recalcViewMatrix();
    }
    
    protected void recalcViewMatrix(){
        
        viewMatrix = viewTrasnform.getViewMatrix();
        //cameraPosVector = new Vector3();
     }
    
    public void incCameraRotation(Vector3 deltaVec){
       Matrix44 matRotY = new MatrixRotationY(deltaVec.values[1]);
       Matrix44 matRotX = new MatrixRotationX(deltaVec.values[0]);
       Matrix M1 =  (matRotY.multiply(matRotX));
       
       Matrix Tr = viewTrasnform.getTranslationMatrix();
       camView = M1.multiply(camView);
       //camRotVec = basisMatr.multiply(camRotVec);
       //Commutator
       
       
       // M1.plus(M2.changeSign()).toString();
       
       
       //cameraPosVector = rotMatr.multiply(cameraPosVector) ;
       recalcViewMatrix();
    }
    
    
    public void incCameraRotationX(float angle){
       viewTrasnform.applyTransform(new MatrixRotationX(angle));
       recalcViewMatrix();
    }
    
     public void incCameraRotationY(float angle){
       viewTrasnform.applyIncRotationAroundAbsY(angle);
      //viewTrasnform.applyTransform(new MatrixRotationY(angle));
      
       recalcViewMatrix();
         
      /* Vector3 basisAngles = SceneCalculations.basisAngles(camView, new MatrixUnit());
       float y_Angle = basisAngles.values[0];
       Matrix44 matRotZ = new MatrixRotationX(y_Angle);
       Matrix44 matMov = new MatrixTranslation(cameraPosVector);
       Matrix matConv = matMov.multiply(matRotZ);
       //It's operation for return from y' basis to absolute y(world coordinates)
       
       
       camView = matConv.transpose().multiply(camView);
       
       System.out.println(basisAngles);
         
       Matrix44 matRot = new MatrixRotationY(angle);
       camView = matRot.multiply(camView);
       
       //Basis should be returned to relative basis y' after all transformtaions
       camView = matConv.multiply(camView);
       
       recalcViewMatrix();*/
    }
    public void incCameraPosition(Vector3 deltaVec){

       
       viewTrasnform.applyTranslation(deltaVec);
       recalcViewMatrix();
       
       
       if(sceneObjects.containsKey("skybox")){
          SceneObject skybox = sceneObjects.get("skybox");
          skybox.setObjTranslateVec(cameraPosVector);
       }
    }
    
    
    public boolean isOptDeepTest() {
        return optDeepTest;
    }

    public void setOptDeepTest(boolean optDeepTest) {
        this.optDeepTest = optDeepTest;
    }

    public boolean isOptWireFrame() {
        return optWireFrame;
    }

    public void setOptWireFrame(boolean optWireFrame) {
        this.optWireFrame = optWireFrame;
    }
   
    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public boolean isOptUseProjectionMatrix() {
        return optUseProjectionMatrix;
    }

    public void setOptUseProjectionMatrix(boolean optUseProjectionMatrix) {
        this.optUseProjectionMatrix = optUseProjectionMatrix;
    }

    public boolean isOptUseViewMatrix() {
        return optUseViewMatrix;
    }

    public void setOptUseViewMatrix(boolean optUseViewMatrix) {
        this.optUseViewMatrix = optUseViewMatrix;
    }

    public boolean isOptUseModelMatrix() {
        return optUseModelMatrix;
    }

    public void setOptUseModelMatrix(boolean optUseModelMatrix) {
        this.optUseModelMatrix = optUseModelMatrix;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
            keyPressed(e);
    }

   
}
