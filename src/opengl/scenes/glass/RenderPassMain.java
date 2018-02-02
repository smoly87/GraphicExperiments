/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.glass;

import opengl.scenes.testscene.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL4;
import opengl.engine.EngineException;
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

    protected boolean optShadowMapping ;

    public boolean isOptShadowMapping() {
        return optShadowMapping;
    }

    public void setOptShadowMapping(boolean optShadowMapping) {
        this.optShadowMapping = optShadowMapping;
    }

    public boolean isOptColorMapping() {
        return optColorMapping;
    }

    public void setOptColorMapping(boolean colorMapping) {
        this.optColorMapping = colorMapping;
    }
    protected boolean optColorMapping ;
    
    public RenderPassMain(Scene scene) {
        super(scene);
    }

    @Override
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName) {
        if (optColorMapping && objName.equals("glass")){
            FrameBuffer colorBuf = scene.getFrameBuffersStorage().get("ColorBuffer");
            GLSLProgramObject programObject = (GLSLProgramObject)sceneObj.getShadersPrograms().values().toArray()[0];
            
            programObject.bind(gl);

            try {
                colorBuf.setTextureCubeMap(programObject);
            } catch (EngineException ex) {
                Logger.getLogger(RenderPassMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            execObjShaderProg(gl, sceneObj, "Main", programObject);
            
            programObject.unbind(gl);
            return;
        } else{
            super.renderObject(gl, sceneObj, objName); 
        }
        
        
    }
    
}
