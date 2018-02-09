/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL4;
import math.linearAlgebra.Matrix;
import math.sceneCalculations.ViewTransformations;

/**
 *
 * @author Andrey
 */
public class RenderPassStandart extends RenderPass{

    public RenderPassStandart(Scene scene) {
        super(scene);
    }

    @Override
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName) {
         for (Map.Entry<String, GLSLProgramObject> entry : sceneObj.getShadersPrograms().entrySet()) {
           String key = entry.getKey();
           
           GLSLProgramObject programObject = entry.getValue();
           
           programObject.bind(gl);
           execObjShaderProg(gl, sceneObj, key, programObject);
           programObject.unbind(gl);
           
         }
    }

    @Override
    protected void setVariablesToShader(GL4 gl, SceneObject sceneObj, String progName, GLSLProgramObject programObject) {
        super.setVariablesToShader(gl, sceneObj, progName, programObject); 
         if(scene.optShadowMapping){
          
                Matrix lightView = scene.getLightMVP();
                Matrix lightMVP = new ViewTransformations(lightView, scene.lightPosition).getViewMatrix();
                programObject.setUniform(gl, "lightMVP", lightMVP);
                
                FrameBuffer depthBuff = scene.getFrameBuffersStorage().get("DepthBuffer");
                depthBuff.setTexture(programObject);
            
        }
        
    }
    
}
