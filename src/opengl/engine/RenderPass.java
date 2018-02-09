/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL4;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.ViewTransformations;

/**
 *
 * @author Andrey
 */
public class RenderPass {
    protected HashMap<String, SceneObject> sceneObjects;
    
    
    protected Matrix viewMatrix;
    protected Matrix projectionMatrix;
    
    protected Vector3 cameraPosVector ;
    protected GL4 gl;

    protected Scene scene;
    
    public GL4 getGl() {
        return gl;
    }

    public void setGl(GL4 gl) {
        this.gl = gl;
    }
    public RenderPass(Scene scene) {
        this.scene = scene;
        this.sceneObjects = scene.sceneObjects;
        this.gl = scene.getGl();
    }

    public Vector3 getCameraPosVector() {
        return cameraPosVector;
    }

    public void setCameraPosVector(Vector3 cameraPosVector) {
        this.cameraPosVector = cameraPosVector;
    }

    public Vector3 getLightPosition() {
        return lightPosition;
    }

    public void setLightPosition(Vector3 lightPosition) {
        this.lightPosition = lightPosition;
    }
    protected Vector3 lightPosition  ;
    
    public Matrix getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public Matrix getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
    
        
    protected void setCommonVarsToShaderProgram(GL4 gl, GLSLProgramObject programObject){

        programObject.setUniform(gl, "light.position", lightPosition.values, 3);
        programObject.setUniform(gl, "viewDir", cameraPosVector.normalise().values, 3);
       // setLightsProps(gl3, programObject);
    } 
    
        
    public void render(){
        
         for (Map.Entry<String, SceneObject> entry : sceneObjects.entrySet()) {
           String key = entry.getKey();
           SceneObject sceneObj = entry.getValue();
           //setObjectPropsFrame(sceneObj, key);
           if(sceneObj.isOptRenderEnabled()) {
               renderObject(gl, sceneObj, key);
           
           }
        }
    }
    
    protected GLSLProgramObject getProgramByObj(SceneObject sceneObj){
        return (GLSLProgramObject)sceneObj.getShadersPrograms().values().toArray()[0];
    }
    
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName){   
           GLSLProgramObject programObject = getProgramByObj(sceneObj);
           programObject.bind(gl);
           execObjShaderProg(gl, sceneObj, "Main", programObject);
           programObject.unbind(gl);
    }
    
    protected void setVariablesToShader(GL4 gl, SceneObject sceneObj, String progName, GLSLProgramObject programObject){
        setStandartVariablesToShader(gl, sceneObj, progName, programObject);
    }
    
    protected void setStandartVariablesToShader(GL4 gl,SceneObject sceneObj, String progName, GLSLProgramObject programObject){
        this.setCommonVarsToShaderProgram(gl, programObject);
        sceneObj.setGlobalMatricies(programObject, projectionMatrix, viewMatrix);
        sceneObj.setMaterialPropsToShader(programObject);
        sceneObj.setBodyPropsToShader(programObject);

    }
    
    protected  void  execObjShaderProg(GL4 gl, SceneObject sceneObj, String progName, GLSLProgramObject programObject){
 
        setVariablesToShader(gl, sceneObj, progName, programObject);

       
        sceneObj.display(gl, programObject, progName);
        
    }
}
