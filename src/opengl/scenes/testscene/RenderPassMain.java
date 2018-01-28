/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.testscene;

import java.util.HashMap;
import javax.media.opengl.GL4;
import opengl.engine.FrameBuffer;
import opengl.engine.GLSLProgramObject;
import opengl.engine.RenderPassStandart;
import opengl.engine.Scene;
import opengl.engine.SceneObject;

/**
 *
 * @author Andrey
 */
public class RenderPassMain extends RenderPassStandart{

    public RenderPassMain(Scene scene) {
        super(scene);
    }

    @Override
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName) {
        if (objName.equals("screen")){
            FrameBuffer colorBuf = scene.getFrameBuffersStorage().get("ColorBuffer");
            GLSLProgramObject programObject = (GLSLProgramObject)sceneObj.getShadersPrograms().values().toArray()[0];
            
            programObject.bind(gl);

            colorBuf.setTexture(programObject);
            execObjShaderProg(gl, sceneObj, "Main", programObject);
            
            programObject.unbind(gl);
            return;
        }
        
        super.renderObject(gl, sceneObj, objName); 
    }
    
}
