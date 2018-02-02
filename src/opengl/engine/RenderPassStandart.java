/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL4;

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
    
}
