/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.SceneCalculations;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
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
       
    protected Matrix viewMatrix;
    protected Matrix normTransform;
    protected Matrix projectionMatrix;
    
    protected Matrix basisMatr ;
    protected Matrix viewMatrixBearing;
    
    
    protected float Zfar = 10.0f;
    protected float Znear = 1.0f;
    
    protected Vector3 cameraPosVector = new Vector3(0f, 0.0f, 0.0f);
    protected Vector3 camRotVec  = new Vector3(0f,0f,0f);
    protected Vector3 lightPosition  = new Vector3(3f, 0f, 0.5f);
    
    protected HashMap<String, SceneObject> sceneObjects;
    
    public Scene(){
       sceneObjects = new HashMap<>();
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
    public void init(){
        viewMatrix = new MatrixUnit();
        viewMatrixBearing = new  MatrixUnit();
        basisMatr = new MatrixUnit();
        
        calcProjMatrix();
        recalcViewMatrix();
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
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT); //  
       gl.glEnable(GL4.GL_TEXTURE_CUBE_MAP);
        if(optWireFrame) gl.glPolygonMode( gl.GL_FRONT_AND_BACK, gl.GL_LINE );
        
        if(optDeepTest){
          gl.glEnable(GL4.GL_CULL_FACE);
            gl.glCullFace(GL4.GL_BACK);
            gl.glFrontFace(GL4.GL_CCW);

          gl.glEnable(GL3.GL_DEPTH_TEST);
           // gl.glDepthMask(true);
         // gl.glDepthFunc(GL3.GL_LESS);
           // gl.glDepthRangef(0.0f, 10.0f);
           // gl.glEnable(GL4.GL_DEPTH_CLAMP);
        }
        
        this.renderObjects(gl);
        
        glad.swapBuffers();
     }
    public void calcProjMatrix(){
        if(optUseProjectionMatrix){
            projectionMatrix = SceneCalculations.calcProjMatrix(screenWidth, screenHeight, fieldOfView, Zfar, Znear);
        } else{
            projectionMatrix = new MatrixUnit();
           
        }
        
    } 
    
    protected void renderObjects(GL4 gl){
         for (Map.Entry<String, SceneObject> entry : sceneObjects.entrySet()) {
           String key = entry.getKey();
           SceneObject sceneObj = entry.getValue();
           //setObjectPropsFrame(sceneObj, key);
            renderObject(gl, sceneObj);
        }
    }
    
    protected void renderObject(GL4 gl, SceneObject sceneObj){
        
         for (Map.Entry<String, GLSLProgramObject> entry : sceneObj.getShadersPrograms().entrySet()) {
           String key = entry.getKey();
           GLSLProgramObject programObject = entry.getValue();
           execObjShaderProg(gl, sceneObj, key, programObject);
           
         }
    }
    
    protected void execObjShaderProg(GL4 gl, SceneObject sceneObj, String progName, GLSLProgramObject programObject){
        programObject.bind(gl);
        sceneObj.setGlobalMatricies(programObject, projectionMatrix, viewMatrix);
        sceneObj.setMaterialPropsToShader(programObject);
        sceneObj.setBodyPropsToShader(programObject);
        // programObject.setUniform(gl, "modelMatrix", modelMatrix);
        setCommonVarsToShaderProgram(gl, programObject);
        sceneObj.display(gl, programObject, progName);
        programObject.unbind(gl);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        /*float factor;
         factor = e.isShiftDown() ? 10 : 1;*/
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F5:
                 optWireFrame = !optWireFrame;
                 break;
            case KeyEvent.VK_F6:
                 optDeepTest = !optDeepTest;
                 break;    
            case KeyEvent.VK_LEFT:
                incCameraRotation(new Vector3(new float[]{0.0f, +0.1f, 0.0f}));
                break;
            case KeyEvent.VK_RIGHT:
                incCameraRotation(new Vector3(new float[]{0.0f, -0.1f, 0.0f}));
                break;
            case KeyEvent.VK_UP:
                incCameraRotation(new Vector3(new float[]{+0.1f, 0.0f, 0.0f}));
                break;
            case KeyEvent.VK_DOWN:
                incCameraRotation(new Vector3(new float[]{-0.1f, 0.0f, 0.0f}));
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
        }
    }
    
    protected void recalcViewMatrix(){
        
        
       /* Vector3 lookVec = new Vector3(0f, 0f , -1f);
        Vector3 centerOfWorldVec = new  Vector3(0f, 0f , 0f);
        Vector3 upVec = new Vector3(0f, 1f , 0f);
        lookVec = rotMatr.multiply(lookVec);
        //Если взгляд по оси Y направлен, то up будем лежать с ним в одной плоскости!
        if(Math.abs(Math.abs(lookVec.normalise().values[1]) - 1.0f) < 0.05){
             Matrix44 matRotX = new MatrixRotationX((float)Math.PI / 4);
             upVec = matRotX.multiply(lookVec);
        }
        
        

        viewMatrix = SceneCalculations.lookAt( cameraPosVector, cameraPosVector.plus(lookVec), upVec );*/
        MatrixTranslation Tr = new MatrixTranslation(cameraPosVector);
        //(A*B)^-1 = (B^-1)*(A^-1)
        //-1 = T для ортонормированного базиса
        viewMatrix = (basisMatr.transpose()).multiply(Tr);
     }
    
    public void incCameraRotation(Vector3 deltaVec){
       Matrix44 matRotY = new MatrixRotationY(deltaVec.values[1]);
       Matrix44 matRotX = new MatrixRotationX(deltaVec.values[0]);
       Matrix M1 =  matRotY.multiply(matRotX);
       
       basisMatr = basisMatr.multiply(M1);
       //Commutator
       
       Matrix M2 =  matRotY.multiply(matRotX);
       
       // M1.plus(M2.changeSign()).toString();
       
       
       //cameraPosVector = rotMatr.multiply(cameraPosVector) ;
       recalcViewMatrix();
    }
    
    public void incCameraPosition(Vector3 deltaVec){

       
       Vector3 vecT = basisMatr.multiply(deltaVec);
       cameraPosVector = cameraPosVector.minus(vecT ) ;
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
